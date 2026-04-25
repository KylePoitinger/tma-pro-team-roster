package main.java;

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
import main.java.entity.ProTeamEntity;
import main.java.repository.ProArenaRepo;
import main.java.repository.ProMascotRepo;
import main.java.repository.ProPlayerRepo;
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

	@Autowired
	public ProTeamRosterApplication(ProTeamRepo proTeamRepo, ProPlayerRepo proPlayerRepo, ProMascotRepo proMascotRepo,
			ProArenaRepo proArenaRepo) {
		this.proTeamRepo = proTeamRepo;
		this.proPlayerRepo = proPlayerRepo;
		this.proMascotRepo = proMascotRepo;
		this.proArenaRepo = proArenaRepo;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Inserting team data in DB.");
		ProTeamEntity team1 = new ProTeamEntity();
		team1.teamId = 1;
		team1.city = "Charlotte";
		team1.name = "Hornets";
		team1.mascot = "Bee";
		team1.foundedYear = 1988;
		team1.stadium = "Spectrum Center";
		team1.championships = 0;
		team1.owner = "Gabe Plotkin";
		team1.colors = "Teal, Purple, White";
		team1.website = "https://www.nba.com/hornets";
		ProTeamEntity team2 = new ProTeamEntity();
		team2.teamId = 2;
		team2.city = "Hickory";
		team2.name = "Switch";
		team2.mascot = "Stick";
		team2.foundedYear = 2000;
		team2.stadium = "Hickory Arena";
		team2.championships = 1;
		team2.owner = "Local Owners";
		team2.colors = "Green, Brown";
		team2.website = "https://switchteam.com";
		ProTeamEntity team3 = new ProTeamEntity();
		team3.teamId = 3;
		team3.city = "Asheville";
		team3.name = "Apps";
		team3.mascot = "Mountain";
		team3.foundedYear = 1995;
		team3.stadium = "Asheville Stadium";
		team3.championships = 2;
		team3.owner = "Mountain Corp";
		team3.colors = "Blue, White";
		team3.website = "https://apps.com";
		LOG.info("saving team data in DB.");
		proTeamRepo.save(team1);
		proTeamRepo.save(team2);
		proTeamRepo.save(team3);
		LOG.info("Inserting teams: {" + team1, team2, team3 + "}");
		LOG.info("Inserting player data in DB.");
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
		player1.jerseyNumber = 10;
		player1.nationality = "American";
		player1.contractYears = 3;
		player1.injuryStatus = "Healthy";
		player1.stats = "Goals: 5, Assists: 3";
		player1.debutDate = "2020-01-01";
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
		player2.jerseyNumber = 5;
		player2.nationality = "Canadian";
		player2.contractYears = 2;
		player2.injuryStatus = "Healthy";
		player2.stats = "Goals: 2, Assists: 7";
		player2.debutDate = "2021-03-15";
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
		player3.jerseyNumber = 8;
		player3.nationality = "American";
		player3.contractYears = 4;
		player3.injuryStatus = "Minor Injury";
		player3.stats = "Goals: 8, Assists: 4";
		player3.debutDate = "2019-09-10";
		proPlayerRepo.save(player1);
		proPlayerRepo.save(player2);
		proPlayerRepo.save(player3);
		LOG.info("Inserting players: {" + player1, player2, player3 + "}");
		LOG.info("Inserting mascot data in DB.");
		ProMascotEntity mascot1 = new ProMascotEntity();
		mascot1.mascotId = 1;
		mascot1.name = "Hornsby";
		mascot1.description = "A giant bee mascot";
		mascot1.costume = "Yellow and black striped";
		ProMascotEntity mascot2 = new ProMascotEntity();
		mascot2.mascotId = 2;
		mascot2.name = "Switchly";
		mascot2.description = "A stick mascot";
		mascot2.costume = "Brown wood texture";
		ProMascotEntity mascot3 = new ProMascotEntity();
		mascot3.mascotId = 3;
		mascot3.name = "Rocky";
		mascot3.description = "A mountain mascot";
		mascot3.costume = "Gray boulder suit";
		LOG.info("saving mascot data in DB.");
		proMascotRepo.save(mascot1);
		proMascotRepo.save(mascot2);
		proMascotRepo.save(mascot3);
		LOG.info("Inserting mascots: {" + mascot1, mascot2, mascot3 + "}");
		LOG.info("Inserting arena data in DB.");
		ProArenaEntity arena1 = new ProArenaEntity();
		arena1.arenaId = 1;
		arena1.name = "Spectrum Center";
		arena1.location = "Charlotte, NC";
		arena1.capacity = 19000;
		ProArenaEntity arena2 = new ProArenaEntity();
		arena2.arenaId = 2;
		arena2.name = "Hickory Metro Convention Center";
		arena2.location = "Hickory, NC";
		arena2.capacity = 5000;
		ProArenaEntity arena3 = new ProArenaEntity();
		arena3.arenaId = 3;
		arena3.name = "US Cellular Center";
		arena3.location = "Asheville, NC";
		arena3.capacity = 7000;
		LOG.info("saving arena data in DB.");
		proArenaRepo.save(arena1);
		proArenaRepo.save(arena2);
		proArenaRepo.save(arena3);
		LOG.info("Inserting arenas: {" + arena1, arena2, arena3 + "}");
	}

}
