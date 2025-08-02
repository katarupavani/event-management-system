package cfg.proj.DTO;

import java.time.LocalDate;

import cfg.proj.Entities.UserEntity;
import lombok.Data;
@Data
public class BookEvent {
	
	private int bookId;
	private int userId;
	private  int eventId;
	private LocalDate eventdt;
	  private User user;
	  private Event event;

}
