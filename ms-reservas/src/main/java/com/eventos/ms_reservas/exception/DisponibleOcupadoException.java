package com.eventos.ms_reservas.exception;

public class DisponibleOcupadoException extends RuntimeException {
    private final Long id;

    public DisponibleOcupadoException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
