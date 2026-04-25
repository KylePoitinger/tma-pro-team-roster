package main.java.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Pro Team Controller", description = "Endpoints for managing professional sports teams")
public class ProTeamController {

	@Autowired
	private ProTeamService proTeamService;

	// gets

	@GetMapping("/teams")
	@Operation(summary = "Get all teams", description = "Returns a list of all professional teams in the database")
	public List<ProTeamEntity> getTeams() {
		return proTeamService.getTeams();
	}

	@GetMapping("/teams/{teamId}/roster")
	@Operation(summary = "Get team and roster", description = "Returns details for a single team including its full player roster")
	public ProTeamEntity getSingleTeamAndRoster(@PathVariable(value = "teamId") long teamId) {
		return proTeamService.getSingleTeamAndRoster(teamId);
	}

	@GetMapping("/teams/fields")
	public List<ProTeamEntity> getTeamsByFieldLookup(@RequestParam(value = "team-name", required = false) String name,
			@RequestParam(value = "team-city", required = false) String city,
			@RequestParam(value = "team-mascot", required = false) String mascot) {
		return proTeamService.getTeamsByFieldLookup(name, city, mascot);
	}

	// posts

	@PostMapping("/teams")
	public ProTeamEntity createTeam(@RequestBody ProTeamEntity createTeamReq) {
		return proTeamService.createTeam(createTeamReq);
	}

	// puts

	@PutMapping("/teams/{teamId}")
	public ProTeamEntity updateTeam(@PathVariable(value = "teamId") long teamId,
			@RequestBody ProTeamEntity updateTeamReq) {

		return proTeamService.updateTeam(teamId, updateTeamReq);
	}

	// deletes

	@DeleteMapping("/teams/{teamId}")
	public String deleteTeam(@PathVariable(value = "teamId") long teamId) {
		return proTeamService.deleteTeam(teamId);
	}

}
