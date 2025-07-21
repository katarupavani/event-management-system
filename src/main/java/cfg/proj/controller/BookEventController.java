package cfg.proj.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cfg.proj.DTO.BookEvent;
import cfg.proj.Entities.BookEventEntity;
import cfg.proj.bo.ResponseData;
import cfg.proj.service.BookEventService;

@RestController
@RequestMapping("/bookings")
public class BookEventController {

    @Autowired
    private BookEventService bookEventService;

    @PostMapping("/create")
    public ResponseData createBooking(@RequestBody BookEvent booking) {
        ResponseData response = new ResponseData();
        try {
            BookEventEntity created = bookEventService.bookEvent(booking);
            response.setStatus("success");
            response.setMessage("Booking created successfully.");
            response.setData(created);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error creating booking: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData getBookingById(@PathVariable("id") int bookId) {
        ResponseData response = new ResponseData();
        try {
            BookEventEntity booking = bookEventService.getBookingById(bookId);
            response.setStatus("success");
            response.setMessage("Booking fetched successfully.");
            response.setData(booking);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Booking not found with ID: " + bookId);
            response.setData(null);
        }
        return response;
    }

    @GetMapping
    public ResponseData getAllBookings() {
        ResponseData response = new ResponseData();
        try {
            List<BookEventEntity> bookings = bookEventService.getAllBookings();
            response.setStatus("success");
            response.setMessage("All bookings fetched successfully.");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching bookings: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteBooking(@PathVariable("id") int bookId) {
        ResponseData response = new ResponseData();
        try {
            bookEventService.deleteBooking(bookId);
            response.setStatus("success");
            response.setMessage("Booking deleted successfully.");
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error deleting booking: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/user/{userId}")
    public ResponseData getBookingsByUserId(@PathVariable int userId) {
        ResponseData response = new ResponseData();
        try {
            List<BookEventEntity> bookings = bookEventService.getBookingsByUserId(userId);
            response.setStatus("success");
            response.setMessage("Bookings fetched successfully for user.");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching user bookings: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/event/{eventId}")
    public ResponseData getBookingsByEventId(@PathVariable int eventId) {
        ResponseData response = new ResponseData();
        try {
            List<BookEventEntity> bookings = bookEventService.getBookingsByEventId(eventId);
            response.setStatus("success");
            response.setMessage("Bookings fetched successfully for event.");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching event bookings: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/date/{date}")
    public ResponseData getBookingsByDate(@PathVariable String date) {
        ResponseData response = new ResponseData();
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<BookEventEntity> bookings = bookEventService.getBookingsByDate(localDate);
            response.setStatus("success");
            response.setMessage("Bookings fetched successfully for date.");
            response.setData(bookings);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Invalid date format or error: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
