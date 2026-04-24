package io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import main.java.ProTeamRosterApplication;
import main.java.entity.ProTeamEntity;
import main.java.service.ProTeamService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProTeamRosterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ProTeamServiceTest {

	@Before
	public void setUp() {
		// Clear existing data
		ProTeamService.getTeams().forEach(team -> ProTeamService.deleteTeam(team.teamId));

		// Create test data
		ProTeamEntity team1 = new ProTeamEntity();
		team1.teamId = 1L;
		team1.city = "Charlotte";
		team1.name = "Hornets";
		team1.mascot = "Giant Bee";
		ProTeamService.createTeam(team1);

		ProTeamEntity team2 = new ProTeamEntity();
		team2.teamId = 2L;
		team2.city = "Hickory";
		team2.name = "Switch";
		team2.mascot = "A Stick";
		ProTeamService.createTeam(team2);

		ProTeamEntity team3 = new ProTeamEntity();
		team3.teamId = 3L;
		team3.city = "Asheville";
		team3.name = "Apps";
		team3.mascot = "Mountain";
		ProTeamService.createTeam(team3);
	}

	@Test
	public void get_all_teams_test() {
		assertEquals(3, ProTeamService.getTeams().size());
	}

	@Test
	public void get_single_team_test() {
		ProTeamEntity team = ProTeamService.getSingleTeamAndRoster(1L);
		assertNotNull(team);
		assertEquals("Hornets", team.name);
	}

	@Test
	public void get_teams_by_field_lookup_test() {
		java.util.List<ProTeamEntity> teams = ProTeamService.getTeamsByFieldLookup(null, "Hickory", null);
		assertEquals(1, teams.size());
		assertEquals("Switch", teams.get(0).name);
	}

	@Test
	public void create_team_test() {
		ProTeamEntity team4 = new ProTeamEntity();
		team4.teamId = 14L;
		team4.city = "Morganton";
		team4.name = "Shadows";
		team4.mascot = "Dirt Roads";
		ProTeamService.createTeam(team4);

		ProTeamEntity retrieved = ProTeamService.getSingleTeamAndRoster(14L);
		assertNotNull(retrieved);
		assertEquals("Shadows", retrieved.name);
	}

	@Test
	public void update_team_test() {
		ProTeamEntity team1 = new ProTeamEntity();
		team1.teamId = 1L;
		team1.city = "Space";
		team1.name = "Hornets";
		team1.mascot = "Giant Bee";

		ProTeamEntity beforeUpdate = ProTeamService.getSingleTeamAndRoster(1L);
		assertNotNull(beforeUpdate);
		assertEquals("Charlotte", beforeUpdate.city);

		ProTeamService.updateTeam(1L, team1);

		ProTeamEntity afterUpdate = ProTeamService.getSingleTeamAndRoster(1L);
		assertNotNull(afterUpdate);
		assertEquals("Space", afterUpdate.city);
	}

	@Test
	public void delete_team_test() {
		ProTeamEntity beforeDelete = ProTeamService.getSingleTeamAndRoster(1L);
		assertNotNull(beforeDelete);

		ProTeamService.deleteTeam(1L);

		ProTeamEntity afterDelete = ProTeamService.getSingleTeamAndRoster(1L);
		assertNull(afterDelete);
	}
}

