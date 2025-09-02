package com.eventos.ms_reservas.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import reactor.core.publisher.Mono;

/**
 * Manejo centralizado de excepciones para el microservicio de reservas (WebFlux compatible).
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ReservaNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleReservaNotFound(ReservaNotFoundException ex) {
        LOG.error("Error en reserva: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(EventoNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleEventoNotFound(EventoNotFoundException ex) {
        LOG.error("Error en evento: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    /*@ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneral(Exception ex) {
        LOG.error("Error inesperado: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
    }*/

    // ----------- Método centralizado para crear respuestas -----------

    private Mono<ResponseEntity<Map<String, Object>>> errorResponse(String message, HttpStatus status, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", message);
        body.put("path", path);
        return Mono.just(ResponseEntity.status(status).body(body));
    }

    // ----------- Solicitudes -----------

    @ExceptionHandler(SolicitudNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudNotFound(SolicitudNotFoundException ex) {
        return errorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, "/v1/solicitudes");
    }

    @ExceptionHandler(SolicitudPendienteException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudPendiente(SolicitudPendienteException ex) {
        return errorResponse(ex.getMessage(), HttpStatus.CONFLICT, "/v1/solicitudes");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleParametroInvalido(IllegalArgumentException ex) {
        HttpStatus status = ex.getMessage().contains("-") ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.BAD_REQUEST;
        return errorResponse(ex.getMessage(), status, "/v1/solicitudes");
    }

    // ----------- Disponibles -----------

    @ExceptionHandler(DisponibleNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleDisponibleNotFound(DisponibleNotFoundException ex) {
        return errorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, "/v1/disponibles");
    }

    @ExceptionHandler(DisponibleOcupadoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleDisponibleOcupado(DisponibleOcupadoException ex) {
        return errorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, "/v1/disponibles");
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleFechaInvalida(FechaInvalidaException ex) {
        return errorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, "/v1/disponibles");
    }

  
    // ----------- Errores generales -----------

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenerico(Exception ex) {
        LOG.error("Error inesperado: {}", ex.getMessage(), ex);
        return errorResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR, "/");
    }
    
}