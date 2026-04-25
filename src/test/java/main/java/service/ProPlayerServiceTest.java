package main.java.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import main.java.entity.ProPlayerEntity;
import main.java.repository.ProPlayerRepo;

@RunWith(MockitoJUnitRunner.class)
public class ProPlayerServiceTest {

    @Mock
    private ProPlayerRepo proPlayerRepo;

    @InjectMocks
    private ProPlayerService proPlayerService;

    @Test
    public void testCreateProPlayer() {
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "Test Player";
        player.position = "QB";
        player.teamName = "Test Team";
        player.age = 25;
        player.height = 72;
        player.weight = 200;
        player.college = "Test College";
        player.salary = 1000000L;
        when(proPlayerRepo.save(player)).thenReturn(player);
        ProPlayerEntity created = proPlayerService.createProPlayer(player);
        assertNotNull(created);
        assertEquals("Test Player", created.name);
    }

    // Add more tests
}
