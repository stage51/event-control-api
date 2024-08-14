package javaservice.error.services;

import javaservice.error.models.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventTypeService extends CrudService<EventType, EventType> {
    Optional<EventType> getByEventCode(Long code);
    boolean generateTypes();
    Page<EventType> getAllEventTypes(Pageable pageable);
}
