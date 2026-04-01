package main.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProMascotEntity;
import main.java.repository.ProMascotRepo;

@Service
public class ProMascotService {

	private static ProMascotRepo proMascotRepo;

	@Autowired
	public ProMascotService(ProMascotRepo proMascotRepo) {
		ProMascotService.proMascotRepo = proMascotRepo;
	}

	public static ProMascotEntity getProMascot(long mascotId) {
		return proMascotRepo.getOneByMascotId(mascotId);
	}

	public static List<ProMascotEntity> getMascotsByTeam(String teamName) {
		return proMascotRepo.getMascotsByTeamName(teamName);
	}

	public static ProMascotEntity createProMascot(ProMascotEntity createMascotReq) {
		return proMascotRepo.save(createMascotReq);
	}

	public static ProMascotEntity updateProMascot(long mascotId, ProMascotEntity updateMascotReq) {
		return proMascotRepo.findById(mascotId).map(mascot -> {
			mascot.name = updateMascotReq.name;
			mascot.teamName = updateMascotReq.teamName;
			mascot.description = updateMascotReq.description;
			mascot.costume = updateMascotReq.costume;
			return proMascotRepo.save(mascot);
		}).orElseGet(() -> {
			return proMascotRepo.save(updateMascotReq);
		});
	}

	public static String deleteProMascot(long mascotId) {
		try {
			proMascotRepo.deleteMascotById(mascotId);
		} catch (Exception e) {
			return "Delete was unsuccessful with error: " + e.toString();
		}
		return "Delete was successful for mascot:" + mascotId;
	}

}

