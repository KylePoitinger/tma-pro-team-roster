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

	public static ProPlayerEntity createProPlayer() {
		return null;
		// TODO Auto-generated method stub

	}

	public static ProPlayerEntity updateProPlayer() {
		return null;
		// TODO Auto-generated method stub

	}

	public static ProPlayerEntity deleteProPlayer() {
		return null;
		// TODO Auto-generated method stub

	}
}
