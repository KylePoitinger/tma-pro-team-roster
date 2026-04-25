package main.java.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import main.java.entity.ProArenaEntity;
import main.java.repository.ProArenaRepo;

@RunWith(MockitoJUnitRunner.class)
public class ProArenaServiceTest {

    @Mock
    private static ProArenaRepo proArenaRepo;

    @InjectMocks
    private ProArenaService proArenaService;

    @Test
    public void testGetArenas() {
        List<ProArenaEntity> mockArenas = Arrays.asList(new ProArenaEntity());
        when(proArenaRepo.findAll()).thenReturn(mockArenas);
        List<ProArenaEntity> arenas = ProArenaService.getArenas();
        assertNotNull(arenas);
        assertEquals(1, arenas.size());
    }

    @Test
    public void testCreateArena() {
        ProArenaEntity arena = new ProArenaEntity();
        arena.arenaId = 1L;
        arena.name = "Test Arena";
        arena.location = "Test City";
        arena.capacity = 20000;
        arena.teamName = "Test Team";
        when(proArenaRepo.save(arena)).thenReturn(arena);
        ProArenaEntity created = ProArenaService.createArena(arena);
        assertNotNull(created);
        assertEquals("Test Arena", created.name);
    }

    // Add more tests
}
