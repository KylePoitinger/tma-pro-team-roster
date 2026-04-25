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

import main.java.entity.ProTeamEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProTeamRepo;

@ExtendWith(MockitoExtension.class)
public class ProTeamServiceTest {

    @Mock
    private ProTeamRepo proTeamRepo;

    @InjectMocks
    private ProTeamService proTeamService;

    private static final Logger LOG = LoggerFactory.getLogger(ProTeamServiceTest.class);

    @Test
    public void testGetTeams() {
        LOG.info("Testing getTeams method");
        List<ProTeamEntity> mockTeams = Arrays.asList(new ProTeamEntity());
        when(proTeamRepo.findAll()).thenReturn(mockTeams);
        List<ProTeamEntity> teams = proTeamService.getTeams();
        LOG.info("Retrieved {} teams", teams.size());
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void testCreateTeam() {
        LOG.info("Testing createTeam method");
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        team.city = "Test City";
        team.mascot = "Test Mascot";
        when(proTeamRepo.save(team)).thenReturn(team);
        ProTeamEntity created = proTeamService.createTeam(team);
        LOG.info("Created team: {}", created.name);
        assertNotNull(created);
        assertEquals("Test Team", created.name);
    }

    @Test
    public void testGetSingleTeamAndRosterSuccess() {
        LOG.info("Testing getSingleTeamAndRoster method - success");
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        when(proTeamRepo.getOneByTeamId(1L)).thenReturn(team);
        ProTeamEntity result = proTeamService.getSingleTeamAndRoster(1L);
        assertNotNull(result);
        assertEquals("Test Team", result.name);
    }

    @Test
    public void testGetSingleTeamAndRosterNotFound() {
        LOG.info("Testing getSingleTeamAndRoster method - not found");
        when(proTeamRepo.getOneByTeamId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.getSingleTeamAndRoster(999L));
    }

    @Test
    public void testGetTeamsByFieldLookupByName() {
        LOG.info("Testing getTeamsByFieldLookup - by name");
        ProTeamEntity team = new ProTeamEntity();
        team.name = "Lakers";
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByName("Lakers")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup("Lakers", null, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lakers", result.get(0).name);
    }

    @Test
    public void testGetTeamsByFieldLookupByCity() {
        LOG.info("Testing getTeamsByFieldLookup - by city");
        ProTeamEntity team = new ProTeamEntity();
        team.city = "Los Angeles";
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByCity("Los Angeles")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup(null, "Los Angeles", null);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTeamsByFieldLookupByMascot() {
        LOG.info("Testing getTeamsByFieldLookup - by mascot");
        ProTeamEntity team = new ProTeamEntity();
        team.mascot = "Laker Girl";
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByMascot("Laker Girl")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup(null, null, "Laker Girl");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTeamsByFieldLookupNoCriteria() {
        LOG.info("Testing getTeamsByFieldLookup - no criteria");
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.getTeamsByFieldLookup(null, null, null));
    }

    @Test
    public void testUpdateTeamSuccess() {
        LOG.info("Testing updateTeam method - success");
        ProTeamEntity existingTeam = new ProTeamEntity();
        existingTeam.teamId = 1L;
        existingTeam.name = "Old Team";
        existingTeam.city = "Old City";
        existingTeam.mascot = "Old Mascot";

        ProTeamEntity updateReq = new ProTeamEntity();
        updateReq.name = "New Team";
        updateReq.city = "New City";
        updateReq.mascot = "New Mascot";

        when(proTeamRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(proTeamRepo.save(any(ProTeamEntity.class))).thenReturn(existingTeam);

        ProTeamEntity result = proTeamService.updateTeam(1L, updateReq);
        assertNotNull(result);
        assertEquals("New Team", result.name);
        assertEquals("New City", result.city);
    }

    @Test
    public void testUpdateTeamNotFound() {
        LOG.info("Testing updateTeam method - not found");
        ProTeamEntity updateReq = new ProTeamEntity();
        when(proTeamRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.updateTeam(999L, updateReq));
    }

    @Test
    public void testDeleteTeamSuccess() {
        LOG.info("Testing deleteTeam method - success");
        ProTeamEntity team = new ProTeamEntity();
        team.teamId = 1L;
        team.name = "Test Team";
        when(proTeamRepo.getOneByTeamId(1L)).thenReturn(team);
        String result = proTeamService.deleteTeam(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proTeamRepo, times(1)).delete(team);
    }

    @Test
    public void testDeleteTeamNotFound() {
        LOG.info("Testing deleteTeam method - not found");
        when(proTeamRepo.getOneByTeamId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.deleteTeam(999L));
    }
}