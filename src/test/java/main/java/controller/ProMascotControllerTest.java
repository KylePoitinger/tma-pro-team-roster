package main.java.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProMascotEntity;
import main.java.service.ProMascotService;

@ExtendWith(MockitoExtension.class)
public class ProMascotControllerTest {

    @Mock
    private ProMascotService proMascotService;

    @InjectMocks
    private ProMascotController proMascotController;

    @Test
    public void testGetProMascot() {
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        when(proMascotService.getProMascot(1L)).thenReturn(mascot);

        ProMascotEntity result = proMascotController.getProMascot(1L);

        assertNotNull(result);
        assertEquals("Test Mascot", result.name);
        verify(proMascotService, times(1)).getProMascot(1L);
    }

    @Test
    public void testGetMascotsByTeam() {
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        List<ProMascotEntity> mockMascots = Arrays.asList(mascot);
        when(proMascotService.getMascotsByTeam(1L)).thenReturn(mockMascots);

        List<ProMascotEntity> result = proMascotController.getMascotsByTeam(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proMascotService, times(1)).getMascotsByTeam(1L);
    }

    @Test
    public void testCreateProMascot() {
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "New Mascot";
        when(proMascotService.createProMascot(mascot)).thenReturn(mascot);

        ProMascotEntity result = proMascotController.createProMascot(mascot);

        assertNotNull(result);
        assertEquals("New Mascot", result.name);
        verify(proMascotService, times(1)).createProMascot(mascot);
    }

    @Test
    public void testUpdateProMascot() {
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Updated Mascot";
        when(proMascotService.updateProMascot(1L, mascot)).thenReturn(mascot);

        ProMascotEntity result = proMascotController.updateProMascot(1L, mascot);

        assertNotNull(result);
        assertEquals("Updated Mascot", result.name);
        verify(proMascotService, times(1)).updateProMascot(1L, mascot);
    }

    @Test
    public void testDeleteProMascot() {
        when(proMascotService.deleteProMascot(1L)).thenReturn("Delete was successful for mascot:1");

        String result = proMascotController.deleteProMascot(1L);

        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proMascotService, times(1)).deleteProMascot(1L);
    }
}

