package javaservice.error.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javaservice.error.models.Event;
import javaservice.error.utils.Message;
import javaservice.error.models.Controller;
import javaservice.error.services.ControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/controllers")
@Tag(name = "Controller", description = "Работа с устройствами-контроллерами")
public class ControllerResource {
    
    private ControllerService controllerService;

    public ControllerResource(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }
    @Operation(summary = "Получить все контроллеры", description = "Возвращает список всех устройств-контроллеров")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("")
    public Page<Controller> getAllControllers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Get all controllers");
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "serialNumber"));
        if (sortBy != null && !sortBy.isEmpty()) {
            String[] sortParams = sortBy.split(",");
            String direction = sortParams[1];
            if (direction.equals("asc")) {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortParams[0]));
            } else {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParams[0]));
            }
        }
        return controllerService.getAllControllers(pageable);
    }
    @Operation(summary = "Получить контроллер по ID", description = "Возвращает устройство-контроллер по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = Controller.class))),
            @ApiResponse(responseCode = "404", description = "Контроллер не найден", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/{id}")
    public Controller getById(@PathVariable(name = "id") Long id){
        log.info("Get controller with id " + id);
        return controllerService.read(id).get();
    }
    @Operation(summary = "Получить контроллеры постранично", description = "Возвращает страницу с устройствами-контроллерами по номеру страницы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Controller.class)))),
            @ApiResponse(responseCode = "404", description = "Пустая страница", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/page/{page}")
    public List getByPage(@PathVariable(name = "page") int page){
        log.info("Get page of controllers with number " + page);
        Page<?> entity = controllerService.readPage(page);
        return entity.get().toList();
    }
    @Operation(summary = "Создать новый контроллер", description = "Добавляет новое устройство-контроллер в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контроллер создан", content = @Content(schema = @Schema(implementation = Controller.class))),
            @ApiResponse(responseCode = "400", description = "Контроллер уже существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("")
    public Controller createController(@RequestBody Controller controller){
        log.info("Create new controller");
        return controllerService.create(controller);
    }
    @Operation(summary = "Обновить существующий контроллер", description = "Обновляет информацию об устройстве-контроллере")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контроллер обновлен", content = @Content(schema = @Schema(implementation = Controller.class))),
            @ApiResponse(responseCode = "400", description = "Контроллер не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PutMapping("/{id}")
    public Controller updateController(@RequestBody Controller controller, @PathVariable Long id){
        log.info("Update controller");
        controller.setId(id);
        return controllerService.update(controller);
    }
    @Operation(summary = "Удалить контроллер по ID", description = "Удаляет устройство-контроллер по заданному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контроллер удален", content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Контроллер не существует", content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteController(@PathVariable(name = "id") Long id){
        log.info("Delete controller");
        boolean isDeleted = controllerService.delete(id);
        if (isDeleted){
            return new ResponseEntity<>(new Message(HttpStatus.OK.value(), "Deleted"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message(HttpStatus.BAD_REQUEST.value(), "Controller doesn't exist"), HttpStatus.BAD_REQUEST);
        }
    }
}
