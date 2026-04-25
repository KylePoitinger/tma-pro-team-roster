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

import main.java.entity.ProTeamEntity;
import main.java.service.ProTeamService;

@ExtendWith(MockitoExtension.class)
public class ProTeamControllerTest {

    @Mock
    private ProTeamService proTeamService;

    @InjectMocks
    private ProTeamController proTeamController;

    @Test
    public void testGetTeams() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamService.getTeams()).thenReturn(mockTeams);

        List<ProTeamEntity> result = proTeamController.getTeams();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proTeamService, times(1)).getTeams();
    }

    @Test
    public void testGetSingleTeamAndRoster() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        when(proTeamService.getSingleTeamAndRoster(1L)).thenReturn(team);

        ProTeamEntity result = proTeamController.getSingleTeamAndRoster(1L);

        assertNotNull(result);
        assertEquals("Test Team", result.name);
        verify(proTeamService, times(1)).getSingleTeamAndRoster(1L);
    }

    @Test
    public void testGetTeamsByFieldLookup() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Lakers";
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamService.getTeamsByFieldLookup("Lakers", null, null)).thenReturn(mockTeams);

        List<ProTeamEntity> result = proTeamController.getTeamsByFieldLookup("Lakers", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(proTeamService, times(1)).getTeamsByFieldLookup("Lakers", null, null);
    }

    @Test
    public void testCreateTeam() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "New Team";
        when(proTeamService.createTeam(team)).thenReturn(team);

        ProTeamEntity result = proTeamController.createTeam(team);

        assertNotNull(result);
        assertEquals("New Team", result.name);
        verify(proTeamService, times(1)).createTeam(team);
    }

    @Test
    public void testUpdateTeam() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Updated Team";
        when(proTeamService.updateTeam(1L, team)).thenReturn(team);

        ProTeamEntity result = proTeamController.updateTeam(1L, team);

        assertNotNull(result);
        assertEquals("Updated Team", result.name);
        verify(proTeamService, times(1)).updateTeam(1L, team);
    }

    @Test
    public void testDeleteTeam() {
        when(proTeamService.deleteTeam(1L)).thenReturn("Delete was successful for team:1");

        String result = proTeamController.deleteTeam(1L);

        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proTeamService, times(1)).deleteTeam(1L);
    }
}

