package cfg.proj.ems;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import cfg.proj.DTO.Feedback;
import cfg.proj.Entities.EventEntitiy;
import cfg.proj.Entities.FeedbackEntity;
import cfg.proj.Entities.UserEntity;
import cfg.proj.exceptions.UserNotFoundException;
import cfg.proj.repos.EventRepository;
import cfg.proj.repos.FeedbackRepository;
import cfg.proj.repos.UserRepository;
import cfg.proj.service.FeedbackService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FeedbackTest {

    @Mock
    private FeedbackRepository feedbackRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFeedback_Success() throws UserNotFoundException {
        int userId = 1;
        int eventId = 2;

        Feedback feedbackDto = new Feedback();
        feedbackDto.setFid(100);
        feedbackDto.setComment("Great event!");
        feedbackDto.setRating(5);

        UserEntity user = new UserEntity();
        user.setUserId(userId);

        EventEntitiy event = new EventEntitiy();
        event.setEventId(eventId);

        FeedbackEntity savedFeedback = new FeedbackEntity();
        savedFeedback.setEfid(100);
        savedFeedback.setComment("Great event!");
        savedFeedback.setRating(5);
        savedFeedback.setUser(user);
        savedFeedback.setEvent(event);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepo.save(any(FeedbackEntity.class))).thenReturn(savedFeedback);

        FeedbackEntity result = feedbackService.addFeedback(userId, eventId, feedbackDto);

        assertNotNull(result);
        assertEquals(100, result.getEfid());
        assertEquals("Great event!", result.getComment());
        assertEquals(5, result.getRating());
        verify(feedbackRepo, times(1)).save(any(FeedbackEntity.class));
    }

    @Test
    void testAddFeedback_UserOrEventNotFound() {
        when(userRepo.findById(anyInt())).thenReturn(Optional.empty());
        when(eventRepo.findById(anyInt())).thenReturn(Optional.empty());

        Feedback feedbackDto = new Feedback();

        assertThrows(UserNotFoundException.class, () -> {
            feedbackService.addFeedback(1, 2, feedbackDto);
        });
    }

    @Test
    void testGetFeedbackById_Found() {
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setEfid(1);

        when(feedbackRepo.findById(1)).thenReturn(Optional.of(feedback));

        FeedbackEntity result = feedbackService.getFeedbackById(1);
        assertEquals(1, result.getEfid());
    }

    @Test
    void testGetFeedbackById_NotFound() {
        when(feedbackRepo.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            feedbackService.getFeedbackById(1);
        });
        assertTrue(ex.getMessage().contains("Feedback not found"));
    }

    @Test
    void testGetAllFeedbacks() {
        List<FeedbackEntity> feedbacks = List.of(new FeedbackEntity(), new FeedbackEntity());
        when(feedbackRepo.findAll()).thenReturn(feedbacks);

        List<FeedbackEntity> result = feedbackService.getAllFeedbacks();
        assertEquals(2, result.size());
    }

    @Test
    void testGetFeedbacksByUserId() {
        List<FeedbackEntity> feedbacks = List.of(new FeedbackEntity());
        when(feedbackRepo.findByUserUserId(1)).thenReturn(feedbacks);

        List<FeedbackEntity> result = feedbackService.getFeedbacksByUserId(1);
        assertEquals(1, result.size());
    }

    @Test
    void testGetFeedbacksByEventId() {
        List<FeedbackEntity> feedbacks = List.of(new FeedbackEntity());
        when(feedbackRepo.findByEventEventId(2)).thenReturn(feedbacks);

        List<FeedbackEntity> result = feedbackService.getFeedbacksByEventId(2);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteFeedback_Success() {
        when(feedbackRepo.existsById(1)).thenReturn(true);
        doNothing().when(feedbackRepo).deleteById(1);

        assertDoesNotThrow(() -> feedbackService.deleteFeedback(1));
        verify(feedbackRepo, times(1)).deleteById(1);
    }

    @Test
    void testDeleteFeedback_NotFound() {
        when(feedbackRepo.existsById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            feedbackService.deleteFeedback(1);
        });
        assertTrue(ex.getMessage().contains("Feedback not found"));
    }

    @Test
    void testUpdateFeedback_Success() {
        FeedbackEntity existing = new FeedbackEntity();
        existing.setEfid(1);
        existing.setComment("Old comment");
        existing.setRating(3);

        FeedbackEntity updated = new FeedbackEntity();
        updated.setComment("New comment");
        updated.setRating(5);

        when(feedbackRepo.findById(1)).thenReturn(Optional.of(existing));
        when(feedbackRepo.save(any(FeedbackEntity.class))).thenAnswer(i -> i.getArgument(0));

        FeedbackEntity result = feedbackService.updateFeedback(1, updated);

        assertEquals("New comment", result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void testUpdateFeedback_NotFound() {
        when(feedbackRepo.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            feedbackService.updateFeedback(1, new FeedbackEntity());
        });
        assertTrue(ex.getMessage().contains("Feedback not found"));
    }
}


