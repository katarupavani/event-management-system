package cfg.proj.ems;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import cfg.proj.DTO.Organizer;
import cfg.proj.Entities.OrganizerEntity;
import cfg.proj.exceptions.IdNotFoundException;
import cfg.proj.repos.OrganizerRepository;
import cfg.proj.service.OrganizerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrganizerTest {

    @Mock
    private OrganizerRepository organizerRepo;

    @InjectMocks
    private OrganizerService organizerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    void testCreateOrganizer_Success() {
        Organizer organizer = new Organizer();
        organizer.setId(1);
        organizer.setUsername("validUser");
        organizer.setEmail("user@example.com");
        organizer.setPassword("password123");

        OrganizerEntity savedEntity = new OrganizerEntity();
        savedEntity.setId(1);
        savedEntity.setUsername("validUser");
        savedEntity.setEmail("user@example.com");
        savedEntity.setPassword("password123");

        when(organizerRepo.save(any(OrganizerEntity.class))).thenReturn(savedEntity);

        Organizer result = organizerService.createOrganizer(organizer);

        assertNotNull(result);
        assertEquals("validUser", result.getUsername());
        assertEquals("user@example.com", result.getEmail());
        verify(organizerRepo, times(1)).save(any(OrganizerEntity.class));
    }

    
    @Test
    void testCreateOrganizer_InvalidUsername_Empty() {
        Organizer organizer = new Organizer();
        organizer.setUsername("");
        organizer.setEmail("user@example.com");
        organizer.setPassword("password123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Username cannot be empty.", ex.getMessage());
    }

    @Test
    void testCreateOrganizer_InvalidUsername_Short() {
        Organizer organizer = new Organizer();
        organizer.setUsername("usr");
        organizer.setEmail("user@example.com");
        organizer.setPassword("password123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Username must be at least 5 characters long.", ex.getMessage());
    }

    @Test
    void testCreateOrganizer_InvalidEmail_Empty() {
        Organizer organizer = new Organizer();
        organizer.setUsername("validUser");
        organizer.setEmail("");
        organizer.setPassword("password123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Email cannot be empty.", ex.getMessage());
    }

    @Test
    void testCreateOrganizer_InvalidEmail_Format() {
        Organizer organizer = new Organizer();
        organizer.setUsername("validUser");
        organizer.setEmail("invalidemail");
        organizer.setPassword("password123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Invalid email format.", ex.getMessage());
    }

    @Test
    void testCreateOrganizer_InvalidPassword_Empty() {
        Organizer organizer = new Organizer();
        organizer.setUsername("validUser");
        organizer.setEmail("user@example.com");
        organizer.setPassword("");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Password cannot be empty.", ex.getMessage());
    }

    @Test
    void testCreateOrganizer_InvalidPassword_Short() {
        Organizer organizer = new Organizer();
        organizer.setUsername("validUser");
        organizer.setEmail("user@example.com");
        organizer.setPassword("short");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.createOrganizer(organizer);
        });
        assertEquals("Password must be at least 8 characters long.", ex.getMessage());
    }

   
    @Test
    void testDeleteOrganizer_Success() throws IdNotFoundException {
        OrganizerEntity existing = new OrganizerEntity();
        existing.setId(1);

        when(organizerRepo.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(organizerRepo).deleteById(1);

        assertDoesNotThrow(() -> organizerService.deleteOrganizer(1));
        verify(organizerRepo, times(1)).deleteById(1);
    }

    
    @Test
    void testDeleteOrganizer_NotFound() {
        when(organizerRepo.findById(1)).thenReturn(Optional.empty());

        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> {
            organizerService.deleteOrganizer(1);
        });
        assertEquals("Organizer not found with ID: 1", ex.getMessage());
    }

    
    @Test
    void testDeleteOrganizer_IdMismatch() {
        OrganizerEntity existing = new OrganizerEntity();
        existing.setId(2);

        when(organizerRepo.findById(1)).thenReturn(Optional.of(existing));

        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> {
            organizerService.deleteOrganizer(1);
        });
        assertEquals("Organizer ID mismatch.", ex.getMessage());
    }

    
    @Test
    void testUpdateOrganizer_Success() throws IdNotFoundException {
        OrganizerEntity existing = new OrganizerEntity();
        existing.setId(1);
        existing.setUsername("oldUser");
        existing.setEmail("old@example.com");
        existing.setPassword("oldpassword");

        Organizer updated = new Organizer();
        updated.setUsername("newUser");
        updated.setEmail("new@example.com");
        updated.setPassword("newpassword123");

        when(organizerRepo.findById(1)).thenReturn(Optional.of(existing));
        when(organizerRepo.save(any(OrganizerEntity.class))).thenAnswer(i -> i.getArgument(0));

        Organizer result = organizerService.updateOrganizer(1, updated);

        assertEquals("newUser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newpassword123", result.getPassword());
    }

    
    @Test
    void testUpdateOrganizer_NotFound() {
        when(organizerRepo.findById(1)).thenReturn(Optional.empty());

        Organizer updated = new Organizer();
        updated.setUsername("user");
        updated.setEmail("user@example.com");
        updated.setPassword("password123");

        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> {
            organizerService.updateOrganizer(1, updated);
        });
        assertEquals("Organizer not found with ID: 1", ex.getMessage());
    }

    
    @Test
    void testUpdateOrganizer_InvalidUsername() {
        OrganizerEntity existing = new OrganizerEntity();
        existing.setId(1);

        Organizer updated = new Organizer();
        updated.setUsername("usr");
        updated.setEmail("user@example.com");
        updated.setPassword("password123");

        when(organizerRepo.findById(1)).thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            organizerService.updateOrganizer(1, updated);
        });
        assertEquals("Username must be at least 5 characters long.", ex.getMessage());
    }

    
}
