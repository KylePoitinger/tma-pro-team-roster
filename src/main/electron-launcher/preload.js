const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  startService: (service) => ipcRenderer.invoke('start-service', service),
  stopService: (service) => ipcRenderer.invoke('stop-service', service),
  checkKafka: () => ipcRenderer.invoke('check-kafka'),
  checkSqlite: () => ipcRenderer.invoke('check-sqlite'),
  onServiceLog: (callback) => ipcRenderer.on('service-log', (event, value) => callback(value)),
  onServiceStopped: (callback) => ipcRenderer.on('service-stopped', (event, value) => callback(value))
});
