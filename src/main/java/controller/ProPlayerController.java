package main.java.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PostMapping("/players")
	public ProPlayerEntity createProPlayer(@RequestBody ProPlayerEntity createPlayerReq) {
		return ProPlayerService.createProPlayer(createPlayerReq);
	}

	// puts

	@PutMapping("/players/{playerId}")
	public ProPlayerEntity updateProPlayer(@PathVariable(value = "playerId") long playerId,
			@RequestBody ProPlayerEntity updatePlayerReq) {
		return ProPlayerService.updateProPlayer(playerId, updatePlayerReq);
	}

	// deletes

	@DeleteMapping("players/{playerId}")
	public String deleteProPlayer(@PathVariable(value = "playerId") long playerId) {
		return ProPlayerService.deleteProPlayer(playerId);
	}

}
