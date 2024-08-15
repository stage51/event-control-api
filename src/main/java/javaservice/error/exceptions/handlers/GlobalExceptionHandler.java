package javaservice.error.exceptions.handlers;

import javaservice.error.exceptions.*;
import javaservice.error.utils.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(new Message(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
    @ExceptionHandler(EntityIsExistException.class)
    public ResponseEntity<?> handleEntityIsExistException(EntityIsExistException ex) {
        return ResponseEntity.badRequest().body(new Message(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(EntityInvalidException.class)
    public ResponseEntity<?> handleEntityIsInvalidException(EntityInvalidException ex){
        return ResponseEntity.badRequest().body(new Message(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(ControllerByGuidNotFoundException.class)
    public ResponseEntity<?> handleControllerByGuidNotFoundException(ControllerByGuidNotFoundException ex){
        return ResponseEntity.badRequest().body(new Message(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }
    @ExceptionHandler(EventMappingException.class)
    public ResponseEntity<?> handleEventMappingException(EventMappingException ex){
        return ResponseEntity.badRequest().body(new Message(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(InvalidEventTimeException.class)
    public ResponseEntity<?> handleInvalidEventTimeException(InvalidEventTimeException ex){
        return ResponseEntity.badRequest().body(new Message(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }
    @ExceptionHandler(IncorrectLoginOrPasswordException.class)
    public ResponseEntity<?> handleIncorrectLoginOrPasswordException(IncorrectLoginOrPasswordException ex){
        return ResponseEntity.badRequest().body(new Message(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }
}
