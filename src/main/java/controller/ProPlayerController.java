package main.java.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Pro Player Controller", description = "Endpoints for managing professional athletes")
public class ProPlayerController {

	@Autowired
	private ProPlayerService proPlayerService;

	// gets

	@GetMapping("/players/{playerId}")
	@Operation(summary = "Get player by ID", description = "Returns detailed information about a single player")
	public ProPlayerEntity getProPlayer(@PathVariable(value = "playerId") long playerId) {
		return proPlayerService.getProPlayer(playerId);
	}

	// posts

	@PostMapping("/players")
	public ProPlayerEntity createProPlayer(@RequestBody ProPlayerEntity createPlayerReq) {
		return proPlayerService.createProPlayer(createPlayerReq);
	}

	// puts

	@PutMapping("/players/{playerId}")
	public ProPlayerEntity updateProPlayer(@PathVariable(value = "playerId") long playerId,
			@RequestBody ProPlayerEntity updatePlayerReq) {
		return proPlayerService.updateProPlayer(playerId, updatePlayerReq);
	}

	// deletes

	@DeleteMapping("players/{playerId}")
	public String deleteProPlayer(@PathVariable(value = "playerId") long playerId) {
		return proPlayerService.deleteProPlayer(playerId);
	}

	@PutMapping("/players/{playerId}/trade/{teamId}")
	@Operation(summary = "Trade player to another team", description = "Moves a player from their current team to a new team")
	public void tradePlayer(@PathVariable(value = "playerId") long playerId,
									   @PathVariable(value = "teamId") long teamId) {
		proPlayerService.initiateTrade(playerId, teamId);
	}

}
