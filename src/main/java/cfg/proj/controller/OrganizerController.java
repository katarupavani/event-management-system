package cfg.proj.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cfg.proj.DTO.Organizer;
import cfg.proj.Entities.OrganizerEntity;
import cfg.proj.bo.ResponseData;
import cfg.proj.repos.OrganizerRepository;
import cfg.proj.service.OrganizerService;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private OrganizerRepository organizerRepo;

    @PostMapping("/create")
    public ResponseData createOrganizer(@RequestBody Organizer organizer) {
        ResponseData response = new ResponseData();
        try {
            OrganizerEntity created = organizerService.createOrganizer(organizer);
            response.setStatus("success");
            response.setMessage("Organizer created successfully.");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error creating organizer: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData getOrganizerById(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            Optional<OrganizerEntity> optional = organizerRepo.findById(id);
            if (optional.isPresent()) {
                response.setStatus("success");
                response.setMessage("Organizer found.");
                response.setData(optional.get());
            } else {
                response.setStatus("error");
                response.setMessage("Organizer not found with ID: " + id);
                response.setData(null);
            }
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching organizer: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping
    public ResponseData getAllOrganizers() {
        ResponseData response = new ResponseData();
        try {
            List<OrganizerEntity> organizers = organizerRepo.findAll();
            response.setStatus("success");
            response.setMessage("Organizers fetched successfully.");
            response.setData(organizers);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching organizers: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteOrganizer(@PathVariable("id") int id) {
        ResponseData response = new ResponseData();
        try {
            organizerService.deleteOrganizer(id);
            response.setStatus("success");
            response.setMessage("Organizer deleted successfully.");
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error deleting organizer: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
