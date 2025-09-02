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

   // ---------- Disponibles ----------
    @ExceptionHandler(DisponibleNotFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleDisponibleNotFound(DisponibleNotFoundException ex) {
        LOG.error("Error en disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/disponibles");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleFechaInvalida(FechaInvalidaException ex) {
        LOG.error("Error en fechas: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/disponibles");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @ExceptionHandler(DisponibleOcupadoException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleDisponibleOcupado(DisponibleOcupadoException ex) {
        LOG.error("Error en estado de disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/disponibles");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    // ---------- Solicitudes ----------
   @ExceptionHandler(SolicitudNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudNotFound(SolicitudNotFoundException ex) {
        LOG.error("Error en solicitud: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/solicitudes/" + ex.getId()); // path dinámico
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(SolicitudPendienteException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudPendiente(SolicitudPendienteException ex) {
        LOG.error("Solicitud pendiente: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/solicitudes/" + ex.getId()); // path dinámico
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error)); // 409
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleParametroInvalido(IllegalArgumentException ex) {
        HttpStatus status = ex.getMessage().contains("-") ? HttpStatus.UNPROCESSABLE_ENTITY : HttpStatus.BAD_REQUEST;
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/solicitudes");
        return Mono.just(ResponseEntity.status(status).body(error));
    }
}