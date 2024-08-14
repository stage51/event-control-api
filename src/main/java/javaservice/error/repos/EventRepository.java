package javaservice.error.repos;

import jakarta.transaction.Transactional;
import javaservice.error.models.Controller;
import javaservice.error.models.Event;
import javaservice.error.models.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
    @Transactional
    void deleteAllByController(Controller controller);
    void deleteAllByEventType(EventType eventType);
    Page<Event> findByOrEventTypeCommentContainsOrControllerSerialNumberContainsOrCommentContains(
            Pageable pageable, String eventTypeCommentContains,
            String controllerSerialNumberContains, String commentContains);
    List<Event> findByEventTimeBetweenOrderByEventTimeAsc(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
