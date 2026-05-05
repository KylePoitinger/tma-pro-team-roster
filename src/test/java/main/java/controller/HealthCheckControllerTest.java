package main.java.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthCheckControllerTest {

    private HealthCheckController healthCheckController = new HealthCheckController();

    @Test
    public void testHealthCheckReturnsOK() {
        ResponseEntity<Map<String, Object>> response = healthCheckController.health();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testHealthCheckBodyContainsRequiredFields() {
        ResponseEntity<Map<String, Object>> response = healthCheckController.health();
        Map<String, Object> body = response.getBody();

        assertNotNull(body);
        assertTrue(body.containsKey("service"));
        assertTrue(body.containsKey("status"));
        assertTrue(body.containsKey("database"));
        assertTrue(body.containsKey("rosterApi"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    public void testHealthCheckStatusIsUP() {
        ResponseEntity<Map<String, Object>> response = healthCheckController.health();
        Map<String, Object> body = response.getBody();

        assertNotNull(body);
        assertEquals("Team Roster Microapp", body.get("service"));
        assertEquals("UP", body.get("status"));
        assertEquals("UP", body.get("database"));
        assertEquals("UP", body.get("rosterApi"));
    }
}

