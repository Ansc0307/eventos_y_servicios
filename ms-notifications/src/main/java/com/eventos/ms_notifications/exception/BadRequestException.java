// BadRequestException.java
package com.eventos.ms_notifications.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String field, String issue) {
        super("Campo '" + field + "' " + issue);
    }
}
