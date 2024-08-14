package javaservice.error.exceptions;

public class EntityInvalidException extends RuntimeException{
    public EntityInvalidException(String message) {
        super(message);
    }
}
