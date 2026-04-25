package main.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProPlayerEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProPlayerRepo;

@Service
public class ProPlayerService {

	@Autowired
	private ProPlayerRepo proPlayerRepo;

	public ProPlayerEntity getProPlayer(long playerId) {
		return Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public ProPlayerEntity createProPlayer(ProPlayerEntity createPlayerReq) {
		return proPlayerRepo.save(createPlayerReq);
	}

	public ProPlayerEntity updateProPlayer(long playerId, ProPlayerEntity updatePlayerReq) {
		return proPlayerRepo.findById(playerId).map(player -> {
			player.name = updatePlayerReq.name;
			player.position = updatePlayerReq.position;
			player.team = updatePlayerReq.team;
			player.age = updatePlayerReq.age;
			player.height = updatePlayerReq.height;
			player.weight = updatePlayerReq.weight;
			player.college = updatePlayerReq.college;
			player.salary = updatePlayerReq.salary;
			return proPlayerRepo.save(player);
		}).orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public String deleteProPlayer(long playerId) {
		ProPlayerEntity player = Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));

		proPlayerRepo.delete(player);
		return "Delete was successful for player:" + playerId;
	}

}
