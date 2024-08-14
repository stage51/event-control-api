package javaservice.error.utils;

import javaservice.error.dtos.EventRequest;
import javaservice.error.exceptions.ControllerByGuidNotFoundException;
import javaservice.error.exceptions.EntityNotFoundException;
import javaservice.error.exceptions.EventMappingException;
import javaservice.error.exceptions.InvalidEventTimeException;
import javaservice.error.models.Controller;
import javaservice.error.models.Event;
import javaservice.error.models.EventType;
import javaservice.error.services.ControllerService;
import javaservice.error.services.EventTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class EventMapper {

    private EventTypeService eventTypeService;
    private ControllerService controllerService;

    public EventMapper(EventTypeService eventTypeService, ControllerService controllerService) {
        this.eventTypeService = eventTypeService;
        this.controllerService = controllerService;
    }

    @Autowired
    public void setEventTypeService(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    public Event map(EventRequest eventRequest) throws EntityNotFoundException, ControllerByGuidNotFoundException, InvalidEventTimeException{
        try {
            Event event = new Event();
            event.setEventType(validateEventType(eventRequest.getEventType()));
            event.setController(validateController(eventRequest.getController()));
            event.setEventTime(validateEventTime(LocalDateTime.parse(eventRequest.getEventTime(), EventRequest.formatter)));
            event.setServerTime(LocalDateTime.now());
            event.setComment(eventRequest.getComment());
            return event;
        } catch (Exception e) {
            log.error("Failed to map event");
            throw new EventMappingException("Unable to map request. " + e.getMessage());
        }
    }
    private EventType validateEventType(Long eventType){
        Optional<EventType> entity = eventTypeService.read(eventType);
        if (entity.isEmpty()){
            throw new EntityNotFoundException("Event type", eventType);
        }
        return entity.get();
    }
    private Controller validateController(UUID controller){
        Optional<Controller> entity = controllerService.getByGuid(controller);
        if (entity.isEmpty()){
            throw new ControllerByGuidNotFoundException(controller);
        }
        return entity.get();
    }
    private LocalDateTime validateEventTime(LocalDateTime time){
        if (time.isAfter(LocalDateTime.now()) || Duration.between(time, LocalDateTime.now()).toDays() >= 365){
            throw new InvalidEventTimeException("The event time is later than the server time or more than a year has passed since the event was created");
        }
        return time;
    }
}