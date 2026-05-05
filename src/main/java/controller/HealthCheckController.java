package main.java.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @Autowired(required = false)
    private ServletWebServerApplicationContext webServerAppContext;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();

        boolean dbOk = checkDatabase();
        boolean rosterApiOk = checkRosterApi();

        status.put("service", "Team Roster Microapp");
        status.put("status", (dbOk && rosterApiOk) ? "UP" : "DEGRADED");
        status.put("database", dbOk ? "UP" : "DOWN");
        status.put("rosterApi", rosterApiOk ? "UP" : "DOWN");
        status.put("timestamp", Instant.now().toString());

        HttpStatus httpStatus = (dbOk && rosterApiOk) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity<>(status, httpStatus);
    }

    /**
     * Returns the actual port the backend is running on.
     * This is used by frontend services (Node.js, Python) to discover the backend URL dynamically.
     */
    @GetMapping("/port")
    public ResponseEntity<Map<String, Object>> getPort() {
        Map<String, Object> portInfo = new HashMap<>();
        int port = webServerAppContext != null ? webServerAppContext.getWebServer().getPort() : 8080;
        portInfo.put("port", port);
        portInfo.put("url", "http://localhost:" + port);
        return new ResponseEntity<>(portInfo, HttpStatus.OK);
    }

    private boolean checkDatabase() {
        // TODO: Replace with real DB check
        return true;
    }

    private boolean checkRosterApi() {
        // TODO: Replace with real external API ping
        return true;
    }
}
