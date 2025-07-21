package cfg.proj.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "event", name = "euser")
public class UserEntity {

	@Id
	private int userId;

	@Column
	private String userName;

	@Column
	private String email;

	@Column
	private String password;

	@OneToMany(mappedBy = "user")
	private List<BookEventEntity> bookedEvents;

}
