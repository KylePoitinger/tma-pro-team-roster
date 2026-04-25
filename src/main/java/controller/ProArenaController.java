package main.java.controller;

import java.util.List;

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

    @GetMapping
    public List<ProArenaEntity> getArenas() {
        return ProArenaService.getArenas();
    }

    @GetMapping("/{arenaId}")
    public ProArenaEntity getArena(@PathVariable long arenaId) {
        return ProArenaService.getArena(arenaId);
    }

    @PostMapping
    public ProArenaEntity createArena(@RequestBody ProArenaEntity arena) {
        return ProArenaService.createArena(arena);
    }

    @PutMapping("/{arenaId}")
    public ProArenaEntity updateArena(@PathVariable long arenaId, @RequestBody ProArenaEntity arena) {
        return ProArenaService.updateArena(arenaId, arena);
    }

    @DeleteMapping("/{arenaId}")
    public String deleteArena(@PathVariable long arenaId) {
        return ProArenaService.deleteArena(arenaId);
    }
}
