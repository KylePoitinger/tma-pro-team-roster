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
        LOG.info("[TEST] Testing getTeams method");
        List<ProTeamEntity> mockTeams = Arrays.asList(new ProTeamEntity());
        when(proTeamRepo.findAll()).thenReturn(mockTeams);
        List<ProTeamEntity> teams = proTeamService.getTeams();
        LOG.info("[TEST] Retrieved {} teams", teams.size());
        assertNotNull(teams);
        assertEquals(1, teams.size());
    }

    @Test
    public void testCreateTeam() {
        LOG.info("[TEST] Testing createTeam method");
        ProTeamEntity team = new ProTeamEntity();
        team.setTeamId(1L);
        team.setName("Test Team");
        team.setCity("Test City");
        team.setMascot("Test Mascot");
        when(proTeamRepo.save(team)).thenReturn(team);
        ProTeamEntity created = proTeamService.createTeam(team);
        LOG.info("[TEST] Created homeTeam: {}", created.getName());
        assertNotNull(created);
        assertEquals("Test Team", created.getName());
    }

    @Test
    public void testGetSingleTeamAndRosterSuccess() {
        LOG.info("[TEST] Testing getSingleTeamAndRoster method - success");
        ProTeamEntity team = new ProTeamEntity();
        team.setTeamId(1L);
        team.setName("Test Team");
        when(proTeamRepo.getOneByTeamId(1L)).thenReturn(team);
        ProTeamEntity result = proTeamService.getSingleTeamAndRoster(1L);
        assertNotNull(result);
        assertEquals("Test Team", result.getName());
    }

    @Test
    public void testGetSingleTeamAndRosterNotFound() {
        LOG.info("[TEST] Testing getSingleTeamAndRoster method - not found");
        when(proTeamRepo.getOneByTeamId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.getSingleTeamAndRoster(999L));
    }

    @Test
    public void testGetTeamsByFieldLookupByName() {
        LOG.info("[TEST] Testing getTeamsByFieldLookup - by name");
        ProTeamEntity team = new ProTeamEntity();
        team.setName("Lakers");
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByName("Lakers")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup("Lakers", null, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lakers", result.get(0).getName());
    }

    @Test
    public void testGetTeamsByFieldLookupByCity() {
        LOG.info("[TEST] Testing getTeamsByFieldLookup - by city");
        ProTeamEntity team = new ProTeamEntity();
        team.setCity("Los Angeles");
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByCity("Los Angeles")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup(null, "Los Angeles", null);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTeamsByFieldLookupByMascot() {
        LOG.info("[TEST] Testing getTeamsByFieldLookup - by mascot");
        ProTeamEntity team = new ProTeamEntity();
        team.setMascot("Laker Girl");
        List<ProTeamEntity> mockTeams = Arrays.asList(team);
        when(proTeamRepo.getTeamsByMascot("Laker Girl")).thenReturn(mockTeams);
        List<ProTeamEntity> result = proTeamService.getTeamsByFieldLookup(null, null, "Laker Girl");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTeamsByFieldLookupNoCriteria() {
        LOG.info("[TEST] Testing getTeamsByFieldLookup - no criteria");
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.getTeamsByFieldLookup(null, null, null));
    }

    @Test
    public void testUpdateTeamSuccess() {
        LOG.info("[TEST] Testing updateTeam method - success");
        ProTeamEntity existingTeam = new ProTeamEntity();
        existingTeam.setTeamId(1L);
        existingTeam.setName("Old Team");
        existingTeam.setCity("Old City");
        existingTeam.setMascot("Old Mascot");

        ProTeamEntity updateReq = new ProTeamEntity();
        updateReq.setName("New Team");
        updateReq.setCity("New City");
        updateReq.setMascot("New Mascot");

        when(proTeamRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(proTeamRepo.save(any(ProTeamEntity.class))).thenReturn(existingTeam);

        ProTeamEntity result = proTeamService.updateTeam(1L, updateReq);
        assertNotNull(result);
        assertEquals("New Team", result.getName());
        assertEquals("New City", result.getCity());
    }

    @Test
    public void testUpdateTeamNotFound() {
        LOG.info("[TEST] Testing updateTeam method - not found");
        ProTeamEntity updateReq = new ProTeamEntity();
        when(proTeamRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.updateTeam(999L, updateReq));
    }

    @Test
    public void testDeleteTeamSuccess() {
        LOG.info("[TEST] Testing deleteTeam method - success");
        ProTeamEntity team = new ProTeamEntity();
        team.setTeamId(1L);
        team.setName("Test Team");
        when(proTeamRepo.getOneByTeamId(1L)).thenReturn(team);
        String result = proTeamService.deleteTeam(1L);
        assertNotNull(result);
        assertTrue(result.contains("Delete was successful"));
        verify(proTeamRepo, times(1)).delete(team);
    }

    @Test
    public void testDeleteTeamNotFound() {
        LOG.info("[TEST] Testing deleteTeam method - not found");
        when(proTeamRepo.getOneByTeamId(999L)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> proTeamService.deleteTeam(999L));
    }
}