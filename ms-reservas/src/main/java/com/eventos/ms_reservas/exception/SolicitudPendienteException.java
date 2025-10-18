package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando una solicitud aún está pendiente y no puede procesarse.
 */
public class SolicitudPendienteException extends RuntimeException {

    private final Integer id;

    public SolicitudPendienteException(Integer id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}