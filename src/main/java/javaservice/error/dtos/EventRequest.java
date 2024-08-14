package javaservice.error.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@ToString
public class EventRequest {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private UUID controller;
    private Long eventType;
    private String eventTime;
    private String comment;

    public EventRequest(UUID controller, Long eventType, String eventTime, String comment) {
        this.controller = controller;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.comment = comment;
    }
}
