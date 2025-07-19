package cfg.proj.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.EventEntitiy;
@Repository
public interface EventRepository extends JpaRepository<EventEntitiy, Integer>{
	
	List<EventEntitiy> findByCategory(String category);
    List<EventEntitiy> findByDateAfter(LocalDate date);
    List<EventEntitiy> findByLocationIgnoreCase(String location);
	Optional<EventEntitiy> findById(int eventId);

}
