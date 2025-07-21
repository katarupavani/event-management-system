package cfg.proj.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
