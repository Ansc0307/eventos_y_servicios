package com.eventos.ms_reservas.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    private Map<String, Object> body(HttpStatus status, String error, String message, String path) {
        Map<String, Object> b = new HashMap<>();
        b.put("timestamp", Instant.now().toString());
        b.put("status", status.value());
        b.put("error", error);
        b.put("message", message);
        b.put("path", path);
        return b;
    }

    @ExceptionHandler(ReservaNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleNotFound(ReservaNotFoundException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ResponseEntity<>(body(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), path), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ResponseEntity<>(body(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), path), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            sb.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("; ");
        }
        return new ResponseEntity<>(body(HttpStatus.BAD_REQUEST, "Validation Error", sb.toString(), path), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ResponseEntity<>(body(HttpStatus.FORBIDDEN, "Forbidden", "Acceso denegado: no tiene permisos", path), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<?> handleAuth(AuthenticationException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ResponseEntity<>(body(HttpStatus.UNAUTHORIZED, "Unauthorized", "No autenticado: envíe un token Bearer válido", path), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleOther(Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return new ResponseEntity<>(body(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), path), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
