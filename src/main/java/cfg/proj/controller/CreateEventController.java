package cfg.proj.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cfg.proj.DTO.Event;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.bo.ResponseData;
import cfg.proj.service.CreateEventService;

@RestController
@RequestMapping("/events")
public class CreateEventController {

    @Autowired
    private CreateEventService eventService;

    @PostMapping("/create")
    public ResponseData createEvent(@RequestBody Event eventDto) {
        ResponseData response = new ResponseData();
        try {
            EventEntitiy createdEvent = eventService.createEvent(eventDto);
            response.setStatus("success");
            response.setMessage("Event created successfully.");
            response.setData(createdEvent);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error creating event: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteEvent(@PathVariable("id") int eventId) {
        ResponseData response = new ResponseData();
        try {
            eventService.deleteEvent(eventId);
            response.setStatus("success");
            response.setMessage("Event deleted successfully.");
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error deleting event: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/category/{category}")
    public ResponseData getEventsByCategory(@PathVariable String category) {
        ResponseData response = new ResponseData();
        try {
            List<EventEntitiy> events = eventService.searchByCategory(category);
            response.setStatus("success");
            response.setMessage("Events fetched successfully by category.");
            response.setData(events);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching events by category: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/upcoming")
    public ResponseData getUpcomingEvents() {
        ResponseData response = new ResponseData();
        try {
            List<EventEntitiy> events = eventService.getUpcomingEvents();
            response.setStatus("success");
            response.setMessage("Upcoming events fetched successfully.");
            response.setData(events);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching upcoming events: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/location/{location}")
    public ResponseData getEventsByLocation(@PathVariable String location) {
        ResponseData response = new ResponseData();
        try {
            List<EventEntitiy> events = eventService.getEventsByLocation(location);
            response.setStatus("success");
            response.setMessage("Events fetched successfully by location.");
            response.setData(events);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching events by location: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
    
    
   

    

}
