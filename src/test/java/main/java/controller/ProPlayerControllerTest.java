package main.java.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProPlayerEntity;
import main.java.service.ProPlayerService;

@ExtendWith(MockitoExtension.class)
public class ProPlayerControllerTest {

    @Mock
    private ProPlayerService proPlayerService;

    @InjectMocks
    private ProPlayerController proPlayerController;

    @Test
    public void testGetProPlayer() {
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "Test Player";
        when(proPlayerService.getProPlayer(1L)).thenReturn(player);

        ProPlayerEntity result = proPlayerController.getProPlayer(1L);

        assertNotNull(result);
        assertEquals("Test Player", result.name);
        verify(proPlayerService, times(1)).getProPlayer(1L);
    }

    @Test
    public void testCreateProPlayer() {
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "New Player";
        when(proPlayerService.createProPlayer(player)).thenReturn(player);

        ProPlayerEntity result = proPlayerController.createProPlayer(player);

        assertNotNull(result);
        assertEquals("New Player", result.name);
        verify(proPlayerService, times(1)).createProPlayer(player);
    }

    @Test
    public void testUpdateProPlayer() {
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "Updated Player";
        when(proPlayerService.updateProPlayer(1L, player)).thenReturn(player);

        ProPlayerEntity result = proPlayerController.updateProPlayer(1L, player);

        assertNotNull(result);
        assertEquals("Updated Player", result.name);
        verify(proPlayerService, times(1)).updateProPlayer(1L, player);
    }

    @Test
    public void testDeleteProPlayer() {
        when(proPlayerService.deleteProPlayer(1L)).thenReturn("Delete was successful for player:1");

        String result = proPlayerController.deleteProPlayer(1L);

        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proPlayerService, times(1)).deleteProPlayer(1L);
    }
}

