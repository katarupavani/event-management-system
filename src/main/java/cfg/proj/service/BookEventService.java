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

	public BookEventEntity bookEvent(BookEvent booking) throws UserNotFoundException {
		Optional<EventEntitiy> event = eventRepo.findById(booking.getEventId());
		Optional<UserEntity> user = userRepo.findById(booking.getUserId());
		if (event.isPresent() && user.isPresent()) {
			BookEventEntity bookEvent = new BookEventEntity();
			bookEvent.setBookId(booking.getBookId());
			bookEvent.setEvent(event.get());
			bookEvent.setUser(user.get());
			bookEvent.setEventDt(LocalDate.now());
			bookEventRepo.save(bookEvent);
			System.out.println("User booked for event successfully...");
		} else {
			throw new UserNotFoundException("user or event does not exist");
		}
		return null;
	}

	public BookEventEntity getBookingById(int bookId) {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			return optionalBooking.get();
		} else {
			throw new RuntimeException("Booking not found with ID: " + bookId);
		}
	}

	public List<BookEventEntity> getAllBookings() {
		return bookEventRepo.findAll();
	}

	public void deleteBooking(int bookId) {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			bookEventRepo.deleteById(bookId);
		} else {
			throw new RuntimeException("Booking not found with ID: " + bookId);
		}
	}

	public BookEventEntity updateBooking(int bookId, BookEventEntity updatedBooking) {
		Optional<BookEventEntity> optionalBooking = bookEventRepo.findById(bookId);
		if (optionalBooking.isPresent()) {
			BookEventEntity existingBooking = optionalBooking.get();

			existingBooking.getUser().setUserId(updatedBooking.getUser().getUserId());
			existingBooking.getEvent().setEventId(updatedBooking.getEvent().getEventId());
			existingBooking.setEventDt(updatedBooking.getEventDt());

			return bookEventRepo.save(existingBooking);
		} else {
			throw new RuntimeException("Booking not found with ID: " + bookId);
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
