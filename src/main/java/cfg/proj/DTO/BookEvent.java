package cfg.proj.DTO;

import java.time.LocalDate;

import lombok.Data;
@Data
public class BookEvent {
	
	private int bookId;
	private int userId;
	private  int eventId;
	private LocalDate eventdt;

}
