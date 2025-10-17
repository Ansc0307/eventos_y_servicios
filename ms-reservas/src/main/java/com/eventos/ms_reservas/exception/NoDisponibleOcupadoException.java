package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando un recurso NoDisponible se encuentra ocupado o reservado.
 */
public class NoDisponibleOcupadoException extends RuntimeException {

    private final Integer id;

    public NoDisponibleOcupadoException(Integer id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}