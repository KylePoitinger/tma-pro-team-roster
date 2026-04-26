package main.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProPlayerEntity;
import main.java.entity.ProTeamEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProPlayerRepo;
import main.java.repository.ProTeamRepo;

@Service
public class ProPlayerService {

	@Autowired
	private ProPlayerRepo proPlayerRepo;

	@Autowired
	private ProTeamRepo proTeamRepo;

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

	public ProPlayerEntity tradePlayer(long playerId, long teamId) {
		ProPlayerEntity player = Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));

		ProTeamEntity team = Optional.ofNullable(proTeamRepo.getOneByTeamId(teamId))
				.orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

		player.team = team;
		return proPlayerRepo.save(player);
	}

}
