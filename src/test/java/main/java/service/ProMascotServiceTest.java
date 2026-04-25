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

        when(proMascotRepo.findById(1L)).thenReturn(Optional.of(existingMascot));
        when(proMascotRepo.save(any(ProMascotEntity.class))).thenReturn(existingMascot);

        ProMascotEntity result = proMascotService.updateProMascot(1L, updateReq);
        assertNotNull(result);
        assertEquals("New Mascot", result.name);
        assertEquals("New Description", result.description);
        assertEquals("New Costume", result.costume);
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