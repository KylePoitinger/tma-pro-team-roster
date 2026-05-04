package main.java.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProArenaEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProArenaRepo;

@Service
public class ProArenaService {

    @Autowired
    private ProArenaRepo proArenaRepo;

    public List<ProArenaEntity> getArenas() {
        return proArenaRepo.findAll();
    }

    public ProArenaEntity getArena(long arenaId) {
        return Optional.ofNullable(proArenaRepo.getOneByArenaId(arenaId))
                .orElseThrow(() -> new ResourceNotFoundException("Arena not found for this id :: " + arenaId));
    }

    public ProArenaEntity createArena(ProArenaEntity createArenaReq) {
        return proArenaRepo.save(createArenaReq);
    }

   	public ProArenaEntity updateArena(long arenaId, ProArenaEntity updateArenaReq) {
        return proArenaRepo.findById(arenaId).map(arena -> {
            arena.setName(updateArenaReq.getName());
            arena.setLocation(updateArenaReq.getLocation());
            arena.setCapacity(updateArenaReq.getCapacity());
            return proArenaRepo.save(arena);
        }).orElseThrow(() -> new ResourceNotFoundException("Arena not found for this id :: " + arenaId));
    }

    public String deleteArena(long arenaId) {
        ProArenaEntity arena = Optional.ofNullable(proArenaRepo.getOneByArenaId(arenaId))
                .orElseThrow(() -> new ResourceNotFoundException("Arena not found for this id :: " + arenaId));

        proArenaRepo.delete(arena);
        return "Delete was successful for arena:" + arenaId;
    }
}