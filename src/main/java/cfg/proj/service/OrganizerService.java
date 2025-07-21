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

    public Organizer createOrganizer(Organizer organizerDTO) {
        validateOrganizerCredentials(organizerDTO.getUsername(), organizerDTO.getEmail(), organizerDTO.getPassword());

        OrganizerEntity orgEntity = new OrganizerEntity();
        orgEntity.setId(organizerDTO.getId()); 
        orgEntity.setUsername(organizerDTO.getUsername());
        orgEntity.setEmail(organizerDTO.getEmail());
        orgEntity.setPassword(organizerDTO.getPassword());

        OrganizerEntity savedEntity = organizerRepo.save(orgEntity);

        
        Organizer savedDTO = new Organizer();
        savedDTO.setId(savedEntity.getId());
        savedDTO.setUsername(savedEntity.getUsername());
        savedDTO.setEmail(savedEntity.getEmail());
        savedDTO.setPassword(savedEntity.getPassword());

        return savedDTO;
    }


    public void deleteOrganizer(int organizerId) throws IdNotFoundException {
        Optional<OrganizerEntity> optionalOrg = organizerRepo.findById(organizerId);

        if (optionalOrg.isPresent()) {
            organizerRepo.deleteById(organizerId);
        } else {
            throw new IdNotFoundException("Organizer not found with ID: " + organizerId);
        }
    }


    public Organizer updateOrganizer(int organizerId, Organizer updatedDTO) throws IdNotFoundException {
        Optional<OrganizerEntity> optionalOrg = organizerRepo.findById(organizerId);

        if (optionalOrg.isPresent()) {
            validateOrganizerCredentials(updatedDTO.getUsername(), updatedDTO.getEmail(), updatedDTO.getPassword());

            OrganizerEntity existingOrg = optionalOrg.get();
            existingOrg.setUsername(updatedDTO.getUsername());
            existingOrg.setEmail(updatedDTO.getEmail());
            existingOrg.setPassword(updatedDTO.getPassword());
            OrganizerEntity savedEntity = organizerRepo.save(existingOrg);

            Organizer resultDTO = new Organizer();
            resultDTO.setId(savedEntity.getId());
            resultDTO.setUsername(savedEntity.getUsername());
            resultDTO.setEmail(savedEntity.getEmail());
            resultDTO.setPassword(savedEntity.getPassword());

            return resultDTO;
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
