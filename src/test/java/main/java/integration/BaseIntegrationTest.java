package main.java.integration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    private static boolean isAppReady = false;

    @BeforeEach
    public void waitForApp() {
        if (!isAppReady) {
            LOG.info("[DEBUG_LOG] Waiting for application health check to be UP at http://localhost:{}/health", port);
            await()
                .atMost(60, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                try {
                    ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/health", Map.class);
                    boolean isUp = response.getStatusCode() == HttpStatus.OK && "UP".equals(response.getBody().get("status"));
                    if (isUp) {
                        LOG.info("[DEBUG_LOG] Health check returned UP!");
                    }
                    return isUp;
                } catch (Exception e) {
                    LOG.info("[DEBUG_LOG] Waiting for health check... (Error: {})", e.getMessage());
                    return false;
                }
            });
            isAppReady = true;
            LOG.info("[DEBUG_LOG] Application is ready for integration tests.");
        }
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
