package cfg.proj.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "event", name = "euser")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(unique = true, nullable = false)
	private String userName;

	@Column
	private String email;

	@Column
	private String password;
	@Column
	private String role;

	@OneToMany(mappedBy = "user")
	private List<BookEventEntity> bookedEvents;
	
	

}
