package com.eventos.reservas.exception;

// Ya está ocupado (no se puede reservar)
public class DisponibleOcupadoException extends RuntimeException {
    public DisponibleOcupadoException(String message) {
        super(message);
    }
}
