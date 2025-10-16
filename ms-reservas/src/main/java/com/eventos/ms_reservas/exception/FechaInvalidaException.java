package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando las fechas de un NoDisponible son inválidas.
 */
public class FechaInvalidaException extends RuntimeException {

    private final Long id;

    public FechaInvalidaException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}