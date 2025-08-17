package cfg.proj.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfg.proj.DTO.BookEvent;
import cfg.proj.Entities.BookEventEntity;
import cfg.proj.bo.ResponseData;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.service.BookEventService;

@RestController
@RequestMapping("/api/bookings")
public class BookEventController {

    @Autowired
    private BookEventService bookEventService;

    /**
     * Create booking by userId and eventId (Existing)
     */
    @PostMapping("/create")
    public ResponseData createBooking(@RequestBody BookEvent booking) {
        ResponseData response = new ResponseData();
        try {
            BookEvent created = bookEventService.bookEvent(booking);
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

    
    @PostMapping("/create-by-names")
    public ResponseData createBookingByNames(@RequestBody Map<String, String> request) {
        ResponseData response = new ResponseData();

        try {
            String username = request.get("username");
            String eventName = request.get("eventName");

            if (username == null || username.trim().isEmpty() || 
                eventName == null || eventName.trim().isEmpty()) {
                response.setStatus("error");
                response.setMessage("Username and Event Name are required and cannot be empty.");
                response.setData(null);
                return response;
            }

            BookEvent createdBooking = bookEventService.bookEventByName(username.trim(), eventName.trim());

            response.setStatus("success");
            response.setMessage("Booking created successfully using names.");
            response.setData(createdBooking);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error creating booking: " + e.getMessage());
            response.setData(null);
        }

        return response;
    }


    /**
     * Get booking by ID
     */
    @GetMapping("/book/{id}")
    public ResponseData getBookingById(@PathVariable("id") int bookId) {
        ResponseData response = new ResponseData();
        try {
            List<BookEvent> booking = bookEventService.getBookingsByEventId(bookId);
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

    /**
     * Get all bookings
     */
    @GetMapping
    public ResponseData getAllBookings() {
        ResponseData response = new ResponseData();
        try {
            List<BookEvent> bookings = bookEventService.getAllBookings();
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

    /**
     * Delete booking by ID
     */
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

    /**
     * Update booking by ID
     */
    @PutMapping("/update/{id}")
    public ResponseData updateBooking(@PathVariable("id") int bookId, @RequestBody BookEvent updatedBooking) {
        ResponseData response = new ResponseData();
        try {
            BookEvent updated = bookEventService.updateBooking(bookId, updatedBooking);
            response.setStatus("success");
            response.setMessage("Booking updated successfully.");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error updating booking: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    /**
     * Get bookings by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseData getBookingsByUserId(@PathVariable int userId) {
        ResponseData response = new ResponseData();
        try {
            List<BookEvent> bookings = bookEventService.getBookingsByUserId(userId);
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

    /**
     * Get bookings by event ID
     */
    @GetMapping("/event/{eventId}")
    public ResponseData getBookingsByEventId(@PathVariable int eventId) {
        ResponseData response = new ResponseData();
        try {
            List<BookEvent> bookings = bookEventService.getBookingsByEventId(eventId);
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

    /**
     * Get bookings by date
     */
    @GetMapping("/date/{date}")
    public ResponseData getBookingsByDate(@PathVariable String date) {
        ResponseData response = new ResponseData();
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<BookEvent> bookings = bookEventService.getBookingsByDate(localDate);
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
    @GetMapping("/username/{username}")
    public ResponseData getBookingDetailsByUsername(@PathVariable String username) {
        ResponseData response = new ResponseData();
        try {
            List<BookEvent> bookings = bookEventService.getBookingDetailsByUsername(username);
            response.setStatus("success");
            response.setMessage("Booking details fetched successfully.");
            response.setData(bookings);
        } catch (UserNotFoundException e) {
            response.setStatus("failed");
            response.setMessage(e.getMessage());
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("failed");
            response.setMessage("Error fetching booking details: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
    @GetMapping("/book/alternate/{id}")
    public ResponseData getBookingById1(@PathVariable("id") int bookId) {
        ResponseData response = new ResponseData();
        try {
            BookEvent booking = bookEventService.getBookingById(bookId);  // Now uses your new method
            response.setStatus("success");
            response.setMessage("Booking fetched successfully.");
            response.setData(booking);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Booking not found with ID: " + bookId + ". " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
    }
