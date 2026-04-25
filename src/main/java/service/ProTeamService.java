package main.java.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProTeamEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProTeamRepo;

@Service
public class ProTeamService {

	@Autowired
	private ProTeamRepo proTeamRepo;

	public List<ProTeamEntity> getTeams() {
		return proTeamRepo.findAll();
	}

	public ProTeamEntity getSingleTeamAndRoster(long teamId) {
		return Optional.ofNullable(proTeamRepo.getOneByTeamId(teamId))
				.orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));
	}

	public List<ProTeamEntity> getTeamsByFieldLookup(String name, String city, String mascot) {
		if (name != null) {
			return proTeamRepo.getTeamsByName(name);
		} else if (city != null) {
			return proTeamRepo.getTeamsByCity(city);
		} else if (mascot != null) {
			return proTeamRepo.getTeamsByMascot(mascot);
		} else {
			throw new ResourceNotFoundException("No search criteria provided");
		}
	}

	public ProTeamEntity createTeam(ProTeamEntity createTeamReq) {
		return proTeamRepo.save(createTeamReq);
	}

	public ProTeamEntity updateTeam(long teamId, ProTeamEntity updateTeamReq) {
		return proTeamRepo.findById(teamId).map(team -> {
			team.city = updateTeamReq.city;
			team.mascot = updateTeamReq.mascot;
			team.name = updateTeamReq.name;
			team.arena = updateTeamReq.arena;
			return proTeamRepo.save(team);
		}).orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));
	}

	public String deleteTeam(long teamId) {
		ProTeamEntity team = Optional.ofNullable(proTeamRepo.getOneByTeamId(teamId))
				.orElseThrow(() -> new ResourceNotFoundException("Team not found for this id :: " + teamId));

		proTeamRepo.delete(team);
		return "Delete was successful for team:" + teamId;
	}

}
