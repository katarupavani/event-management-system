package cfg.proj.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.Event;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.exceptions.EventNotFoundException;
import cfg.proj.exceptions.InvalidEventException;
import cfg.proj.repos.EventRepository;

@Service
public class CreateEventService {

    @Autowired
    private EventRepository eventrepo;

    public EventEntitiy createEvent(Event dto) throws InvalidEventException {

        // Duplicate Event ID Check
        if (eventrepo.existsById(dto.getEventId())) {
            throw new InvalidEventException("Event ID already exists. Please use a unique ID.");
        }

        // Date Validation
        if (dto.getDate() == null || dto.getDate().isBefore(LocalDate.now())) {
            throw new InvalidEventException("Event date should be today or in the future.");
        }

        // Time Validation
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new InvalidEventException("Start time and End time cannot be null.");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new InvalidEventException("Start time should be before End time.");
        }

        // Limit Check
        if (dto.getLimit() <= 0) {
            throw new InvalidEventException("Event limit should be greater than zero.");
        }

        // Create Event
        EventEntitiy event = new EventEntitiy();
       // event.setEventId(dto.getEventId());
        event.setEventName(dto.getEventName());
        event.setCategory(dto.getCategory());
        event.setDescription(dto.getDescription());
        event.setLimit(dto.getLimit());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());

        return eventrepo.save(event);
    }

    public void deleteEvent(int eventId) throws EventNotFoundException {
        if (!eventrepo.existsById(eventId)) {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
        eventrepo.deleteById(eventId);
    }

    public EventEntitiy updateEvent(int eventId, EventEntitiy updatedEvent) throws EventNotFoundException {
        Optional<EventEntitiy> optionalEvent = eventrepo.findById(eventId);

        if (optionalEvent.isPresent()) {
            EventEntitiy existingEvent = optionalEvent.get();

            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setCategory(updatedEvent.getCategory());
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setLimit(updatedEvent.getLimit());
            existingEvent.setLocation(updatedEvent.getLocation());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setStartTime(updatedEvent.getStartTime());
            existingEvent.setEndTime(updatedEvent.getEndTime());

            return eventrepo.save(existingEvent);
        } else {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
    }

    public List<EventEntitiy> getEvents(){
     return eventrepo.findAll();
    }
    
    public EventEntitiy searchById(int eventId) throws EventNotFoundException {
     Optional<EventEntitiy> optevent=eventrepo.findById(eventId);
     if(optevent.isPresent()) {
      return optevent.get();
     }else {
      throw new EventNotFoundException("Event id not found"+eventId);
     }
    }
    
    public List<EventEntitiy> searchByCategory(String category) {
        return eventrepo.findByCategory(category);
    }

    public List<EventEntitiy> getUpcomingEvents() {
        LocalDate today = LocalDate.now();
        return eventrepo.findByDateAfter(today);
    }

    public List<EventEntitiy> getEventsByLocation(String location) {
        return eventrepo.findByLocationIgnoreCase(location);
    }
    
    public EventEntitiy updateEvent(int eventId, Event dto) throws EventNotFoundException, InvalidEventException {
        Optional<EventEntitiy> optionalEvent = eventrepo.findById(eventId);

        if (optionalEvent.isPresent()) {
            // Validation
            if (dto.getDate() == null || dto.getDate().isBefore(LocalDate.now())) {
                throw new InvalidEventException("Event date should be today or in the future.");
            }

            if (dto.getStartTime() == null || dto.getEndTime() == null) {
                throw new InvalidEventException("Start time and End time cannot be null.");
            }

            if (!dto.getStartTime().isBefore(dto.getEndTime())) {
                throw new InvalidEventException("Start time should be before End time.");
            }

            if (dto.getLimit() <= 10) {
                throw new InvalidEventException("Event limit should be greater than 10.");
            }

            // Perform update
            EventEntitiy existingEvent = optionalEvent.get();
            existingEvent.setEventName(dto.getEventName());
            existingEvent.setCategory(dto.getCategory());
            existingEvent.setDescription(dto.getDescription());
            existingEvent.setLimit(dto.getLimit());
            existingEvent.setLocation(dto.getLocation());
            existingEvent.setDate(dto.getDate());
            existingEvent.setStartTime(dto.getStartTime());
            existingEvent.setEndTime(dto.getEndTime());

            return eventrepo.save(existingEvent);
        } else {
            throw new EventNotFoundException("Event not found with ID: " + eventId);
        }
    }


}