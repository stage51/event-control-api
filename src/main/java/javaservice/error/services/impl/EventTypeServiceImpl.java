package javaservice.error.services.impl;

import javaservice.error.exceptions.EntityNotFoundException;
import javaservice.error.models.Controller;
import javaservice.error.models.Event;
import javaservice.error.models.EventType;
import javaservice.error.repos.EventRepository;
import javaservice.error.repos.EventTypeRepository;
import javaservice.error.services.EventTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Slf4j
@Service
public class EventTypeServiceImpl implements EventTypeService {
    private EventTypeRepository eventTypeRepository;
    private EventRepository eventRepository;

    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository, EventRepository eventRepository) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setEventTypeRepository(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }
    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Optional<EventType> getByEventCode(Long code) {
        return Optional.ofNullable(eventTypeRepository.findByEventCode(code));
    }

    @Override
    public boolean generateTypes() {
        List<EventType> eventTypes = new ArrayList<>(List.of(
                new EventType(0L, "Не определен"),
                new EventType(10L, "Включение контроллера"),
                new EventType(15L, "Нештатное выключение контроллера"),
                new EventType(20L, "Штатное выключение контроллера"),
                new EventType(30L, "Достигнут порог минимального заряда батареи"),
                new EventType(100L, "Потеря связи с Расходомером"),
                new EventType(110L, "Восстановление связи с Расходомером"),
                new EventType(120L, "Потеря связи с Уровнемером"),
                new EventType(130L, "Восстановление связи с Уровнемером"),
                new EventType(140L, "Потеря внешней связи Контроллера"),
                new EventType(150L, "Восстановление связи Контроллера"),
                new EventType(160L, "Потеря связи с GPS приемником"),
                new EventType(170L, "Восстановление связи с GPS приемником"),
                new EventType(200L, "Машина давно остановилась"),
                new EventType(210L, "Мало места на жёстком диске"),
                new EventType(220L, "Обнаружено некорректное время"),
                new EventType(240L, "Объем сессии принудительно обнулён"),
                new EventType(230L, "Обратный расход в сессии")
        ));
        eventTypeRepository.saveAll(eventTypes);
        return true;
    }

    @Override
    public EventType create(EventType entity) {
        log.info("Event type created");
        return eventTypeRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<EventType> read(Long id) {
        Optional<EventType> entity = Optional.of(eventTypeRepository.findById(id)).orElse(Optional.empty());
        if (entity.isEmpty()){
            log.error("Failed to read event type with id " + id);
            throw new EntityNotFoundException("Event type", id);
        }
        log.info("Event type read with id " + id);
        return Optional.of(eventTypeRepository.findById(id)).orElse(Optional.empty());
    }

    @Override
    public EventType update(EventType entity) {
        if (read(entity.getId()).isEmpty()) {
            log.error("Failed to update event type with id " + entity.getId());
            throw new EntityNotFoundException("Event type", entity.getId());
        }
        log.info("Event type updated with id " + entity.getId());
        return eventTypeRepository.saveAndFlush(entity);
    }

    @Override
    public boolean delete(Long id) {
        Optional<EventType> entity = read(id);
        if (read(id).isEmpty()) {
            log.error("Failed to delete event type with id " + id);
            throw new EntityNotFoundException("Event type", id);
        }
        log.info("Event type deleted with id " + id);
        eventRepository.deleteAllByEventType(entity.get());
        eventTypeRepository.delete(entity.get());
        return true;
    }

    @Override
    public List<EventType> readAll() {
        return eventTypeRepository.findAll();
    }

    @Override
    public Page<EventType> readPage(int number) {
        return eventTypeRepository.findAll(PageRequest.of(number, PAGE_SIZE));
    }

    @Override
    public Page<EventType> getAllEventTypes(Pageable pageable) {
        return eventTypeRepository.findAll(pageable);
    }
}
