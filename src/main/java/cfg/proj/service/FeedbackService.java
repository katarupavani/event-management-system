package cfg.proj.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Convert Entity to DTO
    private Feedback toDto(FeedbackEntity entity) {
        Feedback dto = new Feedback();
        dto.setFid(entity.getEfid());
        dto.setComment(entity.getComment());
        dto.setRating(entity.getRating());

        dto.setUserid(entity.getUser().getUserId());
        dto.setUserName(entity.getUser().getUserName()); // <-- add this

        dto.setEventid(entity.getEvent().getEventId());
        dto.setEventName(entity.getEvent().getEventName()); // <-- add this

        return dto;
    }
    // Convert DTO to Entity (partial for update or new)
    private FeedbackEntity toEntity(Feedback dto, UserEntity user, EventEntitiy event) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setEfid(dto.getFid());
        entity.setComment(dto.getComment());
        entity.setRating(dto.getRating());
        entity.setUser(user);
        entity.setEvent(event);
        return entity;
    }

//    public Feedback addFeedback(Feedback feedbackDto) throws UserNotFoundException {
//        Optional<UserEntity> user = userRepo.findById(feedbackDto.getUserid());
//        Optional<EventEntitiy> event = eventRepo.findById(feedbackDto.getEventid());
//
//        if (user.isPresent() && event.isPresent()) {
//            FeedbackEntity feedbackEntity = toEntity(feedbackDto, user.get(), event.get());
//            FeedbackEntity saved = feedbackRepo.save(feedbackEntity);
//            return toDto(saved);
//        } else {
//            throw new UserNotFoundException("User or Event not found");
//        }
//    }
    
    public Feedback addFeedback(Feedback feedbackDto) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(feedbackDto.getUserid());
        Optional<EventEntitiy> event = eventRepo.findById(feedbackDto.getEventid());

        if (user.isPresent() && event.isPresent()) {
            // Check if feedback already exists for this user and event
            boolean alreadyExists = feedbackRepo.existsByUserUserIdAndEventEventId(feedbackDto.getUserid(), feedbackDto.getEventid());
            
            if (alreadyExists) {
                throw new RuntimeException("Feedback already submitted for this event by the user");
            }

            FeedbackEntity feedbackEntity = toEntity(feedbackDto, user.get(), event.get());
            FeedbackEntity saved = feedbackRepo.save(feedbackEntity);
            return toDto(saved);
        } else {
            throw new UserNotFoundException("User or Event not found");
        }
    }


    public Feedback getFeedbackById(int feedbackId) {
        FeedbackEntity entity = feedbackRepo.findById(feedbackId)
            .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));
        return toDto(entity);
    }

    public List<Feedback> getAllFeedbacks() {
        List<FeedbackEntity> entities = feedbackRepo.findAll();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Feedback> getFeedbacksByUserId(int userId) {
        List<FeedbackEntity> entities = feedbackRepo.findByUserUserId(userId);
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Feedback> getFeedbacksByEventId(int eventId) {
        List<FeedbackEntity> entities = feedbackRepo.findByEventEventId(eventId);
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteFeedback(int feedbackId) {
        if (feedbackRepo.existsById(feedbackId)) {
            feedbackRepo.deleteById(feedbackId);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }

    public Feedback updateFeedback(int feedbackId, Feedback updatedFeedbackDto) {
        Optional<FeedbackEntity> existing = feedbackRepo.findById(feedbackId);
        if (existing.isPresent()) {
            FeedbackEntity feedbackEntity = existing.get();
            feedbackEntity.setComment(updatedFeedbackDto.getComment());
            feedbackEntity.setRating(updatedFeedbackDto.getRating());
            FeedbackEntity saved = feedbackRepo.save(feedbackEntity);
            return toDto(saved);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }
}