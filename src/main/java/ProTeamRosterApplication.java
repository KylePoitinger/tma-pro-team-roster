package main.java;

import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.java.entity.ProPlayerEntity;
import main.java.entity.ProTeamEntity;
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

	@Autowired
	public ProTeamRosterApplication(ProTeamRepo proTeamRepo, ProPlayerRepo proPlayerRepo) {
		this.proTeamRepo = proTeamRepo;
		this.proPlayerRepo = proPlayerRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		LOG.info("Inserting team data in DB.");
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
		LOG.info("saving team data in DB.");
		proTeamRepo.save(team1);
		proTeamRepo.save(team2);
		proTeamRepo.save(team3);
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
		proPlayerRepo.save(player1);
		proPlayerRepo.save(player2);
		proPlayerRepo.save(player3);
	}

}
