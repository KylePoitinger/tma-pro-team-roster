package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProPlayerEntity;
import main.java.repository.ProPlayerRepo;

@ExtendWith(MockitoExtension.class)
public class ProPlayerServiceTest {

    @Mock
    private ProPlayerRepo proPlayerRepo;

    @InjectMocks
    private ProPlayerService proPlayerService;

    private static final Logger LOG = LoggerFactory.getLogger(ProPlayerServiceTest.class);

    @Test
    public void testCreateProPlayer() {
        LOG.info("Testing createProPlayer method");
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
        LOG.info("Created player: {}", created.name);
        assertNotNull(created);
        assertEquals("Test Player", created.name);
    }

    // Add more tests
}