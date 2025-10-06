package com.eventos.ms_notifications.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String resource, Long id) {
        super(resource + " no encontrado con ID: " + id);
    }
    
    public NotFoundException(String resource, String identifier) {
        super(resource + " no encontrado: " + identifier);
    }
}