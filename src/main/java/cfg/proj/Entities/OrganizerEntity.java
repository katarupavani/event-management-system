package cfg.proj.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema="event",name="organizer")
public class OrganizerEntity {
	@Id
	private int id;
	@Column
	private String username;
	@Column
	private String email;
	@Column
	private String password;
}
