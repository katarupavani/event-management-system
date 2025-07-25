package cfg.proj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.Organizer;
import cfg.proj.Entities.OrganizerEntity;
import cfg.proj.exceptions.IdNotFoundException;
import cfg.proj.repos.OrganizerRepository;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepo;

    public OrganizerEntity createOrganizer(Organizer organizer) {
        validateOrganizerCredentials(organizer.getUsername(), organizer.getEmail(), organizer.getPassword());

        OrganizerEntity org = new OrganizerEntity();
        org.setId(organizer.getId());
        org.setUsername(organizer.getUsername());
        org.setEmail(organizer.getEmail());
        org.setPassword(organizer.getPassword());

        return organizerRepo.save(org);
    }

    public void deleteOrganizer(int organizerId) throws IdNotFoundException {
        Optional<OrganizerEntity> optionalOrg = organizerRepo.findById(organizerId);

        if (optionalOrg.isPresent()) {
            OrganizerEntity org = optionalOrg.get();
            if (org.getId() == organizerId) {
                organizerRepo.deleteById(organizerId);
            } else {
                throw new IdNotFoundException("Organizer ID mismatch.");
            }
        } else {
            throw new IdNotFoundException("Organizer not found with ID: " + organizerId);
        }
    }

    public OrganizerEntity updateOrganizer(int organizerId, Organizer updatedOrganizer) throws IdNotFoundException {
        Optional<OrganizerEntity> optionalOrg = organizerRepo.findById(organizerId);

        if (optionalOrg.isPresent()) {
            validateOrganizerCredentials(updatedOrganizer.getUsername(), updatedOrganizer.getEmail(), updatedOrganizer.getPassword());

            OrganizerEntity existingOrg = optionalOrg.get();
            existingOrg.setUsername(updatedOrganizer.getUsername());
            existingOrg.setEmail(updatedOrganizer.getEmail());
            existingOrg.setPassword(updatedOrganizer.getPassword());

            return organizerRepo.save(existingOrg);
        } else {
            throw new IdNotFoundException("Organizer not found with ID: " + organizerId);
        }
    }

    private void validateOrganizerCredentials(String username, String email, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters long.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
    }
}
