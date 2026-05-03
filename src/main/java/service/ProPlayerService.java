package main.java.service;

import java.util.Optional;

import main.java.config.KafkaConfig;
import main.java.dto.ProEvent;
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

	@Autowired
	private ProKafkaProducer proKafkaProducer;

	public ProPlayerEntity getProPlayer(long playerId) {
		return Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public ProPlayerEntity createProPlayer(ProPlayerEntity createPlayerReq) {
		ProPlayerEntity savedPlayer = proPlayerRepo.save(createPlayerReq);
		proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_CREATED", "PLAYER", savedPlayer));
		return savedPlayer;
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
			ProPlayerEntity updatedPlayer = proPlayerRepo.save(player);
			proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_UPDATED", "PLAYER", updatedPlayer));
			return updatedPlayer;
		}).orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public String deleteProPlayer(long playerId) {
		ProPlayerEntity player = Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));

		proPlayerRepo.delete(player);
		proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_DELETED", "PLAYER", player));
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
