package cfg.proj.Entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookId;

	@Column
	private LocalDate eventDt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	@JsonBackReference
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "eventId")
	//@JsonBackReference
	private EventEntitiy event;

}
