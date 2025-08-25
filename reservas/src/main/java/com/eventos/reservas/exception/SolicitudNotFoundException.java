package com.eventos.reservas.exception;

public class SolicitudNotFoundException extends RuntimeException {
     public SolicitudNotFoundException(String message) {
        super(message);
    }
}