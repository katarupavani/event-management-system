package cfg.proj.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cfg.proj.Entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	 @Query("SELECT u FROM UserEntity u WHERE u.userName = :username AND u.password = :password")
	    Optional<UserEntity> findByUserNameAndPassword(String username, String password);
	 
	 Optional<UserEntity> findByUserName(String username);


}
