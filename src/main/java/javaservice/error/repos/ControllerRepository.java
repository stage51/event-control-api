package javaservice.error.repos;

import javaservice.error.models.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ControllerRepository extends JpaRepository<Controller, Long> {
    Page<Controller> findAll(Pageable pageable);
    Controller getByGuid(UUID guid);
}
