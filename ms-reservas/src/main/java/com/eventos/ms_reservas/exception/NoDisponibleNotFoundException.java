package com.eventos.ms_reservas.exception;

public class NoDisponibleNotFoundException extends RuntimeException {

    private final Long id;

    // Constructor moderno con ID
    public NoDisponibleNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    // Constructor legacy solo con mensaje (id será null)
    public NoDisponibleNotFoundException(String message) {
        super(message);
        this.id = null;
    }

    public Long getId() {
        return id;
    }
}