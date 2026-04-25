import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import main.java.entity.ProTeamEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import main.java.ProTeamRosterApplication;
import main.java.entity.ProMascotEntity;
import main.java.service.ProMascotService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProTeamRosterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ProMascotServiceTest {

	@Before
	public void setUp() {
		// Clear existing data
		try {
			ProMascotService.getProMascot(1L);
			ProMascotService.deleteProMascot(1L);
		} catch (Exception e) {
			// Mascot doesn't exist, continue
		}
		try {
			ProMascotService.getProMascot(2L);
			ProMascotService.deleteProMascot(2L);
		} catch (Exception e) {
			// Mascot doesn't exist, continue
		}
		try {
			ProMascotService.getProMascot(3L);
			ProMascotService.deleteProMascot(3L);
		} catch (Exception e) {
			// Mascot doesn't exist, continue
		}

		// Create test data
		ProMascotEntity mascot1 = new ProMascotEntity();
		mascot1.mascotId = 1L;
		mascot1.name = "Giant Bee";
		mascot1.description = "A large buzzing insect";
		mascot1.costume = "Yellow and black stripes";
		mascot1.team = new ProTeamEntity();
		ProMascotService.createProMascot(mascot1);

		ProMascotEntity mascot2 = new ProMascotEntity();
		mascot2.mascotId = 2L;
		mascot2.name = "Mountain";
		mascot2.description = "A majestic peak";
		mascot2.costume = "Blue and white peaks";
		mascot2.team = new ProTeamEntity();
		ProMascotService.createProMascot(mascot2);

		ProMascotEntity mascot3 = new ProMascotEntity();
		mascot3.mascotId = 3L;
		mascot3.name = "A Stick";
		mascot3.description = "Just a simple stick";
		mascot3.costume = "Brown wood";
		mascot3.team = new ProTeamEntity();
		ProMascotService.createProMascot(mascot3);
	}

	@Test
	public void get_pro_mascot_test() {
		ProMascotEntity mascot = ProMascotService.getProMascot(1L);
		assertNotNull(mascot);
		assertEquals("Giant Bee", mascot.name);
	}

	@Test
	public void get_mascots_by_team_test() {
		java.util.List<ProMascotEntity> mascots = ProMascotService.getMascotsByTeam("Apps");
		assertEquals(1, mascots.size());
		assertEquals("Mountain", mascots.get(0).name);
	}

	@Test
	public void create_pro_mascot_test() {
		ProMascotEntity mascot4 = new ProMascotEntity();
		mascot4.mascotId = 4L;
		mascot4.name = "Dragon";
		mascot4.description = "A fierce mythical creature";
		mascot4.costume = "Green scales and wings";
		mascot4.team = new ProTeamEntity();

		assertNull(ProMascotService.getProMascot(4L));
		ProMascotService.createProMascot(mascot4);
		assertNotNull(ProMascotService.getProMascot(4L));
	}

	@Test
	public void update_pro_mascot_test() {
		ProMascotEntity mascot2 = new ProMascotEntity();
		mascot2.mascotId = 2L;
		mascot2.description = "Updated description";

		ProMascotEntity beforeUpdate = ProMascotService.getProMascot(2L);
		assertNotNull(beforeUpdate);
		assertEquals("A majestic peak", beforeUpdate.description);

		ProMascotService.updateProMascot(2L, mascot2);

		ProMascotEntity afterUpdate = ProMascotService.getProMascot(2L);
		assertNotNull(afterUpdate);
		assertEquals("Updated description", afterUpdate.description);
	}

	@Test
	public void delete_pro_mascot_test() {
		// First create a mascot to delete
		ProMascotEntity mascot4 = new ProMascotEntity();
		mascot4.mascotId = 4L;
		mascot4.name = "Test Mascot";
		mascot4.description = "Test description";
		mascot4.costume = "Test costume";
		mascot4.team = new ProTeamEntity();
		ProMascotService.createProMascot(mascot4);

		ProMascotEntity beforeDelete = ProMascotService.getProMascot(4L);
		assertNotNull(beforeDelete);

		ProMascotService.deleteProMascot(4L);

		assertNull(ProMascotService.getProMascot(4L));
	}
}
