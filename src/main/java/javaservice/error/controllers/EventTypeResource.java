package javaservice.error.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javaservice.error.dtos.EventRequest;
import javaservice.error.models.Event;
import javaservice.error.models.EventType;
import javaservice.error.utils.Message;
import javaservice.error.services.EventTypeService;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/event-types")
@Tag(name = "Event Types", description = "Управление типами событий устройств-контроллеров")
public class EventTypeResource {
    private EventTypeService eventTypeService;

    public EventTypeResource(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @Autowired
    public void setEventTypeService(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @Operation(summary = "Генерация типов событий постранично", description = "Генерирует новые типы событий устройств-контроллеров, отсортированные по какому-либо полю по возрастанию или убыванию\"")
    @ApiResponse(responseCode = "200", description = "Типы событий успешно сгенерированы", content = @Content(schema = @Schema(implementation = Message.class)))
    @GetMapping("/generate")
    public ResponseEntity<?> generateEventTypes(){
        log.info("Generate event types");
        eventTypeService.generateTypes();
        return new ResponseEntity<>(new Message(HttpStatus.OK.value(), "Generated"), HttpStatus.OK);
    }
    @Operation(summary = "Получить все типы событий", description = "Возвращает список всех типов событий устройств-контроллеров")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("")
    public Page<EventType> getAllEventTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Get all event types");
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "eventCode"));
        if (sortBy != null && !sortBy.isEmpty()) {
            String[] sortParams = sortBy.split(",");
            String direction = sortParams[1];
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortParams[0]));
            } else {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParams[0]));
            }
        }
        return eventTypeService.getAllEventTypes(pageable);
    }
    @Operation(summary = "Получить тип события по ID", description = "Возвращает событие устройства-контроллера по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = EventType.class))),
            @ApiResponse(responseCode = "404", description = "Тип события не найдено", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        log.info("Get event types with id " + id);
        Optional<?> entity = eventTypeService.read(id);
        if (entity.isEmpty()){
            log.error("Event type with id " + id + " doesn't exist");
            return new ResponseEntity<>(new Message(HttpStatus.NOT_FOUND.value(), "Event type doesn't exist"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(entity.get(), HttpStatus.OK);
        }
    }
    @Operation(summary = "Получить типы событий постранично", description = "Возвращает страницу с типами событий устройств-контроллеров по номеру страницы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventType.class)))),
            @ApiResponse(responseCode = "404", description = "Пустая страница", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/page/{page}")
    public List getByPage(@PathVariable(name = "page") int page){
        log.info("Get page of event types with number " + page);
        return eventTypeService.readPage(page).toList();
    }
    @Operation(summary = "Создать новый тип события", description = "Добавляет новый тип события устройства-контроллера в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тип события создан", content = @Content(schema = @Schema(implementation = EventType.class))),
            @ApiResponse(responseCode = "400", description = "Тип события уже существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public EventType createEventType(@RequestBody EventType eventType){
        log.info("Create new event types");
        return eventTypeService.create(eventType);
    }
    @Operation(summary = "Обновить существующий тип события", description = "Обновляет тип события устройства-контроллера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тип события обновлено", content = @Content(schema = @Schema(implementation = EventType.class))),
            @ApiResponse(responseCode = "400", description = "Тип события не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public EventType updateEventType(@RequestBody EventType eventType, @PathVariable Long id){
        log.info("Update event type");
        eventType.setId(id);
        return eventTypeService.update(eventType);
    }
    @Operation(summary = "Удалить тип события по ID", description = "Удаляет тип события устройства-контроллера по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тип события удалено", content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Тип события не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEventType(@PathVariable(name = "id") Long id){
        log.info("Delete event type");
        boolean isDeleted = eventTypeService.delete(id);
        if (isDeleted){
            return new ResponseEntity<>(new Message(HttpStatus.OK.value(), "Deleted"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message(HttpStatus.BAD_REQUEST.value(), "Event type doesn't exist"), HttpStatus.BAD_REQUEST);
        }
    }
}
