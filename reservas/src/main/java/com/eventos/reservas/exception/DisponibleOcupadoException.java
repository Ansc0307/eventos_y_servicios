package com.eventos.reservas.exception.DisponibleExceptiones;

// Ya está ocupado (no se puede reservar)
public class DisponibleOcupadoException extends RuntimeException {
    public DisponibleOcupadoException(String message) {
        super(message);
    }
}
