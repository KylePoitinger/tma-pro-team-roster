package main.java.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        if ("true".equalsIgnoreCase(System.getProperty("skip.service.launcher"))) {
            LOG.info("[ServiceLauncherAgent] Execution skipped due to 'skip.service.launcher' system property.");
            return;
        }

        LOG.info("[ServiceLauncherAgent] Initializing external services...");

        // Check environment for Python Dashboard
        String pythonCmd = isWindows() ? "python" : "python3";
        if (checkEnvironment("Python Dashboard", "src/main/python", pythonCmd)) {
            startService("Python Dashboard (Streamlit)", 
                         "src/main/python", 
                         isWindows() ? Arrays.asList("cmd.exe", "/c", "python -m streamlit run dashboard.py") 
                                     : Arrays.asList("python3", "-m", "streamlit", "run", "dashboard.py"));
        }

        // Check environment for Manager Portal
        if (checkEnvironment("Manager Portal", "src/main/nodejs/manager-portal", "npm")) {
            startService("Manager Portal (Node.js)", 
                         "src/main/nodejs/manager-portal", 
                         isWindows() ? Arrays.asList("cmd.exe", "/c", "npm start") 
                                     : Arrays.asList("npm", "start"));
        }
    }

    private boolean checkEnvironment(String serviceName, String directory, String commandToCheck) {
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            LOG.warn("[ServiceLauncherAgent] Skipping {}: Directory not found: {}", serviceName, directory);
            return false;
        }

        if (!isCommandAvailable(commandToCheck)) {
            LOG.warn("[ServiceLauncherAgent] Skipping {}: Command '{}' not found in PATH.", serviceName, commandToCheck);
            return false;
        }

        return true;
    }

    private boolean isCommandAvailable(String command) {
        try {
            String checkCmd = isWindows() ? "where" : "which";
            Process process = new ProcessBuilder(checkCmd, command).start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private void startService(String serviceName, String directory, List<String> command) {
        new Thread(() -> {
            try {
                LOG.info("[ServiceLauncherAgent] Launching {}...", serviceName);
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.directory(new File(directory));
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
