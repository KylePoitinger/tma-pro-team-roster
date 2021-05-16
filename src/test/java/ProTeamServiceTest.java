package test.java;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import main.java.entity.ProTeamEntity;
import main.java.repository.ProTeamRepo;
import main.java.service.ProTeamService;


public class ProTeamServiceTest {

	@Autowired
	private ProTeamRepo proTeamRepo;

	@Autowired
	private ProTeamService proTeamService;	

	ProTeamEntity proTeam = new ProTeamEntity();
	
	@Before
	public void setUp() {
		this.proTeamService = new ProTeamService(proTeamRepo); 
		proTeam.teamId = 10;
		proTeam.city = "CharlotteLight";
		proTeam.name = "HornetsTail";
		proTeam.mascot = "Giant Bee Thing";
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
