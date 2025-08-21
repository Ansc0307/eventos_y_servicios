package com.eventos.reservas.exception;

/**
 * Excepción personalizada para cuando no se encuentra una reserva
 * o el ID proporcionado no es válido.
 */
public class ReservaNotFoundException extends RuntimeException {
    
    public ReservaNotFoundException(String message) {
        super(message);
    }
}
