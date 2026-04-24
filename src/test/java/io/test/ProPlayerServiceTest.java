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
import main.java.entity.ProPlayerEntity;
import main.java.service.ProPlayerService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProTeamRosterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ProPlayerServiceTest {

	@Before
	public void setUp() {
		// Clear existing data
		try {
			ProPlayerService.getProPlayer(1L);
			ProPlayerService.deleteProPlayer(1L);
		} catch (Exception e) {
			// Player doesn't exist, continue
		}
		try {
			ProPlayerService.getProPlayer(2L);
			ProPlayerService.deleteProPlayer(2L);
		} catch (Exception e) {
			// Player doesn't exist, continue
		}
		try {
			ProPlayerService.getProPlayer(3L);
			ProPlayerService.deleteProPlayer(3L);
		} catch (Exception e) {
			// Player doesn't exist, continue
		}

		// Create test data
		ProPlayerEntity player1 = new ProPlayerEntity();
		player1.playerId = 1L;
		player1.name = "Kyle";
		player1.position = "Forward";
		player1.teamName = "Hornets";
		player1.age = 24;
		player1.height = 175.26;
		player1.weight = 176.4;
		player1.college = "UNCC";
		player1.salary = 100000.00;
		ProPlayerService.createProPlayer(player1);

		ProPlayerEntity player2 = new ProPlayerEntity();
		player2.playerId = 2L;
		player2.name = "Max";
		player2.position = "Defender";
		player2.teamName = "Apps";
		player2.age = 24;
		player2.height = 173.26;
		player2.weight = 187.4;
		player2.college = "APP";
		player2.salary = 100540.00;
		ProPlayerService.createProPlayer(player2);

		ProPlayerEntity player3 = new ProPlayerEntity();
		player3.playerId = 3L;
		player3.name = "Kat";
		player3.position = "Midfield";
		player3.teamName = "Switch";
		player3.age = 25;
		player3.height = 173.26;
		player3.weight = 187.4;
		player3.college = "ASHE";
		player3.salary = 120547.00;
		ProPlayerService.createProPlayer(player3);
	}

	@Test
	public void get_pro_player_test() {
		ProPlayerEntity player = ProPlayerService.getProPlayer(1L);
		assertNotNull(player);
		assertEquals("Kyle", player.name);
	}

	@Test
	public void create_pro_player_test() {
		ProPlayerEntity player4 = new ProPlayerEntity();
		player4.playerId = 4L;
		player4.name = "Rush";
		player4.position = "Goalie";
		player4.teamName = "Lilo";
		player4.age = 85;
		player4.height = 73.26;
		player4.weight = 87.4;
		player4.college = "ASHE";
		player4.salary = 120547.00;

		assertNull(ProPlayerService.getProPlayer(4L));
		ProPlayerService.createProPlayer(player4);
		assertNotNull(ProPlayerService.getProPlayer(4L));
	}

	@Test
	public void update_pro_player_test() {
		ProPlayerEntity player2 = new ProPlayerEntity();
		player2.playerId = 2L;
		player2.salary = 1.0;

		ProPlayerEntity beforeUpdate = ProPlayerService.getProPlayer(2L);
		assertNotNull(beforeUpdate);
		assertEquals(100540.00, beforeUpdate.salary, 0.01);

		ProPlayerService.updateProPlayer(2L, player2);

		ProPlayerEntity afterUpdate = ProPlayerService.getProPlayer(2L);
		assertNotNull(afterUpdate);
		assertEquals(1.0, afterUpdate.salary, 0.01);
	}

	@Test
	public void delete_pro_player_test() {
		// First create a player to delete
		ProPlayerEntity player4 = new ProPlayerEntity();
		player4.playerId = 4L;
		player4.name = "Test Player";
		player4.position = "Forward";
		player4.teamName = "Test Team";
		player4.age = 25;
		player4.height = 180.0;
		player4.weight = 175.0;
		player4.college = "TEST";
		player4.salary = 50000.00;
		ProPlayerService.createProPlayer(player4);

		ProPlayerEntity beforeDelete = ProPlayerService.getProPlayer(4L);
		assertNotNull(beforeDelete);

		ProPlayerService.deleteProPlayer(4L);

		assertNull(ProPlayerService.getProPlayer(4L));
	}
}

