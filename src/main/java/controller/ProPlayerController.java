package main.java.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entity.ProPlayerEntity;
import main.java.service.ProPlayerService;

@RestController
public class ProPlayerController {

	// gets

	@GetMapping("/players/{playerId}")
	public ProPlayerEntity getProPlayer(@PathVariable(value = "playerId") long playerId) {
		return ProPlayerService.getProPlayer(playerId);
	}

	// posts

	@PostMapping("/teams/{teamId}/players")
	public ProPlayerEntity createProPlayer() {
		return ProPlayerService.createProPlayer();
	}

	// puts

	@PutMapping("/teams/{teamId}/players/{playerId}")
	public ProPlayerEntity updateProPlayer() {
		return ProPlayerService.updateProPlayer();
	}

	// deletes

	@DeleteMapping("/teams/{teamId}/players/{playerId}")
	public ProPlayerEntity deleteProPlayer() {
		return ProPlayerService.deleteProPlayer();
	}

}
