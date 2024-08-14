package javaservice.error.services;

import javaservice.error.models.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ControllerService extends CrudService<Controller, Controller>{
    Optional<Controller> getByGuid(UUID guid);
    Page<Controller> getAllControllers(Pageable pageable);
}
