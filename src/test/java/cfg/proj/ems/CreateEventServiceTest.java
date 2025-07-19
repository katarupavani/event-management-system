package cfg.proj.ems;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import cfg.proj.DTO.Event;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.exceptions.EventNotFoundException;
import cfg.proj.repos.EventRepository;
import cfg.proj.service.CreateEventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
public class CreateEventServiceTest {

	    @Mock
	    private EventRepository eventrepo;

	    @InjectMocks
	    private CreateEventService createEventService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testCreateEvent() {
	        Event dto = new Event();
	        dto.setEvent_id(1);
	        dto.setEvent_name("Test Event");
	        dto.setCategory("Music");
	        dto.setDescription("Fun event");
	        dto.setLimit(100);
	        dto.setLocation("City Hall");
	        dto.setDate(LocalDate.now().plusDays(10));

	        EventEntitiy entity = new EventEntitiy();
	        when(eventrepo.save(any(EventEntitiy.class))).thenReturn(entity);

	        EventEntitiy result = createEventService.createEvent(dto);
	        assertNotNull(result);
	        verify(eventrepo, times(1)).save(any(EventEntitiy.class));
	    }

	    @Test
	    public void testDeleteEvent_Success() throws EventNotFoundException {
	        EventEntitiy entity = new EventEntitiy();
	        entity.setEventId(1);

	        when(eventrepo.findById(1)).thenReturn(Optional.of(entity));

	        assertDoesNotThrow(() -> createEventService.deleteEvent(1));
	        verify(eventrepo, times(1)).deleteById(1);
	    }

	    @Test
	    public void testDeleteEvent_NotFound() {
	        when(eventrepo.findById(1)).thenReturn(Optional.empty());
	        assertThrows(EventNotFoundException.class, () -> createEventService.deleteEvent(1));
	    }

	    @Test
	    public void testUpdateEvent_Success() throws EventNotFoundException {
	        EventEntitiy existing = new EventEntitiy();
	        existing.setEventId(1);

	        EventEntitiy updated = new EventEntitiy();
	        updated.setEventName("Updated Event");

	        when(eventrepo.findById(1)).thenReturn(Optional.of(existing));
	        when(eventrepo.save(any(EventEntitiy.class))).thenReturn(updated);

	        EventEntitiy result = createEventService.updateEvent(1, updated);
	        assertEquals("Updated Event", result.getEventName());
	    }

	    @Test
	    public void testUpdateEvent_NotFound() {
	        when(eventrepo.findById(1)).thenReturn(Optional.empty());
	        assertThrows(EventNotFoundException.class, () -> createEventService.updateEvent(1, new EventEntitiy()));
	    }

	    @Test
	    public void testSearchByCategory() {
	        List<EventEntitiy> events = Arrays.asList(new EventEntitiy(), new EventEntitiy());
	        when(eventrepo.findByCategory("Music")).thenReturn(events);

	        List<EventEntitiy> result = createEventService.searchByCategory("Music");
	        assertEquals(2, result.size());
	    }

	    @Test
	    public void testGetUpcomingEvents() {
	        List<EventEntitiy> events = Arrays.asList(new EventEntitiy(), new EventEntitiy());
	        when(eventrepo.findByDateAfter(any(LocalDate.class))).thenReturn(events);

	        List<EventEntitiy> result = createEventService.getUpcomingEvents();
	        assertEquals(2, result.size());
	    }

	    @Test
	    public void testGetEventsByLocation() {
	        List<EventEntitiy> events = Arrays.asList(new EventEntitiy());
	        when(eventrepo.findByLocationIgnoreCase("City Hall")).thenReturn(events);

	        List<EventEntitiy> result = createEventService.getEventsByLocation("City Hall");
	        assertEquals(1, result.size());
	    }
	}


