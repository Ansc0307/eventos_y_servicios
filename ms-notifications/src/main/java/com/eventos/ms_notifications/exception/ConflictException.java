// ConflictException.java
package com.eventos.ms_notifications.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
    
    public ConflictException(String resource, String field, String value) {
        super(resource + " ya existe con " + field + ": " + value);
    }
}