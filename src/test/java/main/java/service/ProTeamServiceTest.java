package main.java.service;

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
import main.java.repository.ProTeamRepo;

@ExtendWith(MockitoExtension.class)
public class ProTeamServiceTest {

    @Mock
    private ProTeamRepo proTeamRepo;

    @InjectMocks
    private ProTeamService proTeamService;

    @Test
    public void testGetTeams() {
        List<ProTeamEntity> mockTeams = Arrays.asList(new ProTeamEntity());
        when(proTeamRepo.findAll()).thenReturn(mockTeams);
        List<ProTeamEntity> teams = proTeamService.getTeams();
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void testCreateTeam() {
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        team.city = "Test City";
        team.mascot = "Test Mascot";
        when(proTeamRepo.save(team)).thenReturn(team);
        ProTeamEntity created = proTeamService.createTeam(team);
        assertNotNull(created);
        assertEquals("Test Team", created.name);
    }

    // Add more tests for other methods
}