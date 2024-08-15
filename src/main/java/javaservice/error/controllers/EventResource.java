package javaservice.error.controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javaservice.error.dtos.EventRequest;
import javaservice.error.models.Controller;
import javaservice.error.utils.EventMapper;
import javaservice.error.utils.Message;
import javaservice.error.models.Event;
import javaservice.error.services.EventService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Event", description = "Работа с событиями устройств-контроллеров")
public class EventResource {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
    @Operation(summary = "Получить все события постранично", description = "Возвращает страницу всех событий устройств-контроллеров, отсортированные по какому-либо полю по возрастанию или убыванию, или получить события по названию типа события, серийному номеру контроллера или комменту события")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("")
    public Page<Event> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "") String contains
    ) {
        log.info("Get all events");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "eventTime"));
        if (sortBy != null && !sortBy.isEmpty()) {
            String[] sortParams = sortBy.split(",");
            String direction = sortParams[1];
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortParams[0]));
            } else {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParams[0]));
            }
        }
        return eventService.getAllEvents(pageable, contains);
    }
    @Operation(summary = "Получить событие по ID", description = "Возвращает событие устройства-контроллера по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Событие не найдено", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/{id}")
    public Event getById(@PathVariable(name = "id") Long id){
        log.info("Get event with id " + id);
        return eventService.read(id).get();
    }
    @Operation(summary = "Получить события постранично", description = "Возвращает страницу с событиями устройств-контроллеров по номеру страницы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class)))),
            @ApiResponse(responseCode = "404", description = "Пустая страница", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/page/{page}")
    public List getByPage(@PathVariable(name = "page") int page){
        log.info("Get page of events with number " + page);
        return eventService.readPage(page).toList();
    }
    @Operation(summary = "Создать новое событие", description = "Добавляет новое событие устройства-контроллера в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие создано", content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Событие уже существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("")
    public Event createEvent(@RequestBody EventRequest eventRequest){
        log.info("Create new event");
        return eventService.create(eventRequest);
    }
    @Operation(summary = "Обновить существующее событие", description = "Обновляет информацию о событии устройства-контроллера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие обновлено", content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Событие не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("{id}")
    public Event updateEvent(@RequestBody Event event, @PathVariable Long id){
        log.info("Update event");
        event.setId(id);
        return eventService.update(event);
    }
    @Operation(summary = "Удалить событие по ID", description = "Удаляет событие устройства-контроллера по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие удалено", content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Событие не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable(name = "id") Long id){
        log.info("Delete event");
        boolean isDeleted = eventService.delete(id);
        if (isDeleted){
            return new ResponseEntity<>(new Message(HttpStatus.OK.value(), "Deleted"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message(HttpStatus.BAD_REQUEST.value(), "Event doesn't exist"), HttpStatus.BAD_REQUEST);
        }
    }
    @Operation(summary = "Получить события по времени", description = "Возвращает список всех событий с заданным промежутком времени")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = List.class)))
    @GetMapping("/statistics")
    public List<Event> getStatistics(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        log.info("Get statistics");
        LocalDateTime startDateTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        return eventService.getEventsBetween(startDateTime, endDateTime);
    }

}
