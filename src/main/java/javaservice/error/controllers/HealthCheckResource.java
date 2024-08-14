package javaservice.error.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javaservice.error.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health Check", description = "Проверки состояния сервиса")
public class HealthCheckResource {
    @Operation(summary = "Проверка состояния сервиса", description = "Возвращает состояние сервиса в формате JSON")
    @ApiResponse(responseCode = "200", description = "Сервис работает нормально", content = @Content(schema = @Schema(implementation = Message.class)))
    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<?> getHealth(){
        log.info("Get the Health Status");
        return new ResponseEntity<>(new Message(HttpStatus.OK.value(), "Service is running"), HttpStatus.OK);
    }
}