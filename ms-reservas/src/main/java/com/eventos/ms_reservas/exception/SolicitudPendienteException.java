package com.eventos.ms_reservas.exception;

public class SolicitudPendienteException extends RuntimeException {
    private final Long id; // ID de la solicitud pendiente

    public SolicitudPendienteException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
