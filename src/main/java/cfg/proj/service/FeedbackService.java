package cfg.proj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfg.proj.DTO.Feedback;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.Entities.FeedbackEntity;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.EventRepository;
import cfg.proj.repos.FeedbackRepository;
import cfg.proj.repos.UserRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EventRepository eventRepo;

    public FeedbackEntity addFeedback(int userId, int eventId, Feedback feedback2) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(userId);
        Optional<EventEntitiy> event = eventRepo.findById(eventId);

        if (user.isPresent() && event.isPresent()) {
            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setEfid(feedback2.getFid());
            feedback.setComment(feedback2.getComment());
            feedback.setRating(feedback2.getRating());
            feedback.setUser(user.get());
            feedback.setEvent(event.get());
            return feedbackRepo.save(feedback);
        } else {
            throw new UserNotFoundException("User or Event not found");
        }
    }

    public FeedbackEntity getFeedbackById(int feedbackId) {
        return feedbackRepo.findById(feedbackId)
            .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));
    }

    public List<FeedbackEntity> getAllFeedbacks() {
        return feedbackRepo.findAll();
    }

    public List<FeedbackEntity> getFeedbacksByUserId(int userId) {
        return feedbackRepo.findByUserUserId(userId);
    }

    public List<FeedbackEntity> getFeedbacksByEventId(int eventId) {
        return feedbackRepo.findByEventEventId(eventId);
    }

    public void deleteFeedback(int feedbackId) {
        if (feedbackRepo.existsById(feedbackId)) {
            feedbackRepo.deleteById(feedbackId);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }

    public FeedbackEntity updateFeedback(int feedbackId, FeedbackEntity updatedFeedback) {
        Optional<FeedbackEntity> existing = feedbackRepo.findById(feedbackId);
        if (existing.isPresent()) {
            FeedbackEntity feedback = existing.get();
            feedback.setRating(updatedFeedback.getRating());
            feedback.setComment(updatedFeedback.getComment());
            return feedbackRepo.save(feedback);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }
}
