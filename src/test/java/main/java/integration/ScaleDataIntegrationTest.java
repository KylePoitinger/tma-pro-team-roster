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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
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

public class ScaleDataIntegrationTest extends BaseIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScaleDataIntegrationTest.class);

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    public void testScaleExpansion() {
        LOG.info("[TEST] Starting Scale Expansion Test: 8 Teams, 5 Arenas, 11 Players per Team");

        // 1. Verify 5 Arenas
        ResponseEntity<List> arenasResponse = restTemplate.getForEntity(getBaseUrl() + "/arenas", List.class);
        assertEquals(5, arenasResponse.getBody().size());
        LOG.info("[TEST] Verified 5 arenas exist.");

        // 2. Verify 8 Teams
        ResponseEntity<List> teamsResponse = restTemplate.getForEntity(getBaseUrl() + "/teams", List.class);
        assertEquals(8, teamsResponse.getBody().size());
        LOG.info("[TEST] Verified 8 teams exist.");

        // 3. Verify 11 Players for each homeTeam
        for (int i = 1; i <= 8; i++) {
            ResponseEntity<Map> rosterResponse = restTemplate.getForEntity(getBaseUrl() + "/teams/" + i + "/roster", Map.class);
            List players = (List) rosterResponse.getBody().get("proPlayers");
            assertNotNull(players);
            assertEquals(11, players.size());
            LOG.info("[TEST] Verified Team {} has 11 players.", i);
        }
    }
}
