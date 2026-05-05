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

import main.java.entity.ProArenaEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.service.ProArenaService;

@ExtendWith(MockitoExtension.class)
public class ProArenaControllerTest {

    @Mock
    private ProArenaService proArenaService;

    @InjectMocks
    private ProArenaController proArenaController;

    @Test
    public void testGetArenas() {
        ProArenaEntity arena = new ProArenaEntity();
        arena.setArenaId(1L);
        arena.setName("Test Arena");
        List<ProArenaEntity> mockArenas = Arrays.asList(arena);
        when(proArenaService.getArenas()).thenReturn(mockArenas);

        List<ProArenaEntity> result = proArenaController.getArenas();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proArenaService, times(1)).getArenas();
    }

    @Test
    public void testGetArena() {
        ProArenaEntity arena = new ProArenaEntity();
        arena.setArenaId(1L);
        arena.setName("Test Arena");
        when(proArenaService.getArena(1L)).thenReturn(arena);

        ProArenaEntity result = proArenaController.getArena(1L);

        assertNotNull(result);
        assertEquals("Test Arena", result.getName());
        verify(proArenaService, times(1)).getArena(1L);
    }

    @Test
    public void testCreateArena() {
        ProArenaEntity arena = new ProArenaEntity();
        arena.setArenaId(1L);
        arena.setName("New Arena");
        when(proArenaService.createArena(arena)).thenReturn(arena);

        ProArenaEntity result = proArenaController.createArena(arena);

        assertNotNull(result);
        assertEquals("New Arena", result.getName());
        verify(proArenaService, times(1)).createArena(arena);
    }

    @Test
    public void testUpdateArena() {
        ProArenaEntity arena = new ProArenaEntity();
        arena.setArenaId(1L);
        arena.setName("Updated Arena");
        when(proArenaService.updateArena(1L, arena)).thenReturn(arena);

        ProArenaEntity result = proArenaController.updateArena(1L, arena);

        assertNotNull(result);
        assertEquals("Updated Arena", result.getName());
        verify(proArenaService, times(1)).updateArena(1L, arena);
    }

    @Test
    public void testDeleteArena() {
        when(proArenaService.deleteArena(1L)).thenReturn("Delete was successful for arena:1");

        String result = proArenaController.deleteArena(1L);

        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proArenaService, times(1)).deleteArena(1L);
    }
}

