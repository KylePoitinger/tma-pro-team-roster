package main.java.integration;

import main.java.entity.ProArenaEntity;
import main.java.entity.ProPlayerEntity;
import main.java.entity.ProTeamEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScaleDataIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScaleDataIntegrationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    public void testScaleExpansion() {
        LOG.info("[DEBUG_LOG] Starting Scale Expansion Test: 10 Teams, 5 Arenas, 11 Players per Team");

        // 1. Create 10 Teams
        for (int i = 1; i <= 10; i++) {
            ProTeamEntity team = new ProTeamEntity();
            team.teamId = (long) i;
            team.name = "Team " + i;
            team.city = "City " + i;
            team.mascot = "Mascot " + i;
            
            ResponseEntity<ProTeamEntity> response = restTemplate.postForEntity(getBaseUrl() + "/teams", new HttpEntity<>(team, getHeaders()), ProTeamEntity.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            LOG.info("[DEBUG_LOG] Created Team: {}", team.name);

            // 2. Create 11 Players for each team
            for (int j = 1; j <= 11; j++) {
                ProPlayerEntity player = new ProPlayerEntity();
                player.playerId = (long) (i * 100 + j);
                player.name = "Player " + player.playerId;
                player.teamName = team.name; // Link to team
                player.position = "Position " + j;
                
                ResponseEntity<ProPlayerEntity> playerResponse = restTemplate.postForEntity(getBaseUrl() + "/players", new HttpEntity<>(player, getHeaders()), ProPlayerEntity.class);
                assertEquals(HttpStatus.OK, playerResponse.getStatusCode());
            }
            LOG.info("[DEBUG_LOG] Created 11 players for {}", team.name);
        }

        // 3. Create 5 Arenas
        for (int i = 1; i <= 5; i++) {
            ProArenaEntity arena = new ProArenaEntity();
            arena.arenaId = (long) i;
            arena.name = "Arena " + i;
            arena.location = "Location " + i;
            arena.capacity = 10000 * i;
            
            // Note: ProArenaController seems to be missing based on file structure check earlier, 
            // but let's check ProArenaController.java existence. 
            // Actually it exists: src\main\java\controller\ProArenaController.java
            
            ResponseEntity<ProArenaEntity> response = restTemplate.postForEntity(getBaseUrl() + "/arenas", new HttpEntity<>(arena, getHeaders()), ProArenaEntity.class);
            // If /arenas endpoint exists. Let's verify mapping in ProArenaController if possible or just try.
            assertEquals(HttpStatus.OK, response.getStatusCode());
            LOG.info("[DEBUG_LOG] Created Arena: {}", arena.name);
        }

        // Verification
        ResponseEntity<List> teamsResponse = restTemplate.getForEntity(getBaseUrl() + "/teams", List.class);
        assertEquals(10, teamsResponse.getBody().size());
        LOG.info("[DEBUG_LOG] Verified 10 teams exist.");

        // Check roster for one team
        ResponseEntity<Map> rosterResponse = restTemplate.getForEntity(getBaseUrl() + "/teams/1/roster", Map.class);
        List players = (List) rosterResponse.getBody().get("proPlayers");
        assertNotNull(players);
        assertEquals(11, players.size());
        LOG.info("[DEBUG_LOG] Verified Team 1 has 11 players.");
    }
}
