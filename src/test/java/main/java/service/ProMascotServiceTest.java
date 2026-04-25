package main.java.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
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

    @Test
    public void testCreateProMascot() {
        ProMascotEntity mascot = new ProMascotEntity();
        mascot.mascotId = 1L;
        mascot.name = "Test Mascot";
        mascot.teamName = "Test Team";
        mascot.description = "Test Description";
        mascot.costume = "Test Costume";
        when(proMascotRepo.save(mascot)).thenReturn(mascot);
        ProMascotEntity created = proMascotService.createProMascot(mascot);
        assertNotNull(created);
        assertEquals("Test Mascot", created.name);
    }

    // Add more tests
}