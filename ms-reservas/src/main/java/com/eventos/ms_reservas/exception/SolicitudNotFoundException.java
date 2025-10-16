package com.eventos.ms_reservas.exception;

/**
 * Excepción lanzada cuando no se encuentra una solicitud.
 */
public class SolicitudNotFoundException extends RuntimeException {

    private final Long id;

    public SolicitudNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}