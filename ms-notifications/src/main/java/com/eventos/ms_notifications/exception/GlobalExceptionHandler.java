package com.eventos.ms_notifications.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HttpErrorInfo handleNotFound(WebRequest request, NotFoundException ex) {
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.warn("Recurso no encontrado: {} - {}", path, ex.getMessage());
        return new HttpErrorInfo(HttpStatus.NOT_FOUND, path, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public HttpErrorInfo handleConflict(WebRequest request, ConflictException ex) {
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.warn("Conflicto: {} - {}", path, ex.getMessage());
        return new HttpErrorInfo(HttpStatus.CONFLICT, path, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpErrorInfo handleBadRequest(WebRequest request, BadRequestException ex) {
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.warn("Solicitud incorrecta: {} - {}", path, ex.getMessage());
        return new HttpErrorInfo(HttpStatus.BAD_REQUEST, path, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpErrorInfo handleValidation(WebRequest request, ValidationException ex) {
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.warn("Error de validación: {} - {}", path, ex.getMessage());
        return new HttpErrorInfo(HttpStatus.BAD_REQUEST, path, ex.getMessage(), ex.getErrors());
    }

    // Manejo de validaciones @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpErrorInfo handleValidationExceptions(WebRequest request, MethodArgumentNotValidException ex) {
        String path = request.getDescription(false).replace("uri=", "");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        LOGGER.warn("Errores de validación en {}: {}", path, errors);
        
        return new HttpErrorInfo(HttpStatus.BAD_REQUEST, path, "Errores de validación en los datos de entrada", errors);
    }

    // Manejo de excepciones genéricas
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpErrorInfo handleGenericException(WebRequest request, Exception ex) {
        String path = request.getDescription(false).replace("uri=", "");
        LOGGER.error("Error interno del servidor: {} - {}", path, ex.getMessage(), ex);
        return new HttpErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, path, "Error interno del servidor");
    }
}