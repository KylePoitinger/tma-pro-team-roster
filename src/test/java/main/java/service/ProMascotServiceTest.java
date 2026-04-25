package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProMascotEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProMascotRepo;

@ExtendWith(MockitoExtension.class)
public class ProMascotServiceTest {

    @Mock
    private ProMascotRepo proMascotRepo;

    @Mock
    private MascotImageService mascotImageService;

    @InjectMocks
    private ProMascotService proMascotService;

    private static final Logger LOG = LoggerFactory.getLogger(ProMascotServiceTest.class);

    @Test
    public void testCreateProMascot() {
        LOG.info("Testing createProMascot method");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        mascot.species = "Test Species";
        mascot.description = "Test Description";
        mascot.costume = "Test Costume";
        when(proMascotRepo.save(mascot)).thenReturn(mascot);
        ProMascotEntity created = proMascotService.createProMascot(mascot);
        LOG.info("Created mascot: {}", created.name);
        assertNotNull(created);
        assertEquals("Test Mascot", created.name);
    }

    @Test
    public void testGetProMascotSuccess() {
        LOG.info("Testing getProMascot method - success");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        when(proMascotRepo.getOneByMascotId(1L)).thenReturn(mascot);
        ProMascotEntity result = proMascotService.getProMascot(1L);
        assertNotNull(result);
        assertEquals("Test Mascot", result.name);
    }

    @Test
    public void testGetProMascotNotFound() {
        LOG.info("Testing getProMascot method - not found");
        when(proMascotRepo.getOneByMascotId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proMascotService.getProMascot(999L));
    }

    @Test
    public void testGetMascotsByTeam() {
        LOG.info("Testing getMascotsByTeam method");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        List<ProMascotEntity> mockMascots = Arrays.asList(mascot);
        when(proMascotRepo.findByTeam_TeamId(1L)).thenReturn(mockMascots);
        List<ProMascotEntity> result = proMascotService.getMascotsByTeam(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateProMascotSuccess() {
        LOG.info("Testing updateProMascot method - success");
        ProMascotEntity existingMascot = new ProMascotEntity();
        existingMascot.mascotId = 1L;
        existingMascot.name = "Old Mascot";
        existingMascot.description = "Old Description";
        existingMascot.costume = "Old Costume";

        ProMascotEntity updateReq = new ProMascotEntity();
        updateReq.name = "New Mascot";
        updateReq.species = "New Species";
        updateReq.description = "New Description";
        updateReq.costume = "New Costume";
        updateReq.setImageUrl("http://newimage.jpg");

        when(proMascotRepo.findById(1L)).thenReturn(Optional.of(existingMascot));
        when(proMascotRepo.save(any(ProMascotEntity.class))).thenReturn(existingMascot);

        ProMascotEntity result = proMascotService.updateProMascot(1L, updateReq);
        assertNotNull(result);
        assertEquals("New Mascot", result.name);
        assertEquals("New Description", result.description);
        assertEquals("New Costume", result.costume);
        assertEquals("http://newimage.jpg", result.getImageUrl());
    }

    @Test
    public void testUpdateProMascotPartial() {
        LOG.info("Testing updateProMascot method - partial update");
        ProMascotEntity existingMascot = new ProMascotEntity();
        existingMascot.mascotId = 1L;
        existingMascot.name = "Old Mascot";

        ProMascotEntity updateReq = new ProMascotEntity();
        updateReq.name = null; // Should not update name
        updateReq.species = "New Species";

        when(proMascotRepo.findById(1L)).thenReturn(Optional.of(existingMascot));
        when(proMascotRepo.save(any(ProMascotEntity.class))).thenReturn(existingMascot);

        ProMascotEntity result = proMascotService.updateProMascot(1L, updateReq);
        assertNotNull(result);
        assertEquals("Old Mascot", result.name);
        assertEquals("New Species", result.species);
    }

    @Test
    public void testUpdateProMascotWithDefaultImage() {
        LOG.info("Testing updateProMascot method - with default image");
        ProMascotEntity existingMascot = new ProMascotEntity();
        existingMascot.mascotId = 1L;
        existingMascot.setImageUrl("old_url");

        ProMascotEntity updateReq = new ProMascotEntity();
        updateReq.setImageUrl(null); // Should not update image to "/images/random-mascot" via getImageUrl() if we use setter

        when(proMascotRepo.findById(1L)).thenReturn(Optional.of(existingMascot));
        when(proMascotRepo.save(any(ProMascotEntity.class))).thenReturn(existingMascot);

        ProMascotEntity result = proMascotService.updateProMascot(1L, updateReq);
        assertNotNull(result);
        assertEquals("old_url", result.getImageUrl());
    }

    @Test
    public void testUpdateProMascotNotFound() {
        LOG.info("Testing updateProMascot method - not found");
        ProMascotEntity updateReq = new ProMascotEntity();
        when(proMascotRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proMascotService.updateProMascot(999L, updateReq));
    }

    @Test
    public void testDeleteProMascotSuccess() {
        LOG.info("Testing deleteProMascot method - success");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        when(proMascotRepo.getOneByMascotId(1L)).thenReturn(mascot);
        String result = proMascotService.deleteProMascot(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proMascotRepo, times(1)).delete(mascot);
    }

    @Test
    public void testDeleteProMascotNotFound() {
        LOG.info("Testing deleteProMascot method - not found");
        when(proMascotRepo.getOneByMascotId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proMascotService.deleteProMascot(999L));
    }

    @Test
    public void testGetMascotsUniqueImages() {
        LOG.info("Testing getMascots method with image fetching");
        ProMascotEntity m1 = new ProMascotEntity();
        m1.mascotId = 1L;
        m1.setImageUrl(null);

        ProMascotEntity m2 = new ProMascotEntity();
        m2.mascotId = 2L;
        m2.setImageUrl("/images/random-mascot");

        ProMascotEntity m3 = new ProMascotEntity();
        m3.mascotId = 3L;
        m3.setImageUrl("http://existing.com/image.jpg");

        List<ProMascotEntity> mockMascots = Arrays.asList(m1, m2, m3);
        when(proMascotRepo.findAll()).thenReturn(mockMascots);
        when(mascotImageService.fetchRandomMascotImage())
            .thenReturn("http://api.com/img1.jpg")
            .thenReturn("http://api.com/img2.jpg");

        List<ProMascotEntity> result = proMascotService.getMascots();

        assertEquals(3, result.size());
        assertEquals("http://api.com/img1.jpg", result.get(0).getImageUrl());
        assertEquals("http://api.com/img2.jpg", result.get(1).getImageUrl());
        assertEquals("http://existing.com/image.jpg", result.get(2).getImageUrl());
        verify(proMascotRepo, times(2)).save(any(ProMascotEntity.class));
    }

    @Test
    public void testGetMascotsDuplicateHandling() {
        LOG.info("Testing getMascots method duplicate image handling");
        ProMascotEntity m1 = new ProMascotEntity();
        m1.mascotId = 1L;
        m1.setImageUrl(null);

        ProMascotEntity m2 = new ProMascotEntity();
        m2.mascotId = 2L;
        m2.setImageUrl(null);

        List<ProMascotEntity> mockMascots = Arrays.asList(m1, m2);
        when(proMascotRepo.findAll()).thenReturn(mockMascots);
        when(mascotImageService.fetchRandomMascotImage())
            .thenReturn("http://dup.com/img.jpg") // for m1
            .thenReturn("http://dup.com/img.jpg") // first try for m2 (duplicate)
            .thenReturn("http://unique.com/img.jpg"); // second try for m2

        List<ProMascotEntity> result = proMascotService.getMascots();

        assertEquals("http://dup.com/img.jpg", result.get(0).getImageUrl());
        assertEquals("http://unique.com/img.jpg", result.get(1).getImageUrl());
    }

    @Test
    public void testGetMascotsImageServiceException() {
        LOG.info("Testing getMascots method when image service fails");
        ProMascotEntity m1 = new ProMascotEntity();
        m1.mascotId = 1L;
        m1.setImageUrl(null);

        when(proMascotRepo.findAll()).thenReturn(Arrays.asList(m1));
        when(mascotImageService.fetchRandomMascotImage()).thenThrow(new RuntimeException("API Down"));

        List<ProMascotEntity> result = proMascotService.getMascots();

        assertEquals("/images/random-mascot", result.get(0).getImageUrl());
        verify(proMascotRepo, never()).save(any(ProMascotEntity.class));
    }

    @Test
    public void testGetRandomMascot() {
        LOG.info("Testing getRandomMascot method");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Random Mascot";

        when(proMascotRepo.findRandomMascot()).thenReturn(mascot);
        when(mascotImageService.fetchRandomMascotImage()).thenReturn("http://example.com/image.jpg");
        when(proMascotRepo.save(any(ProMascotEntity.class))).thenReturn(mascot);

        ProMascotEntity result = proMascotService.getRandomMascot();
        assertNotNull(result);
        assertEquals("Random Mascot", result.name);
        assertEquals("http://example.com/image.jpg", result.getImageUrl());
    }

    @Test
    public void testGetRandomMascotNotFound() {
        LOG.info("Testing getRandomMascot method - not found");
        when(proMascotRepo.findRandomMascot()).thenReturn(null);
        ProMascotEntity result = proMascotService.getRandomMascot();
        assertNull(result);
    }

    @Test
    public void testGetRandomMascotImageServiceException() {
        LOG.info("Testing getRandomMascot method - image service exception");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Random Mascot";
        mascot.setImageUrl("old_url");

        when(proMascotRepo.findRandomMascot()).thenReturn(mascot);
        when(mascotImageService.fetchRandomMascotImage()).thenThrow(new RuntimeException("API Down"));

        ProMascotEntity result = proMascotService.getRandomMascot();
        assertNotNull(result);
        assertEquals("old_url", result.getImageUrl());
        verify(proMascotRepo, never()).save(any());
    }
}