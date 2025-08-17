package cfg.proj.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.BookEvent;
import cfg.proj.DTO.User;
import cfg.proj.DTO.Event;
import cfg.proj.Entities.BookEventEntity;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.BookEventException;
import cfg.proj.exceptions.BookNotFoundException;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.BookEventRepository;
import cfg.proj.repos.EventRepository;
import cfg.proj.repos.UserRepository;

@Service
public class BookEventService {

    @Autowired
    private BookEventRepository bookEventRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EventRepository eventRepo;

    // Helper: Convert UserEntity to User DTO
    private User convertUserEntityToDTO(UserEntity userEntity) {
        User user = new User();
        user.setUserId(userEntity.getUserId());
        user.setUsername(userEntity.getUserName());
        user.setEmail(userEntity.getEmail());
        user.setRole(userEntity.getRole());
        return user;
    }

    // Helper: Convert EventEntity to Event DTO
    private Event convertEventEntityToDTO(EventEntitiy eventEntity) {
        Event event = new Event();
        event.setEventId(eventEntity.getEventId());
        event.setEventName(eventEntity.getEventName());
        event.setDate(eventEntity.getDate());
        event.setStartTime(eventEntity.getStartTime());
        event.setEndTime(eventEntity.getEndTime());
        event.setLocation(eventEntity.getLocation());
        event.setLimit(eventEntity.getLimit());
        return event;
    }

    // Convert Booking entity to DTO with nested User and Event
    private BookEvent convertToDTO(BookEventEntity entity) {
        BookEvent dto = new BookEvent();
        dto.setBookId(entity.getBookId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setEventId(entity.getEvent().getEventId());
        dto.setEventdt(entity.getEventDt());
        dto.setUser(convertUserEntityToDTO(entity.getUser()));
        dto.setEvent(convertEventEntityToDTO(entity.getEvent()));
        return dto;
    }

    /**
     * Book event using username and event name
     */
    public BookEvent bookEventByName(String username, String eventName) throws UserNotFoundException, BookEventException {
        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        EventEntitiy event = eventRepo.findByEventName(eventName)
                .orElseThrow(() -> new BookEventException("Event not found with name: " + eventName));

        // Validate booking rules
        long bookingCount = bookEventRepo.countByEvent(event);
        boolean userAlreadyBooked = bookEventRepo.existsByEventAndUser(event, user);

        if (bookingCount >= event.getLimit()) {
            throw new BookEventException("Cannot book event: Event is fully booked.");
        }
        if (userAlreadyBooked) {
            throw new BookEventException("User already booked this event.");
        }

        // Check for event date/time conflict
        List<BookEventEntity> userBookings = bookEventRepo.findByUser(user);
        for (BookEventEntity booked : userBookings) {
            EventEntitiy bookedEvent = booked.getEvent();
            boolean isSameDate = bookedEvent.getDate().equals(event.getDate());
            boolean isTimeOverlap = event.getStartTime().isBefore(bookedEvent.getEndTime()) &&
                                    event.getEndTime().isAfter(bookedEvent.getStartTime());
            if (isSameDate && isTimeOverlap) {
                throw new BookEventException("Cannot book event: Conflict with another event on the same date/time.");
            }
        }

        // Proceed with booking
        BookEventEntity bookEvent = new BookEventEntity();
        bookEvent.setEvent(event);
        bookEvent.setUser(user);
        bookEvent.setEventDt(LocalDate.now());

        BookEventEntity saved = bookEventRepo.save(bookEvent);
        return convertToDTO(saved);
    }

    /**
     * Book event using IDs (userId, eventId)
     */
    public BookEvent bookEvent(BookEvent booking) throws UserNotFoundException, BookEventException {
        UserEntity user = userRepo.findById(booking.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + booking.getUserId()));

        EventEntitiy event = eventRepo.findById(booking.getEventId())
                .orElseThrow(() -> new BookEventException("Event not found with ID: " + booking.getEventId()));

        long bookingCount = bookEventRepo.countByEvent(event);
        boolean userAlreadyBooked = bookEventRepo.existsByEventAndUser(event, user);

        if (bookingCount >= event.getLimit()) {
            throw new BookEventException("Cannot book event: Event is fully booked.");
        }
        if (userAlreadyBooked) {
            throw new BookEventException("User already booked this event.");
        }

        List<BookEventEntity> userBookings = bookEventRepo.findByUser(user);
        for (BookEventEntity booked : userBookings) {
            EventEntitiy bookedEvent = booked.getEvent();
            boolean isSameDate = bookedEvent.getDate().equals(event.getDate());
            boolean isTimeOverlap = event.getStartTime().isBefore(bookedEvent.getEndTime()) &&
                                    event.getEndTime().isAfter(bookedEvent.getStartTime());
            if (isSameDate && isTimeOverlap) {
                throw new BookEventException("Cannot book event: Conflict with another event on the same date/time.");
            }
        }

        BookEventEntity newBooking = new BookEventEntity();
        newBooking.setUser(user);
        newBooking.setEvent(event);
        newBooking.setEventDt(LocalDate.now());

        BookEventEntity saved = bookEventRepo.save(newBooking);
        return convertToDTO(saved);
    }

    public List<BookEvent> getBookingsByUserId(int userId) {
        return bookEventRepo.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookEvent> getBookingsByEventId(int eventId) {
        return bookEventRepo.findByEventId(eventId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookEvent> getBookingsByDate(LocalDate date) {
        return bookEventRepo.findByEventDt(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookEvent> getAllBookings() {
        return bookEventRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteBooking(int bookId) throws BookNotFoundException {
        Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
        if (optionalBooking.isEmpty()) {
            throw new BookNotFoundException("Booking not found with ID: " + bookId);
        }
        bookEventRepo.deleteById(bookId);
    }

    public BookEvent updateBooking(int bookId, BookEvent updatedBooking) throws BookNotFoundException {
        BookEventEntity existingBooking = bookEventRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Booking not found with ID: " + bookId));

        UserEntity user = userRepo.findById(updatedBooking.getUserId())
                .orElseThrow(() -> new BookNotFoundException("User not found with ID: " + updatedBooking.getUserId()));
        EventEntitiy event = eventRepo.findById(updatedBooking.getEventId())
                .orElseThrow(() -> new BookNotFoundException("Event not found with ID: " + updatedBooking.getEventId()));

        existingBooking.setUser(user);
        existingBooking.setEvent(event);
        existingBooking.setEventDt(updatedBooking.getEventdt() != null ? updatedBooking.getEventdt() : existingBooking.getEventDt());

        BookEventEntity saved = bookEventRepo.save(existingBooking);
        return convertToDTO(saved);
    }

    public List<BookEvent> getBookingDetailsByUsername(String username) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return bookEventRepo.findByUser(userEntity).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public BookEvent getBookingById(int bookId) throws BookNotFoundException {
        BookEventEntity booking = bookEventRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Booking not found with ID: " + bookId));

        return convertToDTO(booking);
    }
    
}
