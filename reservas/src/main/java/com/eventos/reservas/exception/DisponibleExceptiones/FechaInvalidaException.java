package com.eventos.reservas.exception.DisponibleExceptiones;

// Fechas inválidas (ej. fechaInicio después de fechaFin)
public class FechaInvalidaException extends RuntimeException {
    public FechaInvalidaException(String message) {
        super(message);
    }
}
