package test.java;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.java.entity.ProPlayerEntity;
import main.java.repository.ProPlayerRepo;
import main.java.service.ProPlayerService;

public class ProPlayerServiceTest {

	private ProPlayerService proPlayerService;
	private ProPlayerRepo proPlayerRepo;

	@Mock
	private ProPlayerEntity proPlayer;

	@Before
	public void setupMock() {
		proPlayerService = new ProPlayerService(proPlayerRepo);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockCreation() {
		assertNotNull(proPlayer);
	}

	@Test
	public void get_pro_player_test() {
		assertTrue(ProPlayerService.getProPlayer(1).name == "Kyle");
	}

	@Test
	public void create_pro_player_test() {
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId) == null);
		ProPlayerService.createProPlayer(proPlayer);
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId) != null);
	}

	@Test
	public void update_pro_player_test() {
		proPlayer.salary = 999999;
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId).salary != 999999);
		ProPlayerService.updateProPlayer(proPlayer.playerId, proPlayer);
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId).salary == 999999);
	}

	@Test
	public void delete_pro_player_test() {
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId) != null);
		ProPlayerService.deleteProPlayer(proPlayer.playerId);
		assertTrue(ProPlayerService.getProPlayer(proPlayer.playerId) == null);
	}

}
