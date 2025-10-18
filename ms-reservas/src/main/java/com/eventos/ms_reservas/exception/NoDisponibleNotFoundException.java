package com.eventos.ms_reservas.exception;

public class NoDisponibleNotFoundException extends RuntimeException {
    private final Integer id;

    public NoDisponibleNotFoundException(Integer id, String message) {
        super(message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
