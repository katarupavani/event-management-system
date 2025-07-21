package cfg.proj.DTO;

import lombok.Data;

@Data
public class Feedback {
	
	private int fid;
	private int eventid;
	private int userid;
	private int rating;
	private String comment;
	
	

}
