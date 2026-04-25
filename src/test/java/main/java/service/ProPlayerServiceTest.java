package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProPlayerEntity;
import main.java.exception.ResourceNotFoundException;
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

    @Test
    public void testGetProPlayerSuccess() {
        LOG.info("Testing getProPlayer method - success");
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "Test Player";
        when(proPlayerRepo.getOneByPlayerId(1L)).thenReturn(player);
        ProPlayerEntity result = proPlayerService.getProPlayer(1L);
        assertNotNull(result);
        assertEquals("Test Player", result.name);
    }

    @Test
    public void testGetProPlayerNotFound() {
        LOG.info("Testing getProPlayer method - not found");
        when(proPlayerRepo.getOneByPlayerId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proPlayerService.getProPlayer(999L));
    }

    @Test
    public void testUpdateProPlayerSuccess() {
        LOG.info("Testing updateProPlayer method - success");
        ProPlayerEntity existingPlayer = new ProPlayerEntity();
        existingPlayer.playerId = 1L;
        existingPlayer.name = "Old Name";
        existingPlayer.position = "QB";
        existingPlayer.age = 25;
        existingPlayer.height = 72;
        existingPlayer.weight = 200;
        existingPlayer.college = "Old College";
        existingPlayer.salary = 1000000L;

        ProPlayerEntity updateReq = new ProPlayerEntity();
        updateReq.name = "New Name";
        updateReq.position = "RB";
        updateReq.age = 26;
        updateReq.height = 73;
        updateReq.weight = 210;
        updateReq.college = "New College";
        updateReq.salary = 1500000L;

        when(proPlayerRepo.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(proPlayerRepo.save(any(ProPlayerEntity.class))).thenReturn(existingPlayer);

        ProPlayerEntity result = proPlayerService.updateProPlayer(1L, updateReq);
        assertNotNull(result);
        assertEquals("New Name", result.name);
        assertEquals("RB", result.position);
        assertEquals(26, result.age);
        assertEquals(1500000L, result.salary);
    }

    @Test
    public void testUpdateProPlayerNotFound() {
        LOG.info("Testing updateProPlayer method - not found");
        ProPlayerEntity updateReq = new ProPlayerEntity();
        when(proPlayerRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proPlayerService.updateProPlayer(999L, updateReq));
    }

    @Test
    public void testDeleteProPlayerSuccess() {
        LOG.info("Testing deleteProPlayer method - success");
        ProPlayerEntity player = new ProPlayerEntity();
        player.playerId = 1L;
        player.name = "Test Player";
        when(proPlayerRepo.getOneByPlayerId(1L)).thenReturn(player);
        String result = proPlayerService.deleteProPlayer(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proPlayerRepo, times(1)).delete(player);
    }

    @Test
    public void testDeleteProPlayerNotFound() {
        LOG.info("Testing deleteProPlayer method - not found");
        when(proPlayerRepo.getOneByPlayerId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proPlayerService.deleteProPlayer(999L));
    }
}