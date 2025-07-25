package cfg.proj.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.BookEvent;
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

	public BookEventEntity bookEvent(BookEvent booking) throws UserNotFoundException, BookEventException {
	    Optional<EventEntitiy> eventOpt = eventRepo.findById(booking.getEventId());
	    Optional<UserEntity> userOpt = userRepo.findById(booking.getUserId());

	    if (eventOpt.isPresent() && userOpt.isPresent()) {
	        EventEntitiy event = eventOpt.get();
	        UserEntity user = userOpt.get();

	        long bookingCount = bookEventRepo.countByEvent(event);
	        boolean userAlreadyBooked = bookEventRepo.existsByEventAndUser(event, user);

	        if (bookingCount >= event.getLimit() || userAlreadyBooked) {
	            throw new BookEventException("Cannot book event: Either event is full or user has already booked.");
	        }

	        // Check date + time conflict
	        List<BookEventEntity> userBookings = bookEventRepo.findByUser(user);

	        for (BookEventEntity booked : userBookings) {
	            EventEntitiy bookedEvent = booked.getEvent();

	            boolean isSameDate = bookedEvent.getDate().equals(event.getDate());
	            boolean isTimeOverlap = event.getStartTime().isBefore(bookedEvent.getEndTime()) &&
	                                    event.getEndTime().isAfter(bookedEvent.getStartTime());

	            if (isSameDate && isTimeOverlap) {
	                throw new BookEventException("Cannot book event: Conflict with another event on same date and time.");
	            }
	        }

	        // No conflict - proceed
	        BookEventEntity bookEvent = new BookEventEntity();
	        bookEvent.setBookId(booking.getBookId());
	        bookEvent.setEvent(event);
	        bookEvent.setUser(user);
	        bookEvent.setEventDt(LocalDate.now());
	        return bookEventRepo.save(bookEvent);
	    } else {
	        throw new UserNotFoundException("User or event does not exist");
	    }
	}


	public BookEventEntity getBookingById(int bookId) throws BookNotFoundException {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			return optionalBooking.get();
		} else {
			throw new BookNotFoundException("Booking not found with ID: " + bookId);
		}
	}

	public List<BookEventEntity> getAllBookings() {
		return bookEventRepo.findAll();
	}

	public void deleteBooking(int bookId) throws BookNotFoundException {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			bookEventRepo.deleteById(bookId);
		} else {
			throw new BookNotFoundException("Booking not found with ID: " + bookId);
		}
	}

	public BookEventEntity updateBooking(int bookId, BookEventEntity updatedBooking) throws BookNotFoundException {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			BookEventEntity existingBooking = optionalBooking.get();

			existingBooking.getUser().setUserId(updatedBooking.getUser().getUserId());
			existingBooking.getEvent().setEventId(updatedBooking.getEvent().getEventId());
			existingBooking.setEventDt(updatedBooking.getEventDt());

			return bookEventRepo.save(existingBooking);
		} else {
			throw new BookNotFoundException("Booking not found with ID: " + bookId);
		}
	}

	public List<BookEventEntity> getBookingsByUserId(int userId) {
		return bookEventRepo.findByUserId(userId);
	}

	public List<BookEventEntity> getBookingsByEventId(int eventId) {
		return bookEventRepo.findByEventId(eventId);
	}

	public List<BookEventEntity> getBookingsByDate(LocalDate date) {
		return bookEventRepo.findByEventDt(date);
	}

}
