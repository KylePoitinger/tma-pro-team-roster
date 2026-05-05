package main.java.service;

import java.util.Map;
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

	@Autowired(required = false)
	private ProKafkaProducer proKafkaProducer;

	public ProPlayerEntity getProPlayer(long playerId) {
		return Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public ProPlayerEntity createProPlayer(ProPlayerEntity createPlayerReq) {
		ProPlayerEntity savedPlayer = proPlayerRepo.save(createPlayerReq);
		if (proKafkaProducer != null) {
			proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_CREATED", "PLAYER", savedPlayer));
		}
		return savedPlayer;
	}

	public ProPlayerEntity updateProPlayer(long playerId, ProPlayerEntity updatePlayerReq) {
		return proPlayerRepo.findById(playerId).map(player -> {
			player.setName(updatePlayerReq.getName());
			player.setPosition(updatePlayerReq.getPosition());
			player.setTeam(updatePlayerReq.getTeam());
			player.setAge(updatePlayerReq.getAge());
			player.setHeight(updatePlayerReq.getHeight());
			player.setWeight(updatePlayerReq.getWeight());
			player.setCollege(updatePlayerReq.getCollege());
			player.setSalary(updatePlayerReq.getSalary());
			ProPlayerEntity updatedPlayer = proPlayerRepo.save(player);
			if (proKafkaProducer != null) {
				proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_UPDATED", "PLAYER", updatedPlayer));
			}
			return updatedPlayer;
		}).orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));
	}

	public String deleteProPlayer(long playerId) {
		ProPlayerEntity player = Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));

		proPlayerRepo.delete(player);
		if (proKafkaProducer != null) {
			proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_DELETED", "PLAYER", player));
		}
		return "Delete was successful for player:" + playerId;
	}

	public ProPlayerEntity tradePlayer(long playerId, long teamId) {
		ProPlayerEntity player = Optional.ofNullable(proPlayerRepo.getOneByPlayerId(playerId))
				.orElseThrow(() -> new ResourceNotFoundException("Player not found for this id :: " + playerId));

		ProTeamEntity team = Optional.ofNullable(proTeamRepo.getOneByTeamId(teamId))
				.orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

		player.setTeam(team);
		ProPlayerEntity updatedPlayer = proPlayerRepo.save(player);
		if (proKafkaProducer != null) {
			proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC, ProEvent.create("PLAYER_TRADED", "PLAYER", updatedPlayer));
		}
		return updatedPlayer;
	}

	public void initiateTrade(long playerId, long teamId) {
		if (proKafkaProducer != null) {
			proKafkaProducer.sendEvent(KafkaConfig.PLAYER_TOPIC,
					ProEvent.create("PLAYER_TRADE_REQUESTED", "PLAYER", Map.of("playerId", playerId, "teamId", teamId)));
		} else {
			tradePlayer(playerId, teamId);
		}
	}

}
