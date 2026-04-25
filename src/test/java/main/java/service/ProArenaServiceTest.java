package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
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

	@Test
	public void testGetArenas() {
		List<ProArenaEntity> mockArenas = Arrays.asList(new ProArenaEntity());
		when(proArenaRepo.findAll()).thenReturn(mockArenas);
		List<ProArenaEntity> arenas = proArenaService.getArenas();
		assertNotNull(arenas);
		assertEquals(1, arenas.size());
	}

	@Test
	public void testCreateArena() {
		ProArenaEntity arena = new ProArenaEntity();
		arena.arenaId = 1L;
		arena.name = "Test Arena";
		arena.location = "Test City";
		arena.capacity = 20000;
		arena.teamName = "Test Team";
		when(proArenaRepo.save(arena)).thenReturn(arena);
		ProArenaEntity created = proArenaService.createArena(arena);
		assertNotNull(created);
		assertEquals("Test Arena", created.name);
	}

	// Add more tests
}
