package com.eventos.ms_notifications.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFound(ServerWebExchange exchange, NotFoundException ex) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, exchange.getRequest().getPath().toString(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInput(ServerWebExchange exchange, InvalidInputException ex) {
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().toString(), ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody HttpErrorInfo handleUnauthorized(ServerWebExchange exchange, UnauthorizedException ex) {
        return createHttpErrorInfo(HttpStatus.UNAUTHORIZED, exchange.getRequest().getPath().toString(), ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public @ResponseBody HttpErrorInfo handleConflict(ServerWebExchange exchange, ConflictException ex) {
        return createHttpErrorInfo(HttpStatus.CONFLICT, exchange.getRequest().getPath().toString(), ex);
    }

    // Manejo de validaciones DTO (@Valid) en WebFlux
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        LOGGER.warn("Errores de validación encontrados: {}", errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus status, String path, Exception ex) {
        LOGGER.debug("Returning HTTP status: {} for path: {}, message: {}", status, path, ex.getMessage());
        return new HttpErrorInfo(status, path, ex.getMessage());
    }
}
