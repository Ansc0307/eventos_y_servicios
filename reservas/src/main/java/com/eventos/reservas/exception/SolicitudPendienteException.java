package com.eventos.reservas.exception;

// El proveedor todavía no respondió
public class SolicitudPendienteException extends RuntimeException {
    public SolicitudPendienteException(String message) {
        super(message);
    }
}
