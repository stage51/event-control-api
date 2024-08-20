package javaservice.error.services.impl;

import javaservice.error.dtos.EventRequest;
import javaservice.error.exceptions.EntityNotFoundException;
import javaservice.error.exceptions.EventMappingException;
import javaservice.error.models.Event;
import javaservice.error.repos.EventRepository;
import javaservice.error.services.EventService;
import javaservice.error.utils.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private EventMapper eventMapper;

    private EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Autowired
    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }
    @Override
    public Event create(EventRequest eventRequest) throws EventMappingException{
        /*
        if (read(entity.getId()).isPresent()) {
            return false;
        }
        */
        Event entity = eventMapper.map(eventRequest);
        log.info("Event created");
        return eventRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<Event> read(Long id) {
        Optional<Event> entity = Optional.of(eventRepository.findById(id)).orElse(Optional.empty());
        if (entity.isEmpty()){
            log.error("Failed to read event with id " + id);
            throw new EntityNotFoundException("Event", id);
        }
        log.info("Event read with id " + id);
        return entity;
    }

    @Override
    public Event update(Event entity) {
        if (read(entity.getId()).isEmpty()) {
            log.error("Failed to update event with id " + entity.getId());
            throw new EntityNotFoundException("Event", entity.getId());
        }
        log.info("Event updated with id " + entity.getId());
        return eventRepository.saveAndFlush(entity);
    }

    @Override
    public boolean delete(Long id) {
        Optional<Event> entity = read(id);
        if (read(id).isEmpty()) {
            log.error("Failed to delete event with id " + id);
            throw new EntityNotFoundException("Event", id);
        }
        log.info("Event created with id " + id);
        eventRepository.delete(entity.get());
        return true;
    }
    @Deprecated
    @Override
    public List<Event> readAll() {
        return eventRepository.findAll();
    }
    @Deprecated
    @Override
    public Page<Event> readPage(int number) {
        return eventRepository.findAll(PageRequest.of(number, PAGE_SIZE));
    }
    public Page<Event> getAllEvents(int page, int size, String sortBy,
                                    String eventTypeComment, String controllerSerialNumber,
                                    String comment, String controllerVehicleNumber,
                                    String startDate, String endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "eventTime"));

        if (sortBy != null && !sortBy.isEmpty()) {
            String[] sortParams = sortBy.split(",");
            String field = sortParams[0];
            String direction = sortParams[1];
            Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(Sort.Direction.ASC, field) : Sort.by(Sort.Direction.DESC, field);
            pageable = PageRequest.of(page, size, sort);
        }

        if (startDate == null || startDate.isBlank()) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0).format(DateTimeFormatter.ISO_DATE_TIME);
        }
        if (endDate == null || endDate.isBlank()) {
            endDate = LocalDateTime.of(9999, 12, 31, 23, 59).format(DateTimeFormatter.ISO_DATE_TIME);
        }

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME);

        return eventRepository.findByEventTypeCommentContainsAndControllerSerialNumberContainsAndCommentContainsAndControllerVehicleNumberContainsAndEventTimeBetween(
                eventTypeComment, controllerSerialNumber,
                comment, controllerVehicleNumber, startDateTime,
                endDateTime, pageable);
    }

    @Override
    public List<Event> getEventsBetween(String start, String end) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        return eventRepository.findByEventTimeBetweenOrderByEventTimeAsc(startDateTime, endDateTime);
    }
}
