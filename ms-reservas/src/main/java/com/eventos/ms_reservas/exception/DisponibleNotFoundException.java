package com.eventos.ms_reservas.exception;

public class DisponibleNotFoundException extends RuntimeException {
    private final Long id;

    public DisponibleNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
