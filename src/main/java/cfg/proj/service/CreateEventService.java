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
        if (eventrepo.existsById(dto.getEvent_id())) {
            throw new InvalidEventException("Event ID already exists. Please use a unique ID.");
        }

        // Date Validation
        if (dto.getDate() == null || dto.getDate().isBefore(LocalDate.now())) {
            throw new InvalidEventException("Event date should be today or in the future.");
        }

        // Time Validation
        if (dto.getStart_time() == null || dto.getEnd_time() == null) {
            throw new InvalidEventException("Start time and End time cannot be null.");
        }

        if (!dto.getStart_time().isBefore(dto.getEnd_time())) {
            throw new InvalidEventException("Start time should be before End time.");
        }

        // Limit Check
        if (dto.getLimit() <= 0) {
            throw new InvalidEventException("Event limit should be greater than zero.");
        }

        // Create Event
        EventEntitiy event = new EventEntitiy();
        event.setEventId(dto.getEvent_id());
        event.setEventName(dto.getEvent_name());
        event.setCategory(dto.getCategory());
        event.setDescription(dto.getDescription());
        event.setLimit(dto.getLimit());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStart_time());
        event.setEndTime(dto.getEnd_time());

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

}
