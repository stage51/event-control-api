package javaservice.error.exceptions;

public class EntityIsExistException extends RuntimeException {
    public EntityIsExistException(String entity, Long id){
      super(entity + " with id " + id + " is already exist");
    }
}
