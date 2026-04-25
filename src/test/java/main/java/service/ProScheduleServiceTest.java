package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProScheduleEntity;
import main.java.repository.ProScheduleRepo;

@ExtendWith(MockitoExtension.class)
public class ProScheduleServiceTest {

    @Mock
    private ProScheduleRepo proScheduleRepo;

    @InjectMocks
    private ProScheduleService proScheduleService;

    private static final Logger LOG = LoggerFactory.getLogger(ProScheduleServiceTest.class);

    @Test
    public void testGetAllSchedules() {
        LOG.info("[DEBUG_LOG] Testing getAllSchedules method with multiple entries");
        
        // Mocking multiple players, teams (implicitly by IDs), and arenas
        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.playerId = 1L; // Player 1
        s1.arenaId = 101L; // Arena 1
        s1.scheduledDate = "2026-05-01";
        s1.ticketPrice = 50.0;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.playerId = 2L; // Player 2
        s2.arenaId = 102L; // Arena 2
        s2.scheduledDate = "2026-05-15";
        s2.ticketPrice = 60.0;

        ProScheduleEntity s3 = new ProScheduleEntity();
        s3.playerId = 1L; // Player 1 again
        s3.arenaId = 102L; // Arena 2
        s3.scheduledDate = "2026-06-01";
        s3.ticketPrice = 55.0;

        when(proScheduleRepo.findAll()).thenReturn(Arrays.asList(s1, s2, s3));
        
        List<ProScheduleEntity> result = proScheduleService.getAllSchedules();
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetSchedulesByPlayer() {
        LOG.info("[DEBUG_LOG] Testing getSchedulesByPlayer method with multiple players");
        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.playerId = 1L;
        s1.arenaId = 101L;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.playerId = 1L;
        s2.arenaId = 102L;

        when(proScheduleRepo.findByPlayerId(1L)).thenReturn(Arrays.asList(s1, s2));
        when(proScheduleRepo.findByPlayerId(2L)).thenReturn(Arrays.asList(new ProScheduleEntity())); // Player 2 has 1 schedule
        
        List<ProScheduleEntity> resultP1 = proScheduleService.getSchedulesByPlayer(1L);
        List<ProScheduleEntity> resultP2 = proScheduleService.getSchedulesByPlayer(2L);
        
        assertEquals(2, resultP1.size());
        assertEquals(1, resultP2.size());
    }

    @Test
    public void testGetSchedulesByArena() {
        LOG.info("[DEBUG_LOG] Testing getSchedulesByArena method with multiple arenas");
        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.playerId = 1L;
        s1.arenaId = 101L;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.playerId = 2L;
        s2.arenaId = 101L;

        when(proScheduleRepo.findByArenaId(101L)).thenReturn(Arrays.asList(s1, s2));
        when(proScheduleRepo.findByArenaId(102L)).thenReturn(Arrays.asList(new ProScheduleEntity())); // Arena 102 has 1 schedule
        
        List<ProScheduleEntity> resultA1 = proScheduleService.getSchedulesByArena(101L);
        List<ProScheduleEntity> resultA2 = proScheduleService.getSchedulesByArena(102L);
        
        assertEquals(2, resultA1.size());
        assertEquals(1, resultA2.size());
    }

    @Test
    public void testCreateSchedule() {
        LOG.info("[DEBUG_LOG] Testing createSchedule method");
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.playerId = 1L;
        schedule.arenaId = 1L;
        schedule.scheduledDate = "2026-05-01";
        schedule.ticketPrice = 50.0;
        
        when(proScheduleRepo.save(schedule)).thenReturn(schedule);
        
        ProScheduleEntity result = proScheduleService.createSchedule(schedule);
        
        assertNotNull(result);
        assertEquals(1L, result.playerId);
        assertEquals(50.0, result.ticketPrice);
    }
}
