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
        LOG.info("[TEST] Testing getAllSchedules method with multiple entries");
        
        // Mocking multiple teams and arenas
        ProTeamEntity t1 = new ProTeamEntity(); t1.setTeamId(1L);
        ProTeamEntity t2 = new ProTeamEntity(); t2.setTeamId(2L);
        ProArenaEntity a1 = new ProArenaEntity(); a1.setArenaId(101L);
        ProArenaEntity a2 = new ProArenaEntity(); a2.setArenaId(102L);

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.setHomeTeam(t1); // Team 1
        s1.setArena(a1); // Arena 1
        s1.setScheduledDate("2026-05-01");
        s1.setTicketPrice(50.0);
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.setHomeTeam(t2); // Team 2
        s2.setArena(a2); // Arena 2
        s2.setScheduledDate("2026-05-15");
        s2.setTicketPrice(60.0);

        ProScheduleEntity s3 = new ProScheduleEntity();
        s3.setHomeTeam(t1); // Team 1 again
        s3.setArena(a2); // Arena 2
        s3.setScheduledDate("2026-06-01");
        s3.setTicketPrice(55.0);

        when(proScheduleRepo.findAll()).thenReturn(Arrays.asList(s1, s2, s3));
        
        List<ProScheduleEntity> result = proScheduleService.getAllSchedules();
        
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetScheduleSuccess() {
        LOG.info("[TEST] Testing getSchedule method - success");
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.setScheduleId(1L);
        schedule.setScheduledDate("2026-05-01");
        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        ProScheduleEntity result = proScheduleService.getSchedule(1L);
        assertNotNull(result);
        assertEquals("2026-05-01", result.getScheduledDate());
    }

    @Test
    public void testGetScheduleNotFound() {
        LOG.info("[TEST] Testing getSchedule method - not found");
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.getSchedule(999L));
    }

    @Test
    public void testGetSchedulesByTeam() {
        LOG.info("[TEST] Testing getSchedulesByTeam method");
        ProTeamEntity t1 = new ProTeamEntity(); t1.setTeamId(1L);
        ProTeamEntity t2 = new ProTeamEntity(); t2.setTeamId(2L);
        ProArenaEntity a1 = new ProArenaEntity(); a1.setArenaId(101L);

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.setHomeTeam(t1);
        s1.setAwayTeam(t2);
        s1.setArena(a1);
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.setHomeTeam(t2);
        s2.setAwayTeam(t1);
        s2.setArena(a1);

        when(proScheduleRepo.findByHomeTeam_TeamId(1L)).thenReturn(new java.util.ArrayList<>(java.util.Arrays.asList(s1)));
        when(proScheduleRepo.findByAwayTeam_TeamId(1L)).thenReturn(new java.util.ArrayList<>(java.util.Arrays.asList(s2)));
        
        List<ProScheduleEntity> resultT1 = proScheduleService.getSchedulesByTeam(1L);
        
        assertEquals(2, resultT1.size());
    }

    @Test
    public void testGetSchedulesByArena() {
        LOG.info("[TEST] Testing getSchedulesByArena method with multiple arenas");
        ProTeamEntity t1 = new ProTeamEntity(); t1.setTeamId(1L);
        ProTeamEntity t2 = new ProTeamEntity(); t2.setTeamId(2L);
        ProArenaEntity a1 = new ProArenaEntity(); a1.setArenaId(101L);

        ProScheduleEntity s1 = new ProScheduleEntity();
        s1.setHomeTeam(t1);
        s1.setArena(a1);
        
        ProScheduleEntity s2 = new ProScheduleEntity();
        s2.setHomeTeam(t2);
        s2.setArena(a1);

        when(proScheduleRepo.findByArena_ArenaId(101L)).thenReturn(Arrays.asList(s1, s2));
        when(proScheduleRepo.findByArena_ArenaId(102L)).thenReturn(Arrays.asList(new ProScheduleEntity())); // Arena 102 has 1 schedule
        
        List<ProScheduleEntity> resultA1 = proScheduleService.getSchedulesByArena(101L);
        List<ProScheduleEntity> resultA2 = proScheduleService.getSchedulesByArena(102L);
        
        assertEquals(2, resultA1.size());
        assertEquals(1, resultA2.size());
    }

    @Test
    public void testCreateSchedule() {
        LOG.info("[TEST] Testing createSchedule method");
        ProTeamEntity t1 = new ProTeamEntity(); t1.setTeamId(1L);
        ProArenaEntity a1 = new ProArenaEntity(); a1.setArenaId(1L);

        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.setHomeTeam(t1);
        schedule.setArena(a1);
        schedule.setScheduledDate("2026-05-01");
        schedule.setTicketPrice(50.0);
        
        when(proScheduleRepo.save(schedule)).thenReturn(schedule);
        
        ProScheduleEntity result = proScheduleService.createSchedule(schedule);
        
        assertNotNull(result);
        assertEquals(1L, result.getHomeTeam().getTeamId());
        assertEquals(50.0, result.getTicketPrice());
    }

    @Test
    public void testUpdateScheduleSuccess() {
        LOG.info("[TEST] Testing updateSchedule method - success");
        ProTeamEntity t1 = new ProTeamEntity(); t1.setTeamId(1L);
        ProTeamEntity t2 = new ProTeamEntity(); t2.setTeamId(2L);
        ProArenaEntity a1 = new ProArenaEntity(); a1.setArenaId(1L);
        ProArenaEntity a2 = new ProArenaEntity(); a2.setArenaId(2L);

        ProScheduleEntity existingSchedule = new ProScheduleEntity();
        existingSchedule.setScheduleId(1L);
        existingSchedule.setHomeTeam(t1);
        existingSchedule.setAwayTeam(t2);
        existingSchedule.setArena(a1);
        existingSchedule.setScheduledDate("2026-05-01");
        existingSchedule.setTicketPrice(50.0);

        ProScheduleEntity updateReq = new ProScheduleEntity();
        updateReq.setHomeTeam(t2);
        updateReq.setAwayTeam(t1);
        updateReq.setArena(a2);
        updateReq.setScheduledDate("2026-06-01");
        updateReq.setTicketPrice(75.0);

        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(existingSchedule));
        when(proScheduleRepo.save(any(ProScheduleEntity.class))).thenReturn(existingSchedule);

        ProScheduleEntity result = proScheduleService.updateSchedule(1L, updateReq);
        assertNotNull(result);
        assertEquals(2L, result.getHomeTeam().getTeamId());
        assertEquals(1L, result.getAwayTeam().getTeamId());
        assertEquals("2026-06-01", result.getScheduledDate());
        assertEquals(75.0, result.getTicketPrice());
    }

    @Test
    public void testUpdateScheduleNotFound() {
        LOG.info("[TEST] Testing updateSchedule method - not found");
        ProScheduleEntity updateReq = new ProScheduleEntity();
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.updateSchedule(999L, updateReq));
    }

    @Test
    public void testDeleteScheduleSuccess() {
        LOG.info("[TEST] Testing deleteSchedule method - success");
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.setScheduleId(1L);
        schedule.setScheduledDate("2026-05-01");
        when(proScheduleRepo.findById(1L)).thenReturn(Optional.of(schedule));
        String result = proScheduleService.deleteSchedule(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proScheduleRepo, times(1)).delete(schedule);
    }

    @Test
    public void testDeleteScheduleNotFound() {
        LOG.info("[TEST] Testing deleteSchedule method - not found");
        when(proScheduleRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proScheduleService.deleteSchedule(999L));
    }
}
