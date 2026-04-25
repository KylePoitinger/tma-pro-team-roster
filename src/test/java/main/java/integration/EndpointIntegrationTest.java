package main.java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndpointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testHealthCheck() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/health", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    public void testGetTeams() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/teams", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateAndGetTeam() {
        String teamJson = "{\"name\":\"Test Team\",\"city\":\"Test City\",\"mascot\":\"Test Mascot\"}";
        ResponseEntity<Map> postResponse = restTemplate.postForEntity(getBaseUrl() + "/teams", 
            new org.springframework.http.HttpEntity<>(teamJson, createJsonHeaders()), Map.class);
        
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        Object teamId = postResponse.getBody().get("teamId");
        assertNotNull(teamId);

        ResponseEntity<Map> getResponse = restTemplate.getForEntity(getBaseUrl() + "/teams/" + teamId + "/roster", Map.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Test Team", getResponse.getBody().get("name"));
    }

    @Test
    public void testCreateAndGetSchedule() {
        String scheduleJson = "{\"player\":{\"playerId\":1},\"arena\":{\"arenaId\":1},\"scheduledDate\":\"2026-06-01\",\"ticketPrice\":75.0}";
        ResponseEntity<Map> postResponse = restTemplate.postForEntity(getBaseUrl() + "/schedules",
            new org.springframework.http.HttpEntity<>(scheduleJson, createJsonHeaders()), Map.class);

        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        Object scheduleId = postResponse.getBody().get("scheduleId");
        assertNotNull(scheduleId);

        ResponseEntity<Map> getResponse = restTemplate.getForEntity(getBaseUrl() + "/schedules/" + scheduleId, Map.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        
        Map player = (Map) getResponse.getBody().get("player");
        assertNotNull(player);
        assertEquals(1, player.get("playerId"));
        
        Map arena = (Map) getResponse.getBody().get("arena");
        assertNotNull(arena);
        assertEquals(1, arena.get("arenaId"));
        
        assertEquals(75.0, getResponse.getBody().get("ticketPrice"));
    }

    private org.springframework.http.HttpHeaders createJsonHeaders() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return headers;
    }
}
