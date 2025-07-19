package cfg.proj.repos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.BookEventEntity;

@Repository
public interface BookEventRepository extends JpaRepository<BookEventEntity, Integer> {

	@Query("from BookEventEntity b where b.user.userId =:userId")
	List<BookEventEntity> findByUserId(int userId);

	@Query("from BookEventEntity b where b.event.eventId =:eventId")
	List<BookEventEntity> findByEventId(int eventId);

	List<BookEventEntity> findByEventDt(LocalDate date);

}
