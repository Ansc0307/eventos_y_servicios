package com.eventos.ms_reservas.exception;

public class SolicitudNotFoundException extends RuntimeException {
    private final Long id; // ID de la solicitud que no se encontró

    public SolicitudNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
