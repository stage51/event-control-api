package javaservice.error.exceptions;

import java.util.UUID;

public class ControllerByGuidNotFoundException extends RuntimeException {
    public ControllerByGuidNotFoundException(UUID guid){
      super("Controller with guid " + guid + " not found");;
    }
}
