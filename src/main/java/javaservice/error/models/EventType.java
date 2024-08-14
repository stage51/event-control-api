package javaservice.error.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "event_types")
public class EventType extends BaseEntity{
    private Long eventCode;
    private String comment;
}
