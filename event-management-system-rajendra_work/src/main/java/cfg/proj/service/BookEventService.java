package cfg.proj.service;

import java.time.LocalDate;
import java.util.List;

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
import cfg.proj.exceptions.EventNotFoundException;
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

    // Helpers
    private User convertUserEntityToDTO(UserEntity userEntity) {
        User user = new User();
        user.setUserId(userEntity.getUserId());
        user.setUsername(userEntity.getUserName());
        user.setEmail(userEntity.getEmail());
        user.setRole(userEntity.getRole());
        return user;
    }

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

    // Check for time conflicts
    private boolean hasTimeConflict(EventEntitiy event, List<BookEventEntity> userBookings) {
        return userBookings.stream().anyMatch(booked -> {
            EventEntitiy bookedEvent = booked.getEvent();
            boolean sameDate = bookedEvent.getDate().equals(event.getDate());
            boolean overlap = event.getStartTime().isBefore(bookedEvent.getEndTime()) &&
                              event.getEndTime().isAfter(bookedEvent.getStartTime());
            return sameDate && overlap;
        });
    }

    // Book event using username & event name
    public BookEvent bookEventByName(String username, String eventName) 
            throws UserNotFoundException, EventNotFoundException, BookEventException {

        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        EventEntitiy event = eventRepo.findByEventName(eventName)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventName));

        validateBooking(user, event);

        BookEventEntity booking = new BookEventEntity();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setEventDt(LocalDate.now());

        return convertToDTO(bookEventRepo.save(booking));
    }

    // Book event using IDs
    public BookEvent bookEvent(BookEvent booking) 
            throws UserNotFoundException, EventNotFoundException, BookEventException {

        UserEntity user = userRepo.findById(booking.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + booking.getUserId()));

        EventEntitiy event = eventRepo.findById(booking.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + booking.getEventId()));

        validateBooking(user, event);

        BookEventEntity newBooking = new BookEventEntity();
        newBooking.setUser(user);
        newBooking.setEvent(event);
        newBooking.setEventDt(LocalDate.now());

        return convertToDTO(bookEventRepo.save(newBooking));
    }

    // Validate booking rules
    private void validateBooking(UserEntity user, EventEntitiy event) throws BookEventException {
        long bookingCount = bookEventRepo.countByEvent(event);
        boolean alreadyBooked = bookEventRepo.existsByEventAndUser(event, user);

        if (bookingCount >= event.getLimit()) {
            throw new BookEventException("Event is fully booked.");
        }
        if (alreadyBooked) {
            throw new BookEventException("User already booked this event.");
        }
        if (hasTimeConflict(event, bookEventRepo.findByUser(user))) {
            throw new BookEventException("Booking conflict: overlaps with another event.");
        }
    }

    // Queries
    public List<BookEvent> getBookingsByUserId(int userId) {
        return bookEventRepo.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<BookEvent> getBookingsByEventId(int eventId) {
        return bookEventRepo.findByEventId(eventId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<BookEvent> getBookingsByDate(LocalDate date) {
        return bookEventRepo.findByEventDt(date).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<BookEvent> getAllBookings() {
        return bookEventRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void deleteBooking(int bookId) throws BookNotFoundException {
        BookEventEntity booking = bookEventRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Booking not found with ID: " + bookId));
        bookEventRepo.delete(booking);
    }

    public BookEvent updateBooking(int bookId, BookEvent updatedBooking) throws BookNotFoundException {
        BookEventEntity existing = bookEventRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Booking not found with ID: " + bookId));

        UserEntity user = userRepo.findById(updatedBooking.getUserId())
                .orElseThrow(() -> new BookNotFoundException("User not found: " + updatedBooking.getUserId()));

        EventEntitiy event = eventRepo.findById(updatedBooking.getEventId())
                .orElseThrow(() -> new BookNotFoundException("Event not found: " + updatedBooking.getEventId()));

        existing.setUser(user);
        existing.setEvent(event);
        existing.setEventDt(updatedBooking.getEventdt() != null ? updatedBooking.getEventdt() : existing.getEventDt());

        return convertToDTO(bookEventRepo.save(existing));
    }

    public List<BookEvent> getBookingDetailsByUsername(String username) throws UserNotFoundException {
        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        return bookEventRepo.findByUser(user).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public BookEvent getBookingById(int bookId) throws BookNotFoundException {
        return convertToDTO(
                bookEventRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Booking not found: " + bookId))
        );
    }
}
