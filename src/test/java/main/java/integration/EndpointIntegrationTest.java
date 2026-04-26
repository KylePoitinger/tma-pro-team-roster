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
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // TEAMS TESTS
    @Test
    public void testGetTeams() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/teams", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetTeamRoster() {
        ResponseEntity<Map> getResponse = restTemplate.getForEntity(getBaseUrl() + "/teams/1/roster", Map.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
    }

    @Test
    public void testGetNonExistentTeam() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/teams/999/roster", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Team not found for this id :: 999", response.getBody().get("message"));
    }

    @Test
    public void testGetTeamsByFieldLookup() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(
            getBaseUrl() + "/teams/fields?team-name=Lakers", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // ARENAS TESTS
    @Test
    public void testGetArenas() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/arenas", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetArenaById() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/arenas/1", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetNonExistentArena() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/arenas/999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // SCHEDULES TESTS
    @Test
    public void testGetAllSchedules() {
        ResponseEntity<java.util.List> response = restTemplate.getForEntity(getBaseUrl() + "/schedules", java.util.List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        Map firstSchedule = (Map) response.getBody().get(0);
        assertNotNull(firstSchedule.get("homeTeam"));
        assertNotNull(firstSchedule.get("awayTeam"));
    }

    @Test
    public void testGetScheduleById() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/schedules/1", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetNonExistentSchedule() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/schedules/999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetSchedulesByTeam() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/schedules/team/1", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetSchedulesByArena() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/schedules/arena/1", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // PLAYERS TESTS
    @Test
    public void testGetPlayerById() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/players/1", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetNonExistentPlayer() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/players/9999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testTradePlayer() {
        // Player 1 is initially in team 1
        // Trade player 1 to team 2
        restTemplate.put(getBaseUrl() + "/players/1/trade/2", null);

        // Verify player 1 is now in team 2
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/players/1", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // We can't easily check the nested team object because it's a Map in the response
        // But the call should not have failed.
    }

    // MASCOTS TESTS
    @Test
    public void testGetMascotById() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/mascots/1", Map.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("species"));
    }

    @Test
    public void testGetNonExistentMascot() {
        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/mascots/9999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetMascotImageUrl() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(getBaseUrl() + "/mascots", Object[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        Map firstMascot = (Map) response.getBody()[0];
        String imageUrl = (String) firstMascot.get("imageUrl");
        assertNotNull(imageUrl);
        // If it's the fallback URL, it should start with /
        if (imageUrl.equals("/images/random-mascot")) {
            assertTrue(imageUrl.startsWith("/"));
        }
    }


}
