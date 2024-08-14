package javaservice.error.services.impl;

import jakarta.transaction.Transactional;
import javaservice.error.exceptions.EntityNotFoundException;
import javaservice.error.models.Controller;
import javaservice.error.repos.ControllerRepository;
import javaservice.error.repos.EventRepository;
import javaservice.error.services.ControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ControllerServiceImpl implements ControllerService {

    private ControllerRepository controllerRepository;
    private EventRepository eventRepository;

    private ControllerServiceImpl(ControllerRepository controllerRepository, EventRepository eventRepository) {
        this.controllerRepository = controllerRepository;
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setControllerRepository(ControllerRepository controllerRepository) {
        this.controllerRepository = controllerRepository;
    }
    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Controller create(Controller entity) {
        /*
        if (read(entity.getId()).isPresent()) {
            return false;
        }
        */
        log.info("Controller created");
        return controllerRepository.saveAndFlush(entity);
    }

    @Override
    public Optional<Controller> read(Long id) {
        Optional<Controller> entity = Optional.of(controllerRepository.findById(id)).orElse(Optional.empty());
        if (entity.isEmpty()){
            log.error("Failed to read controller with id " + id);
            throw new EntityNotFoundException("Controller", id);
        }
        log.info("Controller read with id " + id);
        return entity;
    }

    @Override
    public Controller update(Controller entity) {
        if (read(entity.getId()).isEmpty()) {
            log.error("Failed to update controller with id " + entity.getId());
            throw new EntityNotFoundException("Controller", entity.getId());
        }
        log.info("Controller updated with id " + entity.getId());
        return controllerRepository.saveAndFlush(entity);
    }

    @Override
    public boolean delete(Long id) {
        Optional<Controller> entity = read(id);
        if (read(id).isEmpty()) {
            log.error("Failed to delete controller with id " + id);
            throw new EntityNotFoundException("Controller", id);
        }
        eventRepository.deleteAllByController(entity.get());
        controllerRepository.delete(entity.get());
        log.info("Controller deleted with id " + id);
        return true;
    }

    @Override
    public List<Controller> readAll() {
        return controllerRepository.findAll();
    }

    @Override
    public Page<Controller> readPage(int number) {
        return controllerRepository.findAll(PageRequest.of(number, PAGE_SIZE));
    }

    @Override
    public Optional<Controller> getByGuid(UUID guid) {
        return Optional.ofNullable(controllerRepository.getByGuid(guid));
    }

    @Override
    public Page<Controller> getAllControllers(Pageable pageable) {
        return controllerRepository.findAll(pageable);
    }
}
