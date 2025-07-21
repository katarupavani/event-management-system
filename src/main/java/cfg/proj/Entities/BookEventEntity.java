package cfg.proj.Entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "event", name = "book_event")
public class BookEventEntity {

	@Id

	private int bookId;

	@Column
	private LocalDate eventDt;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	@JsonBackReference
	private UserEntity user;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "eventId")
	@JsonBackReference
	private EventEntitiy event;

}
