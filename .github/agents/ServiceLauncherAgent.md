# ServiceLauncherAgent – Auxiliary Service Orchestration Agent

## Purpose
The **ServiceLauncherAgent** is a specialized runtime component designed to automatically start and manage the lifecycle of auxiliary services required by the TMA Pro Team Roster application. 

By integrating into the Spring Boot startup sequence, it ensures that the **Python Dashboard (Streamlit)** and the **Manager Portal (Node.js)** are available as soon as the main backend is ready.

## Capabilities
- **Automated Startup**: Detects the host Operating System and executes the appropriate startup commands for Python and Node.js.
- **Environment Verification**: Checks for the existence of required project directories and ensures that necessary runtime commands (`python`/`python3` and `npm`) are available in the system `PATH` before launching.
- **Non-blocking Execution**: Launches services in dedicated background threads and consumes their output streams asynchronously to prevent blocking the main application or test runners.
- **Log Aggregation**: Integrates output from the child processes into the main application logs for easier debugging.
- **Lifecycle Monitoring**: Monitors the exit status of auxiliary services and logs any unexpected shutdowns.
- **Path Resolution & Injection**: Automatically resolves full paths for `python` and `npm` and injects their parent directories into the child process `PATH` to ensure sub-commands (like `node` called by `npm`) are correctly recognized.
- **Headless Execution**: Launches auxiliary services in headless mode (e.g., `--server.headless true` for Streamlit) to prevent browser-based interruptions.
- **Test-Awareness**: Automatically skips execution when the `test` Spring profile is active or when the `skip.service.launcher` system property is set to `true`.
- **Server Admin Summary**: Displays a centralized, structured summary of all service URLs (Backend, Swagger, H2, Dashboard, Manager Portal) and their startup status once initialization is complete.

## Configuration & Usage
The agent is implemented as a Spring `@Component` and implements `CommandLineRunner`. It is automatically picked up by Spring Boot's component scanning.

### Startup Commands
- **Python Dashboard**: `python -m streamlit run dashboard.py` (executed in `src/main/python`)
- **Manager Portal**: `npm start` (executed in `src/main/nodejs/manager-portal`)

### Requirements
- **Python 3.7+** and **Node.js 16+** must be installed and available in the system `PATH`.
- Dependencies for both services must be installed prior to running the Java application (`pip install -r requirements.txt` and `npm install`).

## Implementation Details
- **File**: `src/main/java/agent/ServiceLauncherAgent.java`
- **Package**: `main.java.agent`
- **Technology**: Java 21, Spring Boot, ProcessBuilder

## Maintenance
If new auxiliary services are added to the project, they should be registered within the `run()` method of the `ServiceLauncherAgent` class following the established pattern.
