package cfg.proj.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "event", name = "event_feedback")
public class FeedbackEntity {
	@Id
	private int efid;	

	@ManyToOne
	@JoinColumn(name="userId")
	@JsonBackReference
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name="eventId")
	@JsonBackReference
	private EventEntitiy event;
	
	
	@Column
	private int rating;
	
	@Column
	private String comment;
}
