package com.eventos.reservas.exception;

/**
 * Excepción personalizada para cuando un evento no se encuentra
 * o el ID proporcionado no es válido.
 */
public class EventoNotFoundException extends RuntimeException {
    public EventoNotFoundException(String message) {
        super(message);
    }
}
