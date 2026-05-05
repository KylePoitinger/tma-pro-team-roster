package main.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
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
	// Static reference to the actual port for access by other components
	private static int actualPort = 8080;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
		SpringApplication.run(ProTeamRosterApplication.class, args);
	}

	public static int getActualPort() {
		return actualPort;
	}

	private static final Logger LOG = LoggerFactory.getLogger(ProTeamRosterApplication.class);

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

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> {
			int port = 8080;
			if (!isPortAvailable(port)) {
				LOG.info("[Application] Port 8080 is already in use. Switching to a dynamic port.");
				port = 0;
			}
			actualPort = port; // Store port before server starts
			factory.setPort(port);
		};
	}

	private boolean isPortAvailable(int port) {
		try (ServerSocket socket = new ServerSocket(port)) {
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("[Application] Starting database seeding...");
		
		LOG.debug("Inserting arena data in DB.");
		String[] arenaNames = {"Madison Square Garden", "Staples Center", "United Center", "TD Garden", "Oracle Arena"};
		String[] cities = {"New York, NY", "Los Angeles, CA", "Chicago, IL", "Boston, MA", "Oakland, CA"};
		String[] addresses = {"4 Pennsylvania Plaza", "1111 S Figueroa St", "1901 W Madison St", "100 Legends Way", "7000 Coliseum Way"};

		List<ProArenaEntity> arenas = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			ProArenaEntity arena = new ProArenaEntity();
			arena.setArenaId(i);
			arena.setName(arenaNames[i - 1]);
			arena.setLocation(cities[i - 1]);
			arena.setCapacity(18000 + (i * 500));
			arena.setAddress(addresses[i - 1]);
			arena.setOpenedYear(1990 + (i * 2));
			arena.setSurface("Hardwood");
			arena.setAmenities("Concessions, VIP Suites, Pro Shop");
			arena.setCost(150000000.00 + (i * 20000000));
			arenas.add(proArenaRepo.save(arena));
		}

		LOG.debug("Inserting homeTeam data in DB.");
		List<ProTeamEntity> teams = new ArrayList<>();
		String[] teamNames = {"Knicks", "Lakers", "Bulls", "Celtics", "Warriors", "Nets", "Clippers", "Heat"};
		String[] teamCities = {"New York", "Los Angeles", "Chicago", "Boston", "San Francisco", "Brooklyn", "Los Angeles", "Miami"};
		String[] owners = {"James Dolan", "Jeanie Buss", "Jerry Reinsdorf", "Wyc Grousbeck", "Joe Lacob", "Joe Tsai", "Steve Ballmer", "Micky Arison"};

		for (int i = 1; i <= 8; i++) {
			ProTeamEntity team = new ProTeamEntity();
			team.setTeamId(i);
			team.setName(teamNames[i - 1]);
			team.setCity(teamCities[i - 1]);
			team.setMascot(team.getName() + " Mascot");
			team.setFoundedYear(1946 + (i * 5));
			team.setArena(arenas.get((i - 1) % 5));
			team.setChampionships(i % 4);
			team.setOwner(owners[i - 1]);
			team.setColors("Primary Color, Secondary Color");
			team.setWebsite("https://www.nba.com/" + team.getName().toLowerCase());
			teams.add(proTeamRepo.save(team));
		}

		LOG.debug("Inserting mascot data in DB.");
		String[] mascotNames = {"Spike", "Jack", "Benny", "Lucky", "Thunder", "BrooklyKnight", "Chuck", "Burnie"};
		String[] mascotSpecies = {"Reptile", "Dog", "Bull", "Leprechaun", "Superhero", "Knight", "Pelican", "Sun"};
		String[] performers = {"John Smith", "Mike Jones", "Chris Rock", "Dave Chappelle", "Bill Burr", "Kevin Hart", "Jerry Seinfeld", "Adam Sandler"};

		for (int i = 0; i < teams.size(); i++) {
			ProTeamEntity team = teams.get(i);
			ProMascotEntity mascot = new ProMascotEntity();
			mascot.setMascotId(i + 1);
			mascot.setName(mascotNames[i]);
			mascot.setSpecies(mascotSpecies[i]);
			mascot.setTeam(team);
			mascot.setDescription("The energetic and fan-favorite mascot of the " + team.getName());
			mascot.setCostume(team.getName() + " themed outfit");
			mascot.setHeight(185.0 + i);
			mascot.setWeight(80.0 + i);
			mascot.setPersonality("Highly Energetic");
			mascot.setFirstAppearance("199" + i + "-10-01");
			mascot.setPerformerName(performers[i]);
			proMascotRepo.save(mascot);
		}

		LOG.debug("Inserting player data in DB.");
		String[] firstNames = {"LeBron", "Kevin", "Stephen", "Giannis", "Luka", "Joel", "Nikola", "Jayson", "Ja", "Devin", "Zion"};
		String[] lastNames = {"James", "Durant", "Curry", "Antetokounmpo", "Doncic", "Embiid", "Jokic", "Tatum", "Morant", "Booker", "Williamson"};
		String[] positions = {"Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"};
		String[] colleges = {"Kentucky", "Duke", "Kansas", "UCLA", "North Carolina", "Michigan State", "Arizona", "Villanova", "Gonzaga", "Texas", "Virginia"};

		List<ProPlayerEntity> allPlayers = new ArrayList<>();
		for (int t = 0; t < teams.size(); t++) {
			ProTeamEntity team = teams.get(t);
			for (int p = 1; p <= 11; p++) {
				ProPlayerEntity player = new ProPlayerEntity();
				player.setPlayerId((long) t * 11 + p);
				player.setName(firstNames[(p - 1) % 11] + " " + lastNames[(t + p - 1) % 11]);
				player.setPosition(positions[(p - 1) % 5]);
				player.setTeam(team);
				player.setAge(19 + (p % 15));
				player.setHeight(180.0 + (p * 2));
				player.setWeight(180.0 + (p * 5));
				player.setCollege(colleges[(t + p) % 11]);
				player.setSalary(1000000.00 + (p * 500000));
				player.setJerseyNumber(p + 10);
				player.setNationality("International");
				player.setContractYears(1 + (p % 5));
				player.setInjuryStatus("Active");
				player.setStats("PPG: " + (10 + p) + ", RPG: " + (p / 2) + ", APG: " + (p / 3));
				player.setDebutDate("201" + (p % 10) + "-10-15");
				allPlayers.add(proPlayerRepo.save(player));
			}
		}

		LOG.debug("Inserting schedule data in DB.");
		for (int i = 0; i < teams.size(); i++) {
			ProTeamEntity homeTeam = teams.get(i);
			ProTeamEntity awayTeam = teams.get((i + 1) % teams.size());
			ProScheduleEntity schedule = new ProScheduleEntity();
			schedule.setScheduleId(i + 1);
			schedule.setHomeTeam(homeTeam);
			schedule.setAwayTeam(awayTeam);
			schedule.setArena(homeTeam.getArena());
			schedule.setScheduledDate("2026-05-" + (10 + i));
			schedule.setTicketPrice(50.00 + (i * 5));
			proScheduleRepo.save(schedule);
		}
		
		LOG.debug("[Application] Database seeding completed successfully.");
	}
}
