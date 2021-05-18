package test.java;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.entity.ProPlayerEntity;
import main.java.service.ProPlayerService;

public class ProPlayerServiceTest {

	@Before
	public void setUp() {
		ProPlayerEntity player1 = new ProPlayerEntity();
		player1.playerId = 1;
		player1.name = "Kyle";
		player1.position = "Forward";
		player1.teamName = "Hornets";
		player1.age = 24;
		player1.height = 175.26;
		player1.weight = 176.4;
		player1.college = "UNCC";
		player1.salary = 100000.00;
		ProPlayerEntity player2 = new ProPlayerEntity();
		player2.playerId = 2;
		player2.name = "Max";
		player2.position = "Defender";
		player2.teamName = "Apps";
		player2.age = 24;
		player2.height = 173.26;
		player2.weight = 187.4;
		player2.college = "APP";
		player2.salary = 100540.00;
		ProPlayerEntity player3 = new ProPlayerEntity();
		player3.playerId = 3;
		player3.name = "Kat";
		player3.position = "Midfield";
		player3.teamName = "Switch";
		player3.age = 25;
		player3.height = 173.26;
		player3.weight = 187.4;
		player3.college = "ASHE";
		player3.salary = 120547.00;
	}

	@Test
	public void get_pro_player_test() {
		assertTrue(ProPlayerService.getProPlayer(1).name == "Kyle");
	}

	@Test
	public void create_pro_player_test() {
		ProPlayerEntity player4 = new ProPlayerEntity();
		player4.playerId = 4;
		player4.name = "Rush";
		player4.position = "Goalie";
		player4.teamName = "Lilo";
		player4.age = 85;
		player4.height = 73.26;
		player4.weight = 87.4;
		player4.college = "ASHE";
		player4.salary = 120547.00;
		assertTrue(ProPlayerService.getProPlayer(4) == null);
		ProPlayerService.createProPlayer(player4);
		assertTrue(ProPlayerService.getProPlayer(4) != null);
	}

	@Test
	public void update_pro_player_test() {
		ProPlayerEntity player2 = new ProPlayerEntity();
		player2.playerId = 2;
		player2.salary = 1;
		assertTrue(ProPlayerService.getProPlayer(player2.playerId).salary != 999999);
		ProPlayerService.updateProPlayer(player2.playerId, player2);
		assertTrue(ProPlayerService.getProPlayer(player2.playerId).salary == 1);
	}

	@Test
	public void delete_pro_player_test() {
		ProPlayerEntity player4 = new ProPlayerEntity();
		player4.playerId = 4;
		assertTrue(ProPlayerService.getProPlayer(player4.playerId) != null);
		ProPlayerService.deleteProPlayer(player4.playerId);
		assertTrue(ProPlayerService.getProPlayer(player4.playerId) == null);
	}

}
