package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProArenaEntity;
import main.java.exception.ResourceNotFoundException;
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
		when(proArenaRepo.save(arena)).thenReturn(arena);
		ProArenaEntity created = proArenaService.createArena(arena);
		LOG.info("Created arena: {}", created.name);
		assertNotNull(created);
		assertEquals("Test Arena", created.name);
	}

	@Test
	public void testGetArenaSuccess() {
		LOG.info("Testing getArena method - success");
		ProArenaEntity arena = new ProArenaEntity();
		arena.arenaId = 1L;
		arena.name = "Test Arena";
		when(proArenaRepo.getOneByArenaId(1L)).thenReturn(arena);
		ProArenaEntity result = proArenaService.getArena(1L);
		assertNotNull(result);
		assertEquals("Test Arena", result.name);
	}

	@Test
	public void testGetArenaNotFound() {
		LOG.info("Testing getArena method - not found");
		when(proArenaRepo.getOneByArenaId(999L)).thenReturn(null);
		assertThrows(ResourceNotFoundException.class, () -> proArenaService.getArena(999L));
	}

	@Test
	public void testUpdateArenaSuccess() {
		LOG.info("Testing updateArena method - success");
		ProArenaEntity existingArena = new ProArenaEntity();
		existingArena.arenaId = 1L;
		existingArena.name = "Old Arena";
		existingArena.location = "Old City";
		existingArena.capacity = 10000;

		ProArenaEntity updateReq = new ProArenaEntity();
		updateReq.name = "New Arena";
		updateReq.location = "New City";
		updateReq.capacity = 25000;

		when(proArenaRepo.findById(1L)).thenReturn(Optional.of(existingArena));
		when(proArenaRepo.save(any(ProArenaEntity.class))).thenReturn(existingArena);

		ProArenaEntity result = proArenaService.updateArena(1L, updateReq);
		assertNotNull(result);
		assertEquals("New Arena", result.name);
		assertEquals("New City", result.location);
		assertEquals(25000, result.capacity);
	}

	@Test
	public void testUpdateArenaNotFound() {
		LOG.info("Testing updateArena method - not found");
		ProArenaEntity updateReq = new ProArenaEntity();
		when(proArenaRepo.findById(999L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> proArenaService.updateArena(999L, updateReq));
	}

	@Test
	public void testDeleteArenaSuccess() {
		LOG.info("Testing deleteArena method - success");
		ProArenaEntity arena = new ProArenaEntity();
		arena.arenaId = 1L;
		arena.name = "Test Arena";
		when(proArenaRepo.getOneByArenaId(1L)).thenReturn(arena);
		String result = proArenaService.deleteArena(1L);
		assertNotNull(result);
		assertTrue(result.contains("Delete was successful"));
		verify(proArenaRepo, times(1)).delete(arena);
	}

	@Test
	public void testDeleteArenaNotFound() {
		LOG.info("Testing deleteArena method - not found");
		when(proArenaRepo.getOneByArenaId(999L)).thenReturn(null);
		assertThrows(ResourceNotFoundException.class, () -> proArenaService.deleteArena(999L));
	}
}