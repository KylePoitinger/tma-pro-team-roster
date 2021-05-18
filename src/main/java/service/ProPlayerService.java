package main.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProPlayerEntity;
import main.java.repository.ProPlayerRepo;

@Service
public class ProPlayerService {

	private static ProPlayerRepo proPlayerRepo;

	@Autowired
	public ProPlayerService(ProPlayerRepo proPlayerRepo) {
		ProPlayerService.proPlayerRepo = proPlayerRepo;
	}

	public static ProPlayerEntity getProPlayer(long playerId) {
		return proPlayerRepo.getOneByPlayerId(playerId);
	}

	public static ProPlayerEntity createProPlayer(ProPlayerEntity createPlayerReq) {
		return proPlayerRepo.save(createPlayerReq);
	}

	public static ProPlayerEntity updateProPlayer(long playerId, ProPlayerEntity updatePlayerReq) {
		return proPlayerRepo.findById(playerId).map(player -> {
			player.name = updatePlayerReq.name;
			player.position = updatePlayerReq.position;
			player.teamName = updatePlayerReq.teamName;
			player.age = updatePlayerReq.age;
			player.height = updatePlayerReq.height;
			player.weight = updatePlayerReq.weight;
			player.college = updatePlayerReq.college;
			player.salary = updatePlayerReq.salary;
			return proPlayerRepo.save(player);
		}).orElseGet(() -> {
			return proPlayerRepo.save(updatePlayerReq);
		});
	}

	public static String deleteProPlayer(long playerId) {
		try {
			proPlayerRepo.deletePlayerById(playerId);
		} catch (Exception e) {
			return "Delete was unsuccessful with error: " + e.toString();
		}
		return "Delete was successful for team:" + playerId;
	}

}
