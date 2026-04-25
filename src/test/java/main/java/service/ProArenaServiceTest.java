package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProArenaEntity;
import main.java.repository.ProArenaRepo;

@ExtendWith(MockitoExtension.class)
public class ProArenaServiceTest {

	@Mock
	private ProArenaRepo proArenaRepo;

	@InjectMocks
	private ProArenaService proArenaService;

	private static final Logger LOG = LoggerFactory.getLogger(ProArenaServiceTest.class);

	@Test
	public void testGetArenas() {
		LOG.info("Testing getArenas method");
		List<ProArenaEntity> mockArenas = Arrays.asList(new ProArenaEntity());
		when(proArenaRepo.findAll()).thenReturn(mockArenas);
		List<ProArenaEntity> arenas = proArenaService.getArenas();
		LOG.info("Retrieved {} arenas", arenas.size());
		assertNotNull(arenas);
		assertEquals(1, arenas.size());
	}

	@Test
	public void testCreateArena() {
		LOG.info("Testing createArena method");
		ProArenaEntity arena = new ProArenaEntity();
		arena.arenaId = 1L;
		arena.name = "Test Arena";
		arena.location = "Test City";
		arena.capacity = 20000;
		arena.teamName = "Test Team";
		when(proArenaRepo.save(arena)).thenReturn(arena);
		ProArenaEntity created = proArenaService.createArena(arena);
		LOG.info("Created arena: {}", created.name);
		assertNotNull(created);
		assertEquals("Test Arena", created.name);
	}

	// Add more tests
}