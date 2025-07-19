package cfg.proj.DTO;

import jakarta.persistence.IdClass;
import lombok.Data;

@Data
public class Organizer {

	private int id;
	private String username;
	private String email;
	private String password;
}
