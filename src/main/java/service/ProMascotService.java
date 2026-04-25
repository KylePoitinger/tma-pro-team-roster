package main.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProMascotEntity;
import main.java.repository.ProMascotRepo;

@Service
public class ProMascotService {

	@Autowired
	private ProMascotRepo proMascotRepo;

	public ProMascotEntity getProMascot(long mascotId) {
		return proMascotRepo.getOneByMascotId(mascotId);
	}

	public List<ProMascotEntity> getMascotsByTeam(String teamName) {
		return proMascotRepo.getMascotsByTeamName(teamName);
	}

	public ProMascotEntity createProMascot(ProMascotEntity createMascotReq) {
		return proMascotRepo.save(createMascotReq);
	}

	public ProMascotEntity updateProMascot(long mascotId, ProMascotEntity updateMascotReq) {
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

	public String deleteProMascot(long mascotId) {
		try {
			proMascotRepo.deleteMascotById(mascotId);
		} catch (Exception e) {
			return "Delete was unsuccessful with error: " + e.toString();
		}
		return "Delete was successful for mascot:" + mascotId;
	}

}
