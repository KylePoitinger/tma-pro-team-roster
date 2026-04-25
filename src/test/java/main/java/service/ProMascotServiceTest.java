package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.entity.ProMascotEntity;
import main.java.repository.ProMascotRepo;

@ExtendWith(MockitoExtension.class)
public class ProMascotServiceTest {

    @Mock
    private ProMascotRepo proMascotRepo;

    @InjectMocks
    private ProMascotService proMascotService;

    private static final Logger LOG = LoggerFactory.getLogger(ProMascotServiceTest.class);

    @Test
    public void testCreateProMascot() {
        LOG.info("Testing createProMascot method");
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        mascot.description = "Test Description";
        mascot.costume = "Test Costume";
        when(proMascotRepo.save(mascot)).thenReturn(mascot);
        ProMascotEntity created = proMascotService.createProMascot(mascot);
        LOG.info("Created mascot: {}", created.name);
        assertNotNull(created);
        assertEquals("Test Mascot", created.name);
    }

    // Add more tests
}