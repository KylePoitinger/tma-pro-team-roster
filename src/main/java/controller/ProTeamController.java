package main.java.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.entity.ProTeamEntity;
import main.java.service.ProTeamService;

@RestController
public class ProTeamController {

	// gets

	@GetMapping("/teams")
	public List<ProTeamEntity> getTeams() {
		return ProTeamService.getTeams();
	}

	@GetMapping("/teams/{teamId}/roster")
	public ProTeamEntity getSingleTeamAndRoster(@PathVariable(value = "teamId") long teamId) {
		return ProTeamService.getSingleTeamAndRoster(teamId);
	}

	@GetMapping("/teams/fields")
	public List<ProTeamEntity> getTeamsByFieldLookup(@RequestParam(value = "team-name", required = false) String name,
			@RequestParam(value = "team-city", required = false) String city,
			@RequestParam(value = "team-mascot", required = false) String mascot) {
		return ProTeamService.getTeamsByFieldLookup(name, city, mascot);
	}

	// posts

	@PostMapping("/teams")
	public ProTeamEntity createTeam(@RequestBody ProTeamEntity createTeamReq) {
		return ProTeamService.createTeam(createTeamReq);
	}

	// puts

	@PutMapping("/teams/{teamId}")
	public ProTeamEntity updateTeam(@PathVariable(value = "teamId") long teamId,
			@RequestBody ProTeamEntity updateTeamReq) {
		return ProTeamService.updateTeam(teamId, updateTeamReq);
	}

	// deletes

	@DeleteMapping("/teams/{teamId}")
	public String deleteTeam(@PathVariable(value = "teamId") long teamId) {
		return ProTeamService.deleteTeam(teamId);
	}
}
