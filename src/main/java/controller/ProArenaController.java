package main.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.entity.ProArenaEntity;
import main.java.service.ProArenaService;

@RestController
@RequestMapping("/arenas")
public class ProArenaController {

	@Autowired
	private ProArenaService proArenaService;

	@GetMapping
	public List<ProArenaEntity> getArenas() {
		return proArenaService.getArenas();
	}

	@GetMapping("/{arenaId}")
	public ProArenaEntity getArena(@PathVariable long arenaId) {
		return proArenaService.getArena(arenaId);
	}

	@PostMapping
	public ProArenaEntity createArena(@RequestBody ProArenaEntity arena) {
		return proArenaService.createArena(arena);
	}

	@PutMapping("/{arenaId}")
	public ProArenaEntity updateArena(@PathVariable long arenaId, @RequestBody ProArenaEntity arena) {
		return proArenaService.updateArena(arenaId, arena);
	}

	@DeleteMapping("/{arenaId}")
	public String deleteArena(@PathVariable long arenaId) {
		return proArenaService.deleteArena(arenaId);
	}
}