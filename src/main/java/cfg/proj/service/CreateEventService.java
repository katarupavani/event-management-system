package cfg.proj.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.Event;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.exceptions.EventNotFoundException;
import cfg.proj.repos.EventRepository;

@Service
public class CreateEventService {

	@Autowired
	private EventRepository eventrepo;

	public EventEntitiy createEvent(Event dto) {
		EventEntitiy event = new EventEntitiy();
		event.setEventId(dto.getEvent_id());
		event.setEventName(dto.getEvent_name());
		event.setCategory(dto.getCategory());
		event.setDescription(dto.getDescription());
		event.setLimit(dto.getLimit());
		event.setLocation(dto.getLocation());
		event.setDate(dto.getDate());

		return eventrepo.save(event);
	}

	public void deleteEvent(int eventId) throws EventNotFoundException {
		Optional<EventEntitiy> optionalEvent = eventrepo.findById(eventId);

		if (optionalEvent.isPresent()) {
			EventEntitiy event = optionalEvent.get();

			if (event.getEventId() == eventId) {
				eventrepo.deleteById(eventId);
			} else {
				throw new EventNotFoundException("Event ID mismatch.");
			}

		} else {
			throw new EventNotFoundException("Event not found with ID: " + eventId);
		}
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
