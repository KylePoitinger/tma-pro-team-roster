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
		List<ProArenaEntity> arenas = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			ProArenaEntity arena = new ProArenaEntity();
			arena.arenaId = i;
			arena.name = "Arena " + i;
			arena.location = "City " + i + ", NC";
			arena.capacity = 5000 + (i * 1000);
			arena.address = i + "00 Arena Way, City " + i + ", NC";
			arena.openedYear = 2000 + i;
			arena.surface = "Hardwood";
			arena.amenities = "Concessions, VIP Lounge";
			arena.cost = 50000000.00 + (i * 10000000);
			arenas.add(proArenaRepo.save(arena));
		}

		LOG.info("Inserting team data in DB.");
		List<ProTeamEntity> teams = new ArrayList<>();
		String[] teamNames = {"Hornets", "Switch", "Apps", "Wolves", "Eagles", "Sharks", "Titans", "Lions"};
		for (int i = 1; i <= 8; i++) {
			ProTeamEntity team = new ProTeamEntity();
			team.teamId = i;
			team.name = teamNames[i-1];
			team.city = "City " + ((i-1) % 5 + 1);
			team.mascot = team.name + " Mascot";
			team.foundedYear = 1980 + i;
			team.arena = arenas.get((i-1) % 5);
			team.championships = i % 3;
			team.owner = "Owner " + i;
			team.colors = "Color A, Color B";
			team.website = "https://www." + team.name.toLowerCase() + ".com";
			teams.add(proTeamRepo.save(team));
		}

		LOG.info("Inserting mascot data in DB.");
		for (int i = 0; i < teams.size(); i++) {
			ProTeamEntity team = teams.get(i);
			ProMascotEntity mascot = new ProMascotEntity();
			mascot.name = team.mascot;
			mascot.team = team;
			mascot.description = "The official mascot of " + team.name;
			mascot.costume = team.name + " suit";
			mascot.height = 180.0 + i;
			mascot.weight = 75.0 + i;
			mascot.personality = "Energetic";
			mascot.firstAppearance = "2020-01-01";
			mascot.performerName = "Performer " + (i + 1);
			proMascotRepo.save(mascot);
		}

		LOG.info("Inserting player data in DB.");
		List<ProPlayerEntity> allPlayers = new ArrayList<>();
		for (int t = 0; t < teams.size(); t++) {
			ProTeamEntity team = teams.get(t);
			for (int p = 1; p <= 11; p++) {
				ProPlayerEntity player = new ProPlayerEntity();
				player.playerId = (long) t * 11 + p;
				player.name = "Player " + player.playerId;
				player.position = "Position " + (p % 5 + 1);
				player.team = team;
				player.age = 20 + (p % 15);
				player.height = 170.0 + p;
				player.weight = 150.0 + p;
				player.college = "University " + ((t + p) % 10 + 1);
				player.salary = 50000.00 + (p * 10000);
				player.jerseyNumber = p;
				player.nationality = "American";
				player.contractYears = 1 + (p % 5);
				player.injuryStatus = "Healthy";
				player.stats = "G: " + p + ", A: " + (p/2);
				player.debutDate = "2022-10-10";
				allPlayers.add(proPlayerRepo.save(player));
			}
		}

		LOG.info("Inserting schedule data in DB.");
		for (int i = 0; i < 10; i++) {
			ProScheduleEntity schedule = new ProScheduleEntity();
			ProPlayerEntity randomPlayer = allPlayers.get(i * 5 % allPlayers.size());
			schedule.player = randomPlayer;
			schedule.arena = randomPlayer.team.arena;
			schedule.scheduledDate = "2026-05-" + (10 + i);
			schedule.ticketPrice = 50.00 + (i * 5);
			proScheduleRepo.save(schedule);
		}
		
		LOG.info("Startup data population complete.");
	}
}
