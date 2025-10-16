package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando un recurso NoDisponible se encuentra ocupado o reservado.
 */
public class NoDisponibleOcupadoException extends RuntimeException {

    private final Long id;

    public NoDisponibleOcupadoException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}