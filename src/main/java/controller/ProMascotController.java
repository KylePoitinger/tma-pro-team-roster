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

import main.java.entity.ProMascotEntity;
import main.java.service.ProMascotService;

@RestController
@Tag(name = "Pro Mascot Controller", description = "Endpoints for managing homeTeam mascots")
public class ProMascotController {

	@Autowired
	private ProMascotService proMascotService;

	// gets

	@GetMapping("/mascots")
	@Operation(summary = "Get all mascots", description = "Returns a list of all professional mascots in the database")
	public List<ProMascotEntity> getMascots() {
		return proMascotService.getMascots();
	}

	@GetMapping("/mascots/{mascotId}")
	@Operation(summary = "Get mascot by ID", description = "Returns details for a single mascot")
	public ProMascotEntity getProMascot(@PathVariable(value = "mascotId") long mascotId) {
		return proMascotService.getProMascot(mascotId);
	}

	@GetMapping("/mascots/team")
	public List<ProMascotEntity> getMascotsByTeam(@RequestParam(value = "team-id", required = false) long teamId) {
		return proMascotService.getMascotsByTeam(teamId);
	}

	// posts

	@PostMapping("/mascots")
	public ProMascotEntity createProMascot(@RequestBody ProMascotEntity createMascotReq) {
		return proMascotService.createProMascot(createMascotReq);
	}

	// puts

	@PutMapping("/mascots/{mascotId}")
	public ProMascotEntity updateProMascot(@PathVariable(value = "mascotId") long mascotId,
			@RequestBody ProMascotEntity updateMascotReq) {
		return proMascotService.updateProMascot(mascotId, updateMascotReq);
	}

	// deletes

	@DeleteMapping("/mascots/{mascotId}")
	public String deleteProMascot(@PathVariable(value = "mascotId") long mascotId) {
		return proMascotService.deleteProMascot(mascotId);
	}

	@GetMapping("/mascots/random")
	@Operation(summary = "Get a random mascot", description = "Returns details for a random mascot with a fresh image")
	public ProMascotEntity getRandomMascot() {
		return proMascotService.getRandomMascot();
	}

}
