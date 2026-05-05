package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class MascotImageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MascotImageService mascotImageService;

    private static final Logger LOG = LoggerFactory.getLogger(MascotImageServiceTest.class);

    @Test
    public void testFetchRandomMascotImage() {
        LOG.info("[TEST] Testing fetchRandomMascotImage method");
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("message", "http://example.com/image.jpg");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        String result = mascotImageService.fetchRandomMascotImage();
        LOG.info("[TEST] Fetched image URL: {}", result);
        assertEquals("http://example.com/image.jpg", result);
    }

    @Test
    public void testFetchRandomMascotImageFailure() {
        LOG.info("[TEST] Testing fetchRandomMascotImage failure scenario");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            mascotImageService.fetchRandomMascotImage();
        });
    }
}
