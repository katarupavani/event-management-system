package cfg.proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cfg.proj.DTO.Feedback;
import cfg.proj.Entities.FeedbackEntity;
import cfg.proj.bo.ResponseData;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.service.FeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public ResponseData addFeedback(@RequestBody Feedback request) throws UserNotFoundException {
        ResponseData response = new ResponseData();
        try {
            FeedbackEntity saved = feedbackService.addFeedback(
                request.getUserid(),
                request.getEventid(),
                request
            );
            response.setStatus("success");
            response.setMessage("Feedback added successfully.");
            response.setData(saved);
        } catch (RuntimeException e) {
            response.setStatus("error");
            response.setMessage("Failed to add feedback: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseData getFeedbackById(@PathVariable int id) {
        ResponseData response = new ResponseData();
        try {
            FeedbackEntity feedback = feedbackService.getFeedbackById(id);
            response.setStatus("success");
            response.setMessage("Feedback fetched successfully.");
            response.setData(feedback);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Feedback not found with ID: " + id);
            response.setData(null);
        }
        return response;
    }

    @GetMapping
    public ResponseData getAllFeedbacks() {
        ResponseData response = new ResponseData();
        try {
            List<FeedbackEntity> feedbacks = feedbackService.getAllFeedbacks();
            response.setStatus("success");
            response.setMessage("All feedbacks fetched successfully.");
            response.setData(feedbacks);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching feedbacks: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/user/{userId}")
    public ResponseData getFeedbacksByUserId(@PathVariable int userId) {
        ResponseData response = new ResponseData();
        try {
            List<FeedbackEntity> feedbacks = feedbackService.getFeedbacksByUserId(userId);
            response.setStatus("success");
            response.setMessage("Feedbacks fetched successfully for user.");
            response.setData(feedbacks);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching feedbacks for user: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/event/{eventId}")
    public ResponseData getFeedbacksByEventId(@PathVariable int eventId) {
        ResponseData response = new ResponseData();
        try {
            List<FeedbackEntity> feedbacks = feedbackService.getFeedbacksByEventId(eventId);
            response.setStatus("success");
            response.setMessage("Feedbacks fetched successfully for event.");
            response.setData(feedbacks);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error fetching feedbacks for event: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @PutMapping("/update/{id}")
    public ResponseData updateFeedback(@PathVariable int id, @RequestBody FeedbackEntity updatedFeedback) {
        ResponseData response = new ResponseData();
        try {
            FeedbackEntity updated = feedbackService.updateFeedback(id, updatedFeedback);
            response.setStatus("success");
            response.setMessage("Feedback updated successfully.");
            response.setData(updated);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error updating feedback: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteFeedback(@PathVariable int id) {
        ResponseData response = new ResponseData();
        try {
            feedbackService.deleteFeedback(id);
            response.setStatus("success");
            response.setMessage("Feedback deleted successfully.");
            response.setData(null);
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Error deleting feedback: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }
}
