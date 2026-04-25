package main.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MascotImageService {

    @Autowired
    private RestTemplate restTemplate;

    public String fetchRandomMascotImage() {
        String url = "https://dog.ceo/api/breeds/image/random";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && "success".equals(response.get("status"))) {
            return (String) response.get("message");
        }

        throw new IllegalStateException("Failed to fetch mascot image");
    }
}
