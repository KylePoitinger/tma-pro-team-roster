package test.java;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.java.entity.ProTeamEntity;
import main.java.repository.ProTeamRepo;
import main.java.service.ProTeamService;

public class ProTeamServiceTest {

	private ProTeamService proTeamService;
	private ProTeamRepo proTeamRepo;

	@Mock
	private ProTeamEntity proTeam;

	@Before
	public void setupMock() {
		proTeamService = new ProTeamService(proTeamRepo);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockCreation() {
		assertNotNull(proTeam);
	}

	@Test
	public void get_all_teams_test() {
		assertTrue(ProTeamService.getTeams().size() == 3);
	}

	@Test
	public void get_single_team_test() {
		assertTrue(ProTeamService.getSingleTeamAndRoster(1).name == "Hornets");
	}

	@Test
	public void get_teams_by_field_lookup_test() {
		assertTrue(ProTeamService.getTeamsByFieldLookup(null, "Hickory", null).get(0).name == "Switch");
	}

	@Test
	public void create_team_test() {
		ProTeamService.createTeam(proTeam);
		assertTrue(ProTeamService.getSingleTeamAndRoster(proTeam.teamId) != null);
	}

	@Test
	public void update_team_test() {
		proTeam.mascot = "Monkey";
		assertTrue(ProTeamService.getSingleTeamAndRoster(proTeam.teamId).mascot != "Monkey");
		ProTeamService.updateTeam(proTeam.teamId, proTeam);
		assertTrue(ProTeamService.getSingleTeamAndRoster(proTeam.teamId).mascot == "Monkey");
	}

	@Test
	public void delete_team_test() {
		assertTrue(ProTeamService.getSingleTeamAndRoster(proTeam.teamId) != null);
		ProTeamService.deleteTeam(proTeam.teamId);
		assertTrue(ProTeamService.getSingleTeamAndRoster(proTeam.teamId) == null);
	}
}
