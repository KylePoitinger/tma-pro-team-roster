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
			ensureMascotHasImage(mascot, usedImages);
			if (mascot.getImageUrl() != null) {
				usedImages.add(mascot.getImageUrl());
			}
		}
		return mascots;
	}

	private void ensureMascotHasImage(ProMascotEntity mascot, java.util.Set<String> usedInBatch) {
		String currentUrl = mascot.getImageUrl();
		if (currentUrl == null || currentUrl.equals("/images/random-mascot") || (usedInBatch != null && usedInBatch.contains(currentUrl))) {
			try {
				String newImageUrl = mascotImageService.fetchRandomMascotImage();
				int retries = 0;
				while (usedInBatch != null && usedInBatch.contains(newImageUrl) && retries < 3) {
					newImageUrl = mascotImageService.fetchRandomMascotImage();
					retries++;
				}
				mascot.setImageUrl(newImageUrl);
				proMascotRepo.save(mascot);
			} catch (Throwable t) {
				// Fallback to default or existing, but don't crash
			}
		}
	}

	public ProMascotEntity getProMascot(long mascotId) {
		ProMascotEntity mascot = Optional.ofNullable(proMascotRepo.getOneByMascotId(mascotId))
				.orElseThrow(() -> new ResourceNotFoundException("Mascot not found for this id :: " + mascotId));
		ensureMascotHasImage(mascot, null);
		return mascot;
	}

	public List<ProMascotEntity> getMascotsByTeam(long teamId) {
		List<ProMascotEntity> mascots = proMascotRepo.findByTeam_TeamId(teamId);
		java.util.Set<String> usedImages = new java.util.HashSet<>();
		for (ProMascotEntity mascot : mascots) {
			ensureMascotHasImage(mascot, usedImages);
			if (mascot.getImageUrl() != null) {
				usedImages.add(mascot.getImageUrl());
			}
		}
		return mascots;
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
			ensureMascotHasImage(mascot, null);
		}
		return mascot;
	}

}
