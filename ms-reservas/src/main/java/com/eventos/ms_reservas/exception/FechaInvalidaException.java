package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando las fechas de un NoDisponible son inválidas.
 */
public class FechaInvalidaException extends RuntimeException {

    private final Integer id;

    public FechaInvalidaException(Integer id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}