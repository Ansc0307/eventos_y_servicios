package com.eventos.reservas.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<Map<String, String>> handleEventoNotFound(FechaInvalidaException ex) {
        LOG.error("Error en Fechas: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DisponibleOcupadoException.class)
    public ResponseEntity<Map<String, String>> handleEventoNotFound(DisponibleOcupadoException ex) {
        LOG.error("Error en Estado de Disponibilidad: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SolicitudNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEventoNotFound(SolicitudNotFoundException ex) {
        LOG.error("Error en Solicitud: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

@ExceptionHandler(SolicitudPendienteException.class)
public ResponseEntity<Map<String, String>> handleSolicitudPendiente(SolicitudPendienteException ex) {
    LOG.error("Error en Estado de Solicitud: {}", ex.getMessage());
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409 más correcto
}




}
