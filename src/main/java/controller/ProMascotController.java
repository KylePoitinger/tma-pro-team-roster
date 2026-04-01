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

import main.java.entity.ProMascotEntity;
import main.java.service.ProMascotService;

@RestController
public class ProMascotController {

	// gets

	@GetMapping("/mascots/{mascotId}")
	public ProMascotEntity getProMascot(@PathVariable(value = "mascotId") long mascotId) {
		return ProMascotService.getProMascot(mascotId);
	}

	@GetMapping("/mascots/team")
	public List<ProMascotEntity> getMascotsByTeam(@RequestParam(value = "team-name", required = false) String teamName) {
		return ProMascotService.getMascotsByTeam(teamName);
	}

	// posts

	@PostMapping("/mascots")
	public ProMascotEntity createProMascot(@RequestBody ProMascotEntity createMascotReq) {
		return ProMascotService.createProMascot(createMascotReq);
	}

	// puts

	@PutMapping("/mascots/{mascotId}")
	public ProMascotEntity updateProMascot(@PathVariable(value = "mascotId") long mascotId,
			@RequestBody ProMascotEntity updateMascotReq) {
		return ProMascotService.updateProMascot(mascotId, updateMascotReq);
	}

	// deletes

	@DeleteMapping("/mascots/{mascotId}")
	public String deleteProMascot(@PathVariable(value = "mascotId") long mascotId) {
		return ProMascotService.deleteProMascot(mascotId);
	}

}

