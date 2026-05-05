const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const { Kafka } = require('kafkajs');
const sqlite3 = require('sqlite3').verbose();

let mainWindow;
const processes = {
  java: null,
  python: null,
  node: null
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
    Object.values(processes).forEach(p => {
      if (p) p.kill();
    });
    app.quit();
  }
});

// IPC Handlers for Process Management
ipcMain.handle('start-service', (event, service) => {
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
      command = 'npm';
      args = ['start'];
      cwd = path.join(rootDir, 'src/main/nodejs/manager-portal');
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
});

ipcMain.handle('stop-service', (event, service) => {
  if (processes[service]) {
    processes[service].kill();
    processes[service] = null;
    return { status: 'stopped' };
  }
  return { status: 'not_running' };
});

// Health Checks
ipcMain.handle('check-kafka', async () => {
  const kafka = new Kafka({
    clientId: 'launcher-health-check',
    brokers: ['localhost:9092'],
    retry: { retries: 0 }
  });

  const admin = kafka.admin();
  try {
    await admin.connect();
    await admin.disconnect();
    return { status: 'UP', message: 'Connected to Kafka broker' };
  } catch (error) {
    return { status: 'DOWN', message: error.message };
  }
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
