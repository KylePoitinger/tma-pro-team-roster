package main.java.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MascotImageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MascotImageService mascotImageService;

    @Test
    public void testFetchRandomMascotImage() {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("message", "http://example.com/image.jpg");
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        String result = mascotImageService.fetchRandomMascotImage();
        assertEquals("http://example.com/image.jpg", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testFetchRandomMascotImageFailure() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        mascotImageService.fetchRandomMascotImage();
    }
}
