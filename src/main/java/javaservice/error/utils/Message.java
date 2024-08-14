package javaservice.error.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private int code;
    private String message;

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
