// ValidationException.java
package com.eventos.ms_notifications.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;
    
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public ValidationException(Map<String, String> errors) {
        super("Errores de validación encontrados");
        this.errors = errors;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
}