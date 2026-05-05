# TMA Pro Team Launcher

This is an Electron-based launcher for the TMA Pro Team Roster project. It allows you to start and stop the Java Backend, Python Dashboard, and Node.js Manager Portal from a single interface, and provides real-time health checks for Kafka and SQLite.

## Prerequisites

- **Node.js 16+** and **npm** must be installed.
- **Java 17+** and **Maven** (for the backend).
- **Python 3.7+** (for the dashboard).

## Setup

1. Navigate to the launcher directory:
   ```powershell
   cd src/main/electron-launcher
   ```

2. Install dependencies:
   ```powershell
   npm install
   ```

## Running the Launcher

Start the Electron application:
```powershell
npm start
```

## Features

- **Service Control**: Start and stop Java, Python, and Node.js services with a single click.
- **Log Monitoring**: View stdout/stderr from all services directly in the launcher.
- **Health Checks**: 
  - **Kafka**: Verifies connectivity to the local Kafka broker (default: `localhost:9092`).
  - **SQLite**: Verifies that the `pro_team_roster.db` file is accessible and readable.
- **Theme**: Modern, high-energy sports aesthetic consistent with the project.
