package com.eventos.ofertas.exception;
public class OfertaNotFoundException extends RuntimeException {
    public OfertaNotFoundException(String message) {
        super(message);
    }
}