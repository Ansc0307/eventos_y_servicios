package com.eventos.reservas.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.eventos.reservas.exception.DisponibleExceptiones.DisponibleNotFoundException;
import com.eventos.reservas.exception.DisponibleExceptiones.DisponibleOcupadoException;
import com.eventos.reservas.exception.DisponibleExceptiones.FechaInvalidaException;
/**
 * Manejo centralizado de excepciones para el microservicio de reservas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ReservaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReservaNotFound(ReservaNotFoundException ex) {
        LOG.error("Error en reserva: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventoNotFound(EventoNotFoundException ex) {
        LOG.error("Error en evento: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        LOG.error("Error inesperado: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---------------- exceptiones de Disponible y Solicitud ----------




@ExceptionHandler(DisponibleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReservaNotFound(DisponibleNotFoundException ex) {
        LOG.error("Error en disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    
    @ExceptionHandler(FechaInvalidaException.class)
    public ResponseEntity<?> handleFechaInvalida(FechaInvalidaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DisponibleOcupadoException.class)
    public ResponseEntity<?> handleDisponibleOcupado(DisponibleOcupadoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Método común para devolver errores
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(SolicitudNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventoNotFound(SolicitudNotFoundException ex) {
        LOG.error("Error en Solicitud: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

public ResponseEntity<Map<String, String>> handleEventoNotFound(SolicitudPendienteException ex) {
        LOG.error("Error en Estado de Solicitud: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



}
