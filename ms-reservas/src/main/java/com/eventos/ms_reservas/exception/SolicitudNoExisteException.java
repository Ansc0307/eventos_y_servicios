package com.eventos.ms_reservas.exception;

public class SolicitudNoExisteException extends RuntimeException {
    public SolicitudNoExisteException(String id, String mensaje) {
        super(mensaje);
    }
}
