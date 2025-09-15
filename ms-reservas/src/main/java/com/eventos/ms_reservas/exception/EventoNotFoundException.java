package com.eventos.ms_reservas.exception;

/**
 * Excepción personalizada para cuando un evento no se encuentra
 * o el ID proporcionado no es válido.
 */
public class EventoNotFoundException extends RuntimeException {
    private final String id;
    
    public EventoNotFoundException(String message) {
        super(message);
        this.id = null;
    }
    
    public EventoNotFoundException(String id, String message) {
        super(message);
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
