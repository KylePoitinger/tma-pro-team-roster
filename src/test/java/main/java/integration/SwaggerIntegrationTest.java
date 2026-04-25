package main.java.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

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
