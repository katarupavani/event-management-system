package cfg.proj.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class Event {
	
	private int eventId;
	private String eventName;
	private String category;
	private String description;
	private int limit;
	private String location;
	private LocalDate date;
	
	private LocalTime startTime;
	private LocalTime endTime;
}
