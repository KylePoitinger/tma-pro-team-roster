const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');
const fs = require('fs');
const { spawn } = require('child_process');
const { Kafka, logLevel: LogLevel } = require('kafkajs');
const sqlite3 = require('sqlite3').verbose();
const treeKill = require('tree-kill');

let mainWindow;
const processes = {
  java: null,
  python: null,
  node: null,
  kafka: null
};

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 900,
    height: 700,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true
    },
    title: "TMA Pro Team Launcher"
  });

  mainWindow.loadFile('index.html');
}

app.whenReady().then(() => {
  createWindow();

  app.on('activate', function () {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') {
    // Kill all child processes before quitting
    let killCount = 0;
    Object.values(processes).forEach(p => {
      if (p) {
        killCount++;
        const signal = process.platform === 'win32' ? 'SIGKILL' : 'SIGTERM';
        treeKill(p.pid, signal, (err) => {
          if (err) {
            try {
              p.kill(signal);
            } catch (e) {
              console.error('Failed to kill process:', e);
            }
          }
        });
      }
    });

    // Wait briefly for processes to terminate gracefully
    if (killCount > 0) {
      setTimeout(() => {
        app.quit();
      }, 500);
    } else {
      app.quit();
    }
  }
});

// IPC Handlers for Process Management
function startService(service) {
  if (processes[service]) return { status: 'already_running' };

  let command, args, cwd;
  const rootDir = path.join(__dirname, '../../..');

  switch (service) {
    case 'java':
      command = 'mvn';
      args = ['spring-boot:run'];
      cwd = rootDir;
      break;
    case 'python':
      command = 'python';
      args = ['-m', 'streamlit', 'run', 'dashboard.py', '--server.headless', 'true'];
      cwd = path.join(rootDir, 'src/main/python');
      break;
    case 'node':
      // Auto-start Kafka if requested (as per user requirement)
      if (!processes['kafka']) {
        console.log('[Launcher] Auto-starting Kafka for Manager Portal...');
        startService('kafka');
      }
      command = 'node';
      args = ['app.js'];
      cwd = path.join(rootDir, 'src/main/nodejs/manager-portal');
      break;
    case 'kafka':
      // Construct command based on KAFKA_HOME if available
      if (process.env.KAFKA_HOME) {
        const kafkaHome = process.env.KAFKA_HOME;
        if (process.platform === 'win32') {
          command = path.join(kafkaHome, 'bin', 'windows', 'kafka-server-start.bat');
        } else {
          command = path.join(kafkaHome, 'bin', 'kafka-server-start.sh');
        }
        args = [path.join(kafkaHome, 'config', 'server.properties')];
        cwd = kafkaHome;

        if (!fs.existsSync(command)) {
          return { 
            status: 'error', 
            message: `Kafka binary not found at ${command}. Please check your KAFKA_HOME environment variable.` 
          };
        }
      } else {
        // Fallback to basic command and hope it's in PATH
        command = process.platform === 'win32' ? 'kafka-server-start.bat' : 'kafka-server-start.sh';
        args = ['config/server.properties'];
        cwd = rootDir;
        
        // Log a warning if KAFKA_HOME is missing, as it's the likely cause of failure
        setTimeout(() => {
          mainWindow.webContents.send('service-log', { 
            service: 'kafka', 
            data: '[Launcher] WARNING: KAFKA_HOME environment variable is not set. Kafka might fail to start if it is not in your PATH.' 
          });
        }, 100);
      }
      break;
    default:
      return { status: 'error', message: 'Unknown service' };
  }

  try {
    const p = spawn(command, args, { 
      cwd, 
      shell: true,
      env: { ...process.env, BACKEND_URL: 'http://localhost:8080' }
    });
    
    processes[service] = p;
    mainWindow.webContents.send('service-started', { service });
    mainWindow.webContents.send('service-log', { service, data: `[Launcher] Started with PID: ${p.pid}` });

    p.stdout.on('data', (data) => {
      console.log(`[${service}] stdout: ${data}`);
      mainWindow.webContents.send('service-log', { service, data: data.toString() });
    });

    p.stderr.on('data', (data) => {
      console.error(`[${service}] stderr: ${data}`);
      mainWindow.webContents.send('service-log', { service, data: data.toString() });
    });

    p.on('close', (code) => {
      console.log(`[${service}] process exited with code ${code}`);
      processes[service] = null;
      mainWindow.webContents.send('service-stopped', { service, code });
    });

    return { status: 'started' };
  } catch (error) {
    return { status: 'error', message: error.message };
  }
}

ipcMain.handle('start-service', (event, service) => {
  return startService(service);
});

ipcMain.handle('stop-service', (event, service) => {
  if (processes[service]) {
    const proc = processes[service];
    processes[service] = null;

    return new Promise((resolve) => {
      // Use tree-kill to terminate the entire process tree
      // Using SIGKILL on Windows ensures taskkill /F is used for forceful termination
      const signal = process.platform === 'win32' ? 'SIGKILL' : 'SIGTERM';
      
      console.log(`[Launcher] Stopping ${service} (PID: ${proc.pid}) with ${signal}`);
      mainWindow.webContents.send('service-log', { service, data: `[Launcher] Stopping service (PID: ${proc.pid})...` });

      treeKill(proc.pid, signal, (err) => {
        if (err) {
          console.error(`Failed to kill process tree for ${service}:`, err);
          // Fallback to direct kill
          try {
            proc.kill(signal);
          } catch (e) {}
        }
        resolve({ status: 'stopped' });
      });
    });
  }
  return { status: 'not_running' };
});

// Health Checks
ipcMain.handle('check-kafka', async () => {
  const kafka = new Kafka({
    clientId: 'launcher-health-check',
    brokers: ['127.0.0.1:9092'],
    connectionTimeout: 3000,
    requestTimeout: 3000,
    logLevel: LogLevel.NOTHING,
    retry: { retries: 0 }
  });

  const admin = kafka.admin();
  try {
    await admin.connect();
    await admin.disconnect();
    return { status: 'UP', message: 'Connected to Kafka broker' };
  } catch (error) {
    return { status: 'DOWN', message: 'Kafka unavailable: ' + error.message };
  }
});

ipcMain.handle('check-http', async (event, url) => {
  return new Promise((resolve) => {
    const protocol = url.startsWith('https') ? require('https') : require('http');
    const req = protocol.get(url, (res) => {
      res.on('data', () => {}); // Consume data
      if (res.statusCode >= 200 && res.statusCode < 400) {
        resolve({ status: 'UP', message: `Reachable (${res.statusCode})` });
      } else {
        resolve({ status: 'DOWN', message: `Status: ${res.statusCode}` });
      }
    });

    req.on('error', (err) => {
      resolve({ status: 'DOWN', message: 'Unreachable: ' + err.message });
    });

    req.setTimeout(3000, () => {
      req.destroy();
      resolve({ status: 'DOWN', message: 'Timeout' });
    });
  });
});

ipcMain.handle('check-sqlite', async () => {
  const dbPath = path.join(__dirname, '../../../pro_team_roster.db');
  return new Promise((resolve) => {
    const db = new sqlite3.Database(dbPath, sqlite3.OPEN_READONLY, (err) => {
      if (err) {
        resolve({ status: 'DOWN', message: err.message });
        return;
      }

      db.get('SELECT name FROM sqlite_master WHERE type="table" LIMIT 1', (err, row) => {
        db.close();
        if (err) {
          resolve({ status: 'DOWN', message: err.message });
        } else {
          resolve({ status: 'UP', message: 'SQLite database is accessible' });
        }
      });
    });
  });
});
