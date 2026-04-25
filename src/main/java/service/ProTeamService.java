package main.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProTeamEntity;
import main.java.repository.ProTeamRepo;

@Service
public class ProTeamService {

	@Autowired
	private ProTeamRepo proTeamRepo;

	public List<ProTeamEntity> getTeams() {
		return proTeamRepo.findAll();
	}

	public ProTeamEntity getSingleTeamAndRoster(long teamId) {
		return proTeamRepo.getOneByTeamId(teamId);

	}

	public List<ProTeamEntity> getTeamsByFieldLookup(String name, String city, String mascot) {
		if (name != null) {
			return proTeamRepo.getTeamsByName(name);
		} else if (city != null) {
			return proTeamRepo.getTeamsByCity(city);
		} else if (mascot != null) {
			return proTeamRepo.getTeamsByMascot(mascot);
		} else
			return null;
	}

	public ProTeamEntity createTeam(ProTeamEntity createTeamReq) {
		return proTeamRepo.save(createTeamReq);
	}

	public ProTeamEntity updateTeam(long teamId, ProTeamEntity updateTeamReq) {

		return proTeamRepo.findById(teamId).map(team -> {
			team.city = updateTeamReq.city;
			team.mascot = updateTeamReq.mascot;
			team.name = updateTeamReq.name;
			return proTeamRepo.save(team);
		}).orElseGet(() -> {
			return proTeamRepo.save(updateTeamReq);
		});
	}

	public String deleteTeam(long teamId) {
		try {
			proTeamRepo.deleteTeamById(teamId);
		} catch (Exception e) {

		}
		return "Delete was successful for team:" + teamId;
	}

}
