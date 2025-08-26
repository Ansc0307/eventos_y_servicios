package com.eventos.ms_reservas.exception;

// Fechas inválidas (ej. fechaInicio después de fechaFin)
public class FechaInvalidaException extends RuntimeException {
    public FechaInvalidaException(String message) {
        super(message);
    }
}
