package cfg.proj.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.OrganizerEntity;

@Repository
public interface OrganizerRepository extends JpaRepository<OrganizerEntity, Integer> {

}
