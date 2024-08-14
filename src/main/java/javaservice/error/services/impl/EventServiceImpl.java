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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

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
    @Override
    public Page<Event> getAllEvents(Pageable pageable, String contains) {
        if (contains.isEmpty() || contains.equals("")){
            return eventRepository.findAll(pageable);
        } else{
            return eventRepository.findByOrEventTypeCommentContainsOrControllerSerialNumberContainsOrCommentContains(
                    pageable, contains, contains, contains);
        }
    }

    @Override
    public List<Event> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByEventTimeBetweenOrderByEventTimeAsc(start, end);
    }
}
