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

3. **Kafka Configuration (Optional but Recommended):**
   To manage the Kafka broker via the launcher, set the `KAFKA_HOME` environment variable to your Kafka installation directory.
   - **Windows**: `[System.Environment]::SetEnvironmentVariable('KAFKA_HOME', 'C:\path\to\kafka', 'User')`
   - **Unix**: `export KAFKA_HOME=/path/to/kafka`
   
   If `KAFKA_HOME` is not set, the launcher will attempt to use `kafka-server-start` from your system PATH.

## Running the Launcher

Start the Electron application:
```powershell
npm start
```

## Features

- **Service Control**: Start and stop Java, Python, and Node.js services with a single click.
- **Log Monitoring**: View stdout/stderr from all services directly in the launcher.
- **Health Checks**: 
  - **Kafka**: Verifies connectivity to the local Kafka broker (default: `127.0.0.1:9092`).
  - **SQLite**: Verifies that the `pro_team_roster.db` file is accessible and readable.
- **Theme**: Modern, high-energy sports aesthetic consistent with the project.
