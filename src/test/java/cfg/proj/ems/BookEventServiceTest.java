package cfg.proj.ems;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import cfg.proj.DTO.BookEvent;
import cfg.proj.Entities.BookEventEntity;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.BookEventRepository;
import cfg.proj.repos.EventRepository;
import cfg.proj.repos.UserRepository;
import cfg.proj.service.BookEventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookEventServiceTest {
	@Mock
	private BookEventRepository bookEventRepo;

	@Mock
	private UserRepository userRepo;

	@Mock
	private EventRepository eventRepo;

	@InjectMocks
	private BookEventService bookEventService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	

	@Test
	public void testBookEvent_UserOrEventNotFound() {
		BookEvent booking = new BookEvent();
		booking.setUserId(1);
		booking.setEventId(1);

		when(userRepo.findById(anyInt())).thenReturn(Optional.empty());
		when(eventRepo.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> bookEventService.bookEvent(booking));
	}

	@Test
	public void testGetBookingById_Found() {
		BookEventEntity booking = new BookEventEntity();
		booking.setBookId(1);

		when(bookEventRepo.findById(1)).thenReturn(Optional.of(booking));

		BookEventEntity result = bookEventService.getBookingById(1);
		assertEquals(1, result.getBookId());
	}

	@Test
	public void testGetBookingById_NotFound() {
		when(bookEventRepo.findById(1)).thenReturn(Optional.empty());

		Exception ex = assertThrows(RuntimeException.class, () -> bookEventService.getBookingById(1));
		assertTrue(ex.getMessage().contains("Booking not found"));
	}

	@Test
	public void testGetAllBookings() {
		List<BookEventEntity> bookings = List.of(new BookEventEntity(), new BookEventEntity());
		when(bookEventRepo.findAll()).thenReturn(bookings);

		List<BookEventEntity> result = bookEventService.getAllBookings();
		assertEquals(2, result.size());
	}

	@Test
	public void testDeleteBooking_Success() {
		BookEventEntity booking = new BookEventEntity();
		booking.setBookId(1);

		when(bookEventRepo.findById(1)).thenReturn(Optional.of(booking));
		doNothing().when(bookEventRepo).deleteById(1);

		assertDoesNotThrow(() -> bookEventService.deleteBooking(1));
		verify(bookEventRepo, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteBooking_NotFound() {
		when(bookEventRepo.findById(1)).thenReturn(Optional.empty());

		Exception ex = assertThrows(RuntimeException.class, () -> bookEventService.deleteBooking(1));
		assertTrue(ex.getMessage().contains("Booking not found"));
	}

	@Test
	public void testUpdateBooking_Success() {
		BookEventEntity existing = new BookEventEntity();
		existing.setBookId(1);
		UserEntity existingUser = new UserEntity();
		existingUser.setUserId(10);
		existing.setUser(existingUser);
		EventEntitiy existingEvent = new EventEntitiy();
		existingEvent.setEventId(20);
		existing.setEvent(existingEvent);
		existing.setEventDt(LocalDate.now());

		BookEventEntity updated = new BookEventEntity();
		UserEntity updatedUser = new UserEntity();
		updatedUser.setUserId(15);
		updated.setUser(updatedUser);
		EventEntitiy updatedEvent = new EventEntitiy();
		updatedEvent.setEventId(25);
		updated.setEvent(updatedEvent);
		updated.setEventDt(LocalDate.now().plusDays(1));

		when(bookEventRepo.findById(1)).thenReturn(Optional.of(existing));
		when(bookEventRepo.save(any(BookEventEntity.class))).thenAnswer(i -> i.getArgument(0));

		BookEventEntity result = bookEventService.updateBooking(1, updated);

		assertEquals(15, result.getUser().getUserId());
		assertEquals(25, result.getEvent().getEventId());
		assertEquals(LocalDate.now().plusDays(1), result.getEventDt());
	}

	@Test
	public void testUpdateBooking_NotFound() {
		when(bookEventRepo.findById(1)).thenReturn(Optional.empty());

		Exception ex = assertThrows(RuntimeException.class,
				() -> bookEventService.updateBooking(1, new BookEventEntity()));
		assertTrue(ex.getMessage().contains("Booking not found"));
	}

	@Test
	public void testGetBookingsByUserId() {
		List<BookEventEntity> bookings = List.of(new BookEventEntity());
		when(bookEventRepo.findByUserId(10)).thenReturn(bookings);

		List<BookEventEntity> result = bookEventService.getBookingsByUserId(10);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetBookingsByEventId() {
		List<BookEventEntity> bookings = List.of(new BookEventEntity());
		when(bookEventRepo.findByEventId(20)).thenReturn(bookings);

		List<BookEventEntity> result = bookEventService.getBookingsByEventId(20);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetBookingsByDate() {
		LocalDate date = LocalDate.now();
		List<BookEventEntity> bookings = List.of(new BookEventEntity());
		when(bookEventRepo.findByEventDt(date)).thenReturn(bookings);

		List<BookEventEntity> result = bookEventService.getBookingsByDate(date);
		assertEquals(1, result.size());
	}
}
