package com.eventos.ms_reservas.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Mono;

/**
 * Manejo centralizado de excepciones para el microservicio de reservas (WebFlux compatible).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ReservaNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleReservaNotFound(ReservaNotFoundException ex) {
        LOG.error("Error en reserva: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        String path = ex.getId() != null ? "/v1/reservas/" + ex.getId() : "/v1/reservas";
        error.put("path", path);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationErrors(WebExchangeBindException ex) {
        LOG.error("Error de validación: {}", ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Datos de entrada inválidos");
        error.put("validationErrors", ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                fieldError -> fieldError.getField(),
                fieldError -> fieldError.getDefaultMessage(),
                (existing, replacement) -> existing + "; " + replacement
            )));
        
        return Mono.just(new ResponseEntity<>(error, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneral(Exception ex) {
        LOG.error("Error inesperado: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        return Mono.just(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NoDisponibleNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNoDisponibleNotFound(NoDisponibleNotFoundException ex) {
        LOG.error("NoDisponible no encontrado: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/no-disponibles/" + ex.getId());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(NoDisponibleOcupadoException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNoDisponibleOcupado(NoDisponibleOcupadoException ex) {
        LOG.error("NoDisponible ocupado: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/no-disponibles/" + ex.getId());
        return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error));
    }

  

    @ExceptionHandler(NoDisponibleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoDisponibleNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleFechaInvalida(FechaInvalidaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---------- Solicitudes ----------
    @ExceptionHandler(SolicitudNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudNotFound(SolicitudNotFoundException ex) {
        LOG.error("Solicitud no encontrada: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/solicitudes/" + ex.getId());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(SolicitudPendienteException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleSolicitudPendiente(SolicitudPendienteException ex) {
        LOG.error("Solicitud pendiente: {}", ex.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("path", "/v1/solicitudes/" + ex.getId());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
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