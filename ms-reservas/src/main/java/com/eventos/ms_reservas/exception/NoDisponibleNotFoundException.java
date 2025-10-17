package com.eventos.ms_reservas.exception;

public class NoDisponibleNotFoundException extends RuntimeException {
    private final Long id;

    public NoDisponibleNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
