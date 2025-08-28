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

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneral(Exception ex) {
        LOG.error("Error inesperado: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // ---------------- exceptiones de Disponible y Solicitud ----------

    @ExceptionHandler(DisponibleNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleDisponibleNotFound(DisponibleNotFoundException ex) {
        LOG.error("Error en disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleFechaInvalida(FechaInvalidaException ex) {
        LOG.error("Error en Fechas: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DisponibleOcupadoException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleDisponibleOcupado(DisponibleOcupadoException ex) {
        LOG.error("Error en Estado de Disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(SolicitudNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleSolicitudNotFound(SolicitudNotFoundException ex) {
        LOG.error("Error en Solicitud: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(SolicitudPendienteException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleSolicitudPendiente(SolicitudPendienteException ex) {
        LOG.error("Error en Estado de Solicitud: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return Mono.just(new ResponseEntity<>(error, HttpStatus.CONFLICT)); // 409 más correcto
    }
}