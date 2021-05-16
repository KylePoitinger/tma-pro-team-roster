package test.java;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.entity.ProTeamEntity;
import main.java.service.ProTeamService;

public class ProTeamServiceTest {

	@Before
	public void setUp() {
		ProTeamEntity team1 = new ProTeamEntity();
		team1.teamId = 1;
		team1.city = "Charlotte";
		team1.name = "Hornets";
		team1.mascot = "Giant Bee";
		ProTeamEntity team2 = new ProTeamEntity();
		team2.teamId = 2;
		team2.city = "Hickory";
		team2.name = "Switch";
		team2.mascot = "A Stick";
		ProTeamEntity team3 = new ProTeamEntity();
		team3.teamId = 3;
		team3.city = "Asheville";
		team3.name = "Apps";
		team3.mascot = "Mountain";
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
		ProTeamEntity team4 = new ProTeamEntity();
		team4.teamId = 14;
		team4.city = "Morganton";
		team4.name = "Shadows";
		team4.mascot = "Dirt Roads";
		ProTeamService.createTeam(team4);
		assertTrue(ProTeamService.getSingleTeamAndRoster(team4.teamId) != null);
	}

	@Test
	public void update_team_test() {
		ProTeamEntity team1 = new ProTeamEntity();
		team1.teamId = 1;
		team1.city = "Space";
		assertTrue(ProTeamService.getSingleTeamAndRoster(team1.teamId).city != "Space");
		ProTeamService.updateTeam(team1.teamId, team1);
		assertTrue(ProTeamService.getSingleTeamAndRoster(team1.teamId).city == "Space");
	}

	@Test
	public void delete_team_test() {
		assertTrue(ProTeamService.getSingleTeamAndRoster(1) != null);
		ProTeamService.deleteTeam(1);
		assertTrue(ProTeamService.getSingleTeamAndRoster(1) == null);
	}
}
