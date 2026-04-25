package main.java.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProMascotEntity;
import main.java.exception.ResourceNotFoundException;
import main.java.repository.ProMascotRepo;

import java.util.Random;

@Service
public class ProMascotService {

	@Autowired
	private ProMascotRepo proMascotRepo;

	@Autowired
	private MascotImageService mascotImageService;

	public List<ProMascotEntity> getMascots() {
		List<ProMascotEntity> mascots = proMascotRepo.findAll();
		java.util.Set<String> usedImages = new java.util.HashSet<>();
		for (ProMascotEntity mascot : mascots) {
			String currentUrl = mascot.getImageUrl();
			if (currentUrl == null || currentUrl.equals("/images/random-mascot") || usedImages.contains(currentUrl)) {
				try {
					String newImageUrl = mascotImageService.fetchRandomMascotImage();
					// Simple check to avoid duplicates in the same batch
					int retries = 0;
					while (usedImages.contains(newImageUrl) && retries < 5) {
						newImageUrl = mascotImageService.fetchRandomMascotImage();
						retries++;
					}
					mascot.setImageUrl(newImageUrl);
					proMascotRepo.save(mascot);
					usedImages.add(newImageUrl);
				} catch (Exception e) {
					// Fallback
				}
			} else {
				usedImages.add(currentUrl);
			}
		}
		return mascots;
	}

	public ProMascotEntity getProMascot(long mascotId) {
		return Optional.ofNullable(proMascotRepo.getOneByMascotId(mascotId))
				.orElseThrow(() -> new ResourceNotFoundException("Mascot not found for this id :: " + mascotId));
	}

	public List<ProMascotEntity> getMascotsByTeam(long teamId) {
		return proMascotRepo.findByTeam_TeamId(teamId);
	}

	public ProMascotEntity createProMascot(ProMascotEntity createMascotReq) {
		return proMascotRepo.save(createMascotReq);
	}

	public ProMascotEntity updateProMascot(long mascotId, ProMascotEntity updateMascotReq) {
		return proMascotRepo.findById(mascotId).map(mascot -> {
			if (updateMascotReq.name != null) mascot.name = updateMascotReq.name;
			if (updateMascotReq.species != null) mascot.species = updateMascotReq.species;
			if (updateMascotReq.team != null) mascot.team = updateMascotReq.team;
			if (updateMascotReq.description != null) mascot.description = updateMascotReq.description;
			if (updateMascotReq.costume != null) mascot.costume = updateMascotReq.costume;
			if (updateMascotReq.getImageUrl() != null && !updateMascotReq.getImageUrl().equals("/images/random-mascot")) {
				mascot.setImageUrl(updateMascotReq.getImageUrl());
			}
			return proMascotRepo.save(mascot);
		}).orElseThrow(() -> new ResourceNotFoundException("Mascot not found for this id :: " + mascotId));
	}

	public String deleteProMascot(long mascotId) {
		ProMascotEntity mascot = Optional.ofNullable(proMascotRepo.getOneByMascotId(mascotId))
				.orElseThrow(() -> new ResourceNotFoundException("Mascot not found for this id :: " + mascotId));

		proMascotRepo.delete(mascot);
		return "Delete was successful for mascot:" + mascotId;
	}

	public ProMascotEntity getRandomMascot() {
		ProMascotEntity mascot = proMascotRepo.findRandomMascot();
		if (mascot != null) {
			try {
				String imageUrl = mascotImageService.fetchRandomMascotImage();
				mascot.setImageUrl(imageUrl);
				proMascotRepo.save(mascot);
			} catch (Exception e) {
				// Fallback to existing image or leave as is if service fails
			}
		}
		return mascot;
	}

}
