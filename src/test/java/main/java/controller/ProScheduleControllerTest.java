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

import main.java.entity.ProScheduleEntity;
import main.java.service.ProScheduleService;

@ExtendWith(MockitoExtension.class)
public class ProScheduleControllerTest {

    @Mock
    private ProScheduleService proScheduleService;

    @InjectMocks
    private ProScheduleController proScheduleController;

    @Test
    public void testGetAllSchedules() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        List<ProScheduleEntity> mockSchedules = Arrays.asList(schedule);
        when(proScheduleService.getAllSchedules()).thenReturn(mockSchedules);

        List<ProScheduleEntity> result = proScheduleController.getAllSchedules();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proScheduleService, times(1)).getAllSchedules();
    }

    @Test
    public void testGetSchedule() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        schedule.scheduledDate = "2026-05-01";
        when(proScheduleService.getSchedule(1L)).thenReturn(schedule);

        ProScheduleEntity result = proScheduleController.getSchedule(1L);

        assertNotNull(result);
        assertEquals("2026-05-01", result.scheduledDate);
        verify(proScheduleService, times(1)).getSchedule(1L);
    }

    @Test
    public void testGetSchedulesByTeam() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        List<ProScheduleEntity> mockSchedules = Arrays.asList(schedule);
        when(proScheduleService.getSchedulesByTeam(1L)).thenReturn(mockSchedules);

        List<ProScheduleEntity> result = proScheduleController.getSchedulesByTeam(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proScheduleService, times(1)).getSchedulesByTeam(1L);
    }

    @Test
    public void testGetSchedulesByArena() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        List<ProScheduleEntity> mockSchedules = Arrays.asList(schedule);
        when(proScheduleService.getSchedulesByArena(1L)).thenReturn(mockSchedules);

        List<ProScheduleEntity> result = proScheduleController.getSchedulesByArena(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proScheduleService, times(1)).getSchedulesByArena(1L);
    }

    @Test
    public void testCreateSchedule() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        schedule.ticketPrice = 50.0;
        when(proScheduleService.createSchedule(schedule)).thenReturn(schedule);

        ProScheduleEntity result = proScheduleController.createSchedule(schedule);

        assertNotNull(result);
        assertEquals(50.0, result.ticketPrice);
        verify(proScheduleService, times(1)).createSchedule(schedule);
    }

    @Test
    public void testUpdateSchedule() {
        ProScheduleEntity schedule = new ProScheduleEntity();
        schedule.scheduleId = 1L;
        schedule.ticketPrice = 75.0;
        when(proScheduleService.updateSchedule(1L, schedule)).thenReturn(schedule);

        ProScheduleEntity result = proScheduleController.updateSchedule(1L, schedule);

        assertNotNull(result);
        assertEquals(75.0, result.ticketPrice);
        verify(proScheduleService, times(1)).updateSchedule(1L, schedule);
    }

    @Test
    public void testDeleteSchedule() {
        when(proScheduleService.deleteSchedule(1L)).thenReturn("Delete was successful for schedule:1");

        String result = proScheduleController.deleteSchedule(1L);

        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proScheduleService, times(1)).deleteSchedule(1L);
    }
}

