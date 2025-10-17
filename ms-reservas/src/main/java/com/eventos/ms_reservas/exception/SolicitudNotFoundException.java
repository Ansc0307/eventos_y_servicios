package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando no se encuentra una solicitud.
 */
public class SolicitudNotFoundException extends RuntimeException {

    private final Integer id;

    public SolicitudNotFoundException(Integer id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}