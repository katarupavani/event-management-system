package cfg.proj.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.FeedbackEntity;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Integer> {

	List<FeedbackEntity> findByEventEventId(int eventId);

	List<FeedbackEntity> findByUserUserId(int userId);

	Optional<FeedbackEntity> findByUser_UserIdAndEvent_EventId(int userId, int eventId);

	



}
