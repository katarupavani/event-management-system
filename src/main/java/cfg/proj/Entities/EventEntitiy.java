package cfg.proj.Entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "event", name = "event")
public class EventEntitiy {

    @Id
    private int eventId;

    @Column
    private String eventName;

    @Column
    private String category;

    @Column
    private String description;

    @Column
    private int limit;

    @Column
    private String location;

    @Column
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "event")
    private List<FeedbackEntity> feedbacks;
}
