package cfg.proj.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Event {
	
	private int event_id;
	private String event_name;
	private String category;
	private String description;
	private int limit;
	private String location;
	private LocalDate date;

}
