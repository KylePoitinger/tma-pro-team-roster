package main.java.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/images")
public class MascotImageController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final main.java.service.MascotImageService mascotImageService;

    public MascotImageController(main.java.service.MascotImageService mascotImageService) {
        this.mascotImageService = mascotImageService;
    }

    @GetMapping(value = "/random-mascot", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getRandomMascotImage() {
        try {
            String imageUrl = mascotImageService.fetchRandomMascotImage();
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            // Return a 204 No Content or a generic 404 if we can't get an image
            return ResponseEntity.notFound().build();
        }
    }
}
