package main.java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwaggerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testSwaggerUi() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/swagger-ui.html", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testApiDocs() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/v3/api-docs", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
