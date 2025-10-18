package com.eventos.ms_reservas.exception;

/**
 * Excepción personalizada para cuando no se encuentra una reserva
 * o el ID proporcionado no es válido.
 */
public class ReservaNotFoundException extends RuntimeException {
    private final String id;
    
    public ReservaNotFoundException(String message) {
        super(message);
        this.id = null;
    }
    
    public ReservaNotFoundException(String id, String message) {
        super(message);
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
