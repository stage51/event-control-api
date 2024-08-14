package javaservice.error.repos;

import javaservice.error.models.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    Page<EventType> findAll(Pageable pageable);
    EventType findByEventCode(Long eventCode);
}
