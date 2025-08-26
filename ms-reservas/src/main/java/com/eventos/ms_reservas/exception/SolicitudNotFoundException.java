package com.eventos.ms_reservas.exception;

public class SolicitudNotFoundException extends RuntimeException {
     public SolicitudNotFoundException(String message) {
        super(message);
    }
}