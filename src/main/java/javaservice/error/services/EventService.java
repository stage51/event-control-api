package javaservice.error.services;

import javaservice.error.dtos.EventRequest;
import javaservice.error.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends CrudService<Event, EventRequest>{
    Page<Event> getAllEvents(int page, int size, String sortBy,
                                    String eventTypeComment, String controllerSerialNumber,
                                    String comment, String controllerVehicleNumber,
                                    String startDate, String endDate);
    List<Event> getEventsBetween(String start, String end);
}

