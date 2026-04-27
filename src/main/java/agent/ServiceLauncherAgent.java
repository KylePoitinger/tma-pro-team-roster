package main.java.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * ServiceLauncherAgent is a specialized agent component that automatically
 * orchestrates the startup of auxiliary services (Python Dashboard and Node.js Manager Portal)
 * when the main Java application is launched.
 */
@Component
@Profile("!test")
public class ServiceLauncherAgent implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceLauncherAgent.class);
    
    @Autowired(required = false)
    private ServletWebServerApplicationContext webServerAppContext;

    private String pythonStatus = "SKIPPED";
    private String nodeStatus = "SKIPPED";
    private String resolvedPython = null;
    private String resolvedNode = null;

    @Override
    public void run(String... args) throws Exception {
        if ("true".equalsIgnoreCase(System.getProperty("skip.service.launcher"))) {
            LOG.info("[ServiceLauncherAgent] Execution skipped due to 'skip.service.launcher' system property.");
            return;
        }

        LOG.info("[ServiceLauncherAgent] Initializing external services...");

        // Resolve commands
        LOG.info("[ServiceLauncherAgent] Resolving Python command...");
        resolvedPython = findCommandPath(isWindows() ? "python" : "python3");
        
        LOG.info("[ServiceLauncherAgent] Resolving Node/npm command...");
        resolvedNode = findCommandPath("npm");

        LOG.info("[ServiceLauncherAgent] Command resolution complete. Python: {}, Node: {}", 
                 resolvedPython != null ? "FOUND" : "NOT FOUND", 
                 resolvedNode != null ? "FOUND" : "NOT FOUND");

        // Check environment for Python Dashboard
        if (checkEnvironment("Python Dashboard", "src/main/python", resolvedPython)) {
            pythonStatus = "STARTING";
            startService("Python Dashboard (Streamlit)", 
                         "src/main/python", 
                         Arrays.asList(resolvedPython, "-m", "streamlit", "run", "dashboard.py", "--server.headless", "true"),
                         resolvedPython);
        }

        // Check environment for Manager Portal
        if (checkEnvironment("Manager Portal", "src/main/nodejs/manager-portal", resolvedNode)) {
            nodeStatus = "STARTING";
            startService("Manager Portal (Node.js)", 
                         "src/main/nodejs/manager-portal", 
                         isWindows() ? Arrays.asList("cmd.exe", "/c", "\"\"" + resolvedNode + "\" start\"") 
                                     : Arrays.asList(resolvedNode, "start"),
                         resolvedNode);
        }

        printServerAdminSummary();
    }

    private void printServerAdminSummary() {
        int backendPort = webServerAppContext != null ? webServerAppContext.getWebServer().getPort() : 8080;
        String backendUrl = "http://localhost:" + backendPort;

        LOG.info("");
        LOG.info("[ServiceLauncherAgent] ============================================================");
        LOG.info("[ServiceLauncherAgent] TMA PRO TEAM ROSTER - SERVER ADMIN SUMMARY");
        LOG.info("[ServiceLauncherAgent] ============================================================");
        LOG.info("[ServiceLauncherAgent] 1. Java Backend:           {}", backendUrl);
        LOG.info("[ServiceLauncherAgent]    - Swagger UI:           {}/swagger-ui.html", backendUrl);
        LOG.info("[ServiceLauncherAgent]    - H2 Console:           {}/h2-console", backendUrl);
        LOG.info("[ServiceLauncherAgent] 2. Python Dashboard:       http://localhost:8501 (Status: {})", pythonStatus);
        LOG.info("[ServiceLauncherAgent] 3. Node.js Manager Portal: http://localhost:3000 (Status: {})", nodeStatus);
        LOG.info("[ServiceLauncherAgent] ------------------------------------------------------------");
        LOG.info("[ServiceLauncherAgent] OS: {}, Java: {}", System.getProperty("os.name"), System.getProperty("java.version"));
        LOG.info("[ServiceLauncherAgent] ============================================================");
        LOG.info("");
    }

    private boolean checkEnvironment(String serviceName, String directory, String resolvedPath) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            LOG.warn("[ServiceLauncherAgent] Skipping {}: Directory not found: {}", serviceName, directory);
            return false;
        }

        if (resolvedPath == null) {
            LOG.error("[ServiceLauncherAgent] Skipping {}: Required command not found. Please ensure dependencies are installed.", serviceName);
            return false;
        }

        return true;
    }

    private String findCommandPath(String command) {
        // 1. Check if it's in PATH using system-specific 'where' or 'which'
        try {
            String checkCmd = isWindows() ? "where.exe" : "which";
            ProcessBuilder pb = new ProcessBuilder(checkCmd, command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                String firstFound = null;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (firstFound == null) firstFound = line;
                    // On Windows, prefer non-WindowsApps paths (shims) if available
                    if (isWindows() && !line.toLowerCase().contains("windowsapps")) {
                        return line;
                    }
                    if (!isWindows()) return line;
                }
                // Wait up to 3 seconds for the command-discovery process to finish
                if (process.waitFor(3, java.util.concurrent.TimeUnit.SECONDS) && process.exitValue() == 0 && firstFound != null) {
                    return firstFound;
                }
            }
        } catch (IOException | InterruptedException ignored) {
            // Fall back to manual checks
        }

        // 2. Check common installation paths if on Windows
        if (isWindows()) {
            if ("npm".equals(command)) {
                List<String> fallbacks = Arrays.asList(
                    "C:\\Program Files\\nodejs\\npm.cmd",
                    "C:\\Program Files (x86)\\nodejs\\npm.cmd",
                    System.getenv("APPDATA") + "\\npm\\npm.cmd"
                );
                for (String path : fallbacks) {
                    if (new File(path).exists()) return path;
                }
            } else if (command.startsWith("python")) {
                List<String> fallbacks = Arrays.asList(
                    "C:\\Python312\\python.exe",
                    "C:\\Python311\\python.exe",
                    "C:\\Python310\\python.exe",
                    "C:\\Python39\\python.exe",
                    "C:\\Windows\\python.exe",
                    System.getenv("LOCALAPPDATA") + "\\Programs\\Python\\Python312\\python.exe",
                    System.getenv("LOCALAPPDATA") + "\\Programs\\Python\\Python311\\python.exe",
                    System.getenv("LOCALAPPDATA") + "\\Programs\\Python\\Python310\\python.exe"
                );
                for (String path : fallbacks) {
                    if (new File(path).exists()) return path;
                }
            }
        }
        
        return null;
    }

    private void startService(String serviceName, String directory, List<String> command, String resolvedPath) {
        new Thread(() -> {
            try {
                LOG.info("[ServiceLauncherAgent] Launching {}...", serviceName);
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.directory(new File(directory));
                
                // Get the actual backend port
                int backendPort = webServerAppContext != null ? webServerAppContext.getWebServer().getPort() : 8080;
                String backendUrl = "http://localhost:" + backendPort;

                // Add common env vars to prevent interactive prompts and ensure unbuffered output
                pb.environment().put("STREAMLIT_BROWSER_GATHER_USAGE_STATS", "false");
                pb.environment().put("PYTHONUNBUFFERED", "1");
                pb.environment().put("CI", "true"); // Helps with npm and other tools to run in non-interactive mode
                pb.environment().put("BACKEND_URL", backendUrl); // Pass backend URL to child processes

                // Add resolved path's directory to PATH to ensure sub-commands work (e.g., npm needing node)
                if (resolvedPath != null) {
                    File binary = new File(resolvedPath);
                    String parentDir = binary.getParent();
                    if (parentDir != null) {
                        String pathVar = isWindows() ? "Path" : "PATH";
                        String currentPath = pb.environment().getOrDefault(pathVar, "");
                        if (!currentPath.contains(parentDir)) {
                            pb.environment().put(pathVar, parentDir + File.pathSeparator + currentPath);
                        }
                    }
                }

                pb.redirectErrorStream(true);
                Process process = pb.start();

                // Consume output in a separate thread to avoid blocking and integrate logs
                new Thread(() -> {
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            LOG.info("[{}] {}", serviceName, line);
                        }
                    } catch (IOException e) {
                        LOG.error("[ServiceLauncherAgent] Error reading {} output: {}", serviceName, e.getMessage());
                    }
                }, serviceName + "-Output-Thread").start();

                LOG.info("[ServiceLauncherAgent] {} started successfully.", serviceName);
                
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    LOG.error("[ServiceLauncherAgent] {} exited with error code: {}", serviceName, exitCode);
                } else {
                    LOG.info("[ServiceLauncherAgent] {} shut down gracefully.", serviceName);
                }
            } catch (IOException | InterruptedException e) {
                LOG.error("[ServiceLauncherAgent] Error running {}: {}", serviceName, e.getMessage());
                Thread.currentThread().interrupt();
            }
        }, serviceName + "-Thread").start();
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
