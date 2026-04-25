package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProArenaEntity;
import main.java.entity.ProTeamEntity;
import main.java.entity.ProScheduleEntity;
import main.java.exception.ResourceNotFoundException;
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
        
        // Mocking multiple teams and arenas
        ProTeamEntity t1 = new ProTeamEntity(); t1.teamId = 1L;
        ProTeamEntity t2 = new ProTeamEntity(); t2.teamId = 2L;
        ProArenaEntity a1 = new ProArenaEntity(); a1.arenaId = 101L;
        ProArenaEntity a2 = new ProArenaEntity(); a2.arenaId = 102L;

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.homeTeam = t1; // Team 1
        s1.arena = a1; // Arena 1
        s1.scheduledDate = "2026-05-01";
        s1.ticketPrice = 50.0;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.homeTeam = t2; // Team 2
        s2.arena = a2; // Arena 2
        s2.scheduledDate = "2026-05-15";
        s2.ticketPrice = 60.0;

        ProScheduleEntity s3 = new ProScheduleEntity();
        s3.homeTeam = t1; // Team 1 again
        s3.arena = a2; // Arena 2
        s3.scheduledDate = "2026-06-01";
        s3.ticketPrice = 55.0;

        when(proScheduleRepo.findAll()).thenReturn(Arrays.asList(s1, s2, s3));
        
        List<ProScheduleEntity> result = proScheduleService.getAllSchedules();
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetScheduleSuccess() {
        LOG.info("Testing getSchedule method - success");
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        schedule.scheduledDate = "2026-05-01";
        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        ProScheduleEntity result = proScheduleService.getSchedule(1L);
        assertNotNull(result);
        assertEquals("2026-05-01", result.scheduledDate);
    }

    @Test
    public void testGetScheduleNotFound() {
        LOG.info("Testing getSchedule method - not found");
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.getSchedule(999L));
    }

    @Test
    public void testGetSchedulesByTeam() {
        LOG.info("[DEBUG_LOG] Testing getSchedulesByTeam method");
        ProTeamEntity t1 = new ProTeamEntity(); t1.teamId = 1L;
        ProTeamEntity t2 = new ProTeamEntity(); t2.teamId = 2L;
        ProArenaEntity a1 = new ProArenaEntity(); a1.arenaId = 101L;

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.homeTeam = t1;
        s1.awayTeam = t2;
        s1.arena = a1;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.homeTeam = t2;
        s2.awayTeam = t1;
        s2.arena = a1;

        when(proScheduleRepo.findByHomeTeam_TeamId(1L)).thenReturn(new java.util.ArrayList<>(java.util.Arrays.asList(s1)));
        when(proScheduleRepo.findByAwayTeam_TeamId(1L)).thenReturn(new java.util.ArrayList<>(java.util.Arrays.asList(s2)));
        
        List<ProScheduleEntity> resultT1 = proScheduleService.getSchedulesByTeam(1L);
        
        assertEquals(2, resultT1.size());
    }

    @Test
    public void testGetSchedulesByArena() {
        LOG.info("[DEBUG_LOG] Testing getSchedulesByArena method with multiple arenas");
        ProTeamEntity t1 = new ProTeamEntity(); t1.teamId = 1L;
        ProTeamEntity t2 = new ProTeamEntity(); t2.teamId = 2L;
        ProArenaEntity a1 = new ProArenaEntity(); a1.arenaId = 101L;

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.homeTeam = t1;
        s1.arena = a1;
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.homeTeam = t2;
        s2.arena = a1;

        when(proScheduleRepo.findByArena_ArenaId(101L)).thenReturn(Arrays.asList(s1, s2));
        when(proScheduleRepo.findByArena_ArenaId(102L)).thenReturn(Arrays.asList(new ProScheduleEntity())); // Arena 102 has 1 schedule
        
        List<ProScheduleEntity> resultA1 = proScheduleService.getSchedulesByArena(101L);
        List<ProScheduleEntity> resultA2 = proScheduleService.getSchedulesByArena(102L);
        
        assertEquals(2, resultA1.size());
        assertEquals(1, resultA2.size());
    }

    @Test
    public void testCreateSchedule() {
        LOG.info("[DEBUG_LOG] Testing createSchedule method");
        ProTeamEntity t1 = new ProTeamEntity(); t1.teamId = 1L;
        ProArenaEntity a1 = new ProArenaEntity(); a1.arenaId = 1L;

        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.homeTeam = t1;
        schedule.arena = a1;
        schedule.scheduledDate = "2026-05-01";
        schedule.ticketPrice = 50.0;
        
        when(proScheduleRepo.save(schedule)).thenReturn(schedule);
        
        ProScheduleEntity result = proScheduleService.createSchedule(schedule);
        
        assertNotNull(result);
        assertEquals(1L, result.homeTeam.teamId);
        assertEquals(50.0, result.ticketPrice);
    }

    @Test
    public void testUpdateScheduleSuccess() {
        LOG.info("Testing updateSchedule method - success");
        ProTeamEntity t1 = new ProTeamEntity(); t1.teamId = 1L;
        ProTeamEntity t2 = new ProTeamEntity(); t2.teamId = 2L;
        ProArenaEntity a1 = new ProArenaEntity(); a1.arenaId = 1L;
        ProArenaEntity a2 = new ProArenaEntity(); a2.arenaId = 2L;

        ProScheduleEntity existingSchedule = new ProScheduleEntity();
        existingSchedule.scheduleId = 1L;
        existingSchedule.homeTeam = t1;
        existingSchedule.awayTeam = t2;
        existingSchedule.arena = a1;
        existingSchedule.scheduledDate = "2026-05-01";
        existingSchedule.ticketPrice = 50.0;

        ProScheduleEntity updateReq = new ProScheduleEntity();
        updateReq.homeTeam = t2;
        updateReq.awayTeam = t1;
        updateReq.arena = a2;
        updateReq.scheduledDate = "2026-06-01";
        updateReq.ticketPrice = 75.0;

        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(existingSchedule));
        when(proScheduleRepo.save(any(ProScheduleEntity.class))).thenReturn(existingSchedule);

        ProScheduleEntity result = proScheduleService.updateSchedule(1L, updateReq);
        assertNotNull(result);
        assertEquals(2L, result.homeTeam.teamId);
        assertEquals(1L, result.awayTeam.teamId);
        assertEquals("2026-06-01", result.scheduledDate);
        assertEquals(75.0, result.ticketPrice);
    }

    @Test
    public void testUpdateScheduleNotFound() {
        LOG.info("Testing updateSchedule method - not found");
        ProScheduleEntity updateReq = new ProScheduleEntity();
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.updateSchedule(999L, updateReq));
    }

    @Test
    public void testDeleteScheduleSuccess() {
        LOG.info("Testing deleteSchedule method - success");
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        schedule.scheduledDate = "2026-05-01";
        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        String result = proScheduleService.deleteSchedule(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proScheduleRepo, times(1)).delete(schedule);
    }

    @Test
    public void testDeleteScheduleNotFound() {
        LOG.info("Testing deleteSchedule method - not found");
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.deleteSchedule(999L));
    }
}
