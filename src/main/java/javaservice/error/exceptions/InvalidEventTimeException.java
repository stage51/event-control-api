package javaservice.error.exceptions;

public class InvalidEventTimeException extends RuntimeException {
    public InvalidEventTimeException(String message) {
        super(message);
    }
}
