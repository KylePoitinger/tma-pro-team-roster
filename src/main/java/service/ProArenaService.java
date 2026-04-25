package main.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProArenaEntity;
import main.java.repository.ProArenaRepo;

@Service
public class ProArenaService {

    @Autowired
    private static ProArenaRepo proArenaRepo;

    public static List<ProArenaEntity> getArenas() {
        return proArenaRepo.findAll();
    }

    public static ProArenaEntity getArena(long arenaId) {
        return proArenaRepo.getOneByArenaId(arenaId);
    }

    public static ProArenaEntity createArena(ProArenaEntity createArenaReq) {
        return proArenaRepo.save(createArenaReq);
    }

    public static ProArenaEntity updateArena(long arenaId, ProArenaEntity updateArenaReq) {
        return proArenaRepo.findById(arenaId).map(arena -> {
            arena.name = updateArenaReq.name;
            arena.location = updateArenaReq.location;
            arena.capacity = updateArenaReq.capacity;
            arena.teamName = updateArenaReq.teamName;
            return proArenaRepo.save(arena);
        }).orElseGet(() -> {
            return proArenaRepo.save(updateArenaReq);
        });
    }

    public static String deleteArena(long arenaId) {
        try {
            proArenaRepo.deleteArenaById(arenaId);
        } catch (Exception e) {
            return "Delete was unsuccessful with error: " + e.toString();
        }
        return "Delete was successful for arena:" + arenaId;
    }
}
