package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import main.java.entity.ProArenaEntity;
import main.java.entity.ProMascotEntity;
import main.java.entity.ProPlayerEntity;
import main.java.entity.ProScheduleEntity;
import main.java.entity.ProTeamEntity;
import main.java.repository.ProArenaRepo;
import main.java.repository.ProMascotRepo;
import main.java.repository.ProPlayerRepo;
import main.java.repository.ProScheduleRepo;
import main.java.repository.ProTeamRepo;

@SpringBootApplication
public class ProTeamRosterApplication implements CommandLineRunner {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
		SpringApplication.run(ProTeamRosterApplication.class, args);
	}

	private Logger LOG = LoggerFactory.getLogger("Application");

	private final ProTeamRepo proTeamRepo;

	private final ProPlayerRepo proPlayerRepo;

	private final ProMascotRepo proMascotRepo;

	private final ProArenaRepo proArenaRepo;

	private final ProScheduleRepo proScheduleRepo;

	@Autowired
	public ProTeamRosterApplication(ProTeamRepo proTeamRepo, ProPlayerRepo proPlayerRepo, ProMascotRepo proMascotRepo,
			ProArenaRepo proArenaRepo, ProScheduleRepo proScheduleRepo) {
		this.proTeamRepo = proTeamRepo;
		this.proPlayerRepo = proPlayerRepo;
		this.proMascotRepo = proMascotRepo;
		this.proArenaRepo = proArenaRepo;
		this.proScheduleRepo = proScheduleRepo;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Inserting arena data in DB.");
		String[] arenaNames = {"Madison Square Garden", "Staples Center", "United Center", "TD Garden", "Oracle Arena"};
		String[] cities = {"New York, NY", "Los Angeles, CA", "Chicago, IL", "Boston, MA", "Oakland, CA"};
		String[] addresses = {"4 Pennsylvania Plaza", "1111 S Figueroa St", "1901 W Madison St", "100 Legends Way", "7000 Coliseum Way"};

		List<ProArenaEntity> arenas = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			ProArenaEntity arena = new ProArenaEntity();
			arena.arenaId = i;
			arena.name = arenaNames[i - 1];
			arena.location = cities[i - 1];
			arena.capacity = 18000 + (i * 500);
			arena.address = addresses[i - 1];
			arena.openedYear = 1990 + (i * 2);
			arena.surface = "Hardwood";
			arena.amenities = "Concessions, VIP Suites, Pro Shop";
			arena.cost = 150000000.00 + (i * 20000000);
			arenas.add(proArenaRepo.save(arena));
		}

		LOG.info("Inserting team data in DB.");
		List<ProTeamEntity> teams = new ArrayList<>();
		String[] teamNames = {"Knicks", "Lakers", "Bulls", "Celtics", "Warriors", "Nets", "Clippers", "Heat"};
		String[] teamCities = {"New York", "Los Angeles", "Chicago", "Boston", "San Francisco", "Brooklyn", "Los Angeles", "Miami"};
		String[] owners = {"James Dolan", "Jeanie Buss", "Jerry Reinsdorf", "Wyc Grousbeck", "Joe Lacob", "Joe Tsai", "Steve Ballmer", "Micky Arison"};

		for (int i = 1; i <= 8; i++) {
			ProTeamEntity team = new ProTeamEntity();
			team.teamId = i;
			team.name = teamNames[i - 1];
			team.city = teamCities[i - 1];
			team.mascot = team.name + " Mascot";
			team.foundedYear = 1946 + (i * 5);
			team.arena = arenas.get((i - 1) % 5);
			team.championships = i % 4;
			team.owner = owners[i - 1];
			team.colors = "Primary Color, Secondary Color";
			team.website = "https://www.nba.com/" + team.name.toLowerCase();
			teams.add(proTeamRepo.save(team));
		}

		LOG.info("Inserting mascot data in DB.");
		String[] mascotNames = {"Spike", "Jack", "Benny", "Lucky", "Thunder", "BrooklyKnight", "Chuck", "Burnie"};
		String[] performers = {"John Smith", "Mike Jones", "Chris Rock", "Dave Chappelle", "Bill Burr", "Kevin Hart", "Jerry Seinfeld", "Adam Sandler"};

		for (int i = 0; i < teams.size(); i++) {
			ProTeamEntity team = teams.get(i);
			ProMascotEntity mascot = new ProMascotEntity();
			mascot.name = mascotNames[i];
			mascot.team = team;
			mascot.description = "The energetic and fan-favorite mascot of the " + team.name;
			mascot.costume = team.name + " themed outfit";
			mascot.height = 185.0 + i;
			mascot.weight = 80.0 + i;
			mascot.personality = "Highly Energetic";
			mascot.firstAppearance = "199" + i + "-10-01";
			mascot.performerName = performers[i];
			proMascotRepo.save(mascot);
		}

		LOG.info("Inserting player data in DB.");
		String[] firstNames = {"LeBron", "Kevin", "Stephen", "Giannis", "Luka", "Joel", "Nikola", "Jayson", "Ja", "Devin", "Zion"};
		String[] lastNames = {"James", "Durant", "Curry", "Antetokounmpo", "Doncic", "Embiid", "Jokic", "Tatum", "Morant", "Booker", "Williamson"};
		String[] positions = {"Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"};
		String[] colleges = {"Kentucky", "Duke", "Kansas", "UCLA", "North Carolina", "Michigan State", "Arizona", "Villanova", "Gonzaga", "Texas", "Virginia"};

		List<ProPlayerEntity> allPlayers = new ArrayList<>();
		for (int t = 0; t < teams.size(); t++) {
			ProTeamEntity team = teams.get(t);
			for (int p = 1; p <= 11; p++) {
				ProPlayerEntity player = new ProPlayerEntity();
				player.playerId = (long) t * 11 + p;
				player.name = firstNames[(p - 1) % 11] + " " + lastNames[(t + p - 1) % 11];
				player.position = positions[(p - 1) % 5];
				player.team = team;
				player.age = 19 + (p % 15);
				player.height = 180.0 + (p * 2);
				player.weight = 180.0 + (p * 5);
				player.college = colleges[(t + p) % 11];
				player.salary = 1000000.00 + (p * 500000);
				player.jerseyNumber = p + 10;
				player.nationality = "International";
				player.contractYears = 1 + (p % 5);
				player.injuryStatus = "Active";
				player.stats = "PPG: " + (10 + p) + ", RPG: " + (p / 2) + ", APG: " + (p / 3);
				player.debutDate = "201" + (p % 10) + "-10-15";
				allPlayers.add(proPlayerRepo.save(player));
			}
		}

		LOG.info("Inserting schedule data in DB.");
		for (int i = 0; i < teams.size(); i++) {
			ProTeamEntity team = teams.get(i);
			ProScheduleEntity schedule = new ProScheduleEntity();
			schedule.team = team;
			schedule.arena = team.arena;
			schedule.scheduledDate = "2026-05-" + (10 + i);
			schedule.ticketPrice = 50.00 + (i * 5);
			proScheduleRepo.save(schedule);
		}
		
		LOG.info("Startup data population complete.");
	}
}
