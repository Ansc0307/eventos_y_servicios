package com.eventos.ms_notifications.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, NotFoundException ex) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestExceptions(ServerHttpRequest request, BadRequestException ex) {
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public @ResponseBody HttpErrorInfo handleConflictExceptions(ServerHttpRequest request, ConflictException ex) {
        return createHttpErrorInfo(HttpStatus.CONFLICT, request, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody HttpErrorInfo handleMethodArgumentTypeMismatch(
            ServerHttpRequest request, MethodArgumentTypeMismatchException ex) {

        String parameterName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        String actualValue = ex.getValue() != null ? ex.getValue().toString() : "null";
        
        String message;
        
        if ("id".equals(parameterName) && "Long".equals(requiredType)) {
            message = String.format("El ID debe ser un número entero válido. Se recibió: '%s'", actualValue);
        } else {
            message = String.format(
                "Parámetro '%s' debe ser de tipo %s, pero se recibió: '%s'", 
                parameterName, requiredType, actualValue
            );
        }

        LOGGER.warn("Error de tipo de parámetro - {} - Path: {}", message, request.getPath().value());
        return createHttpErrorInfo(HttpStatus.BAD_REQUEST, request, message);
    }


    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        LOGGER.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }

    // Sobrecarga del método para aceptar String directamente
    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, String message) {
        final String path = request.getPath().pathWithinApplication().value();
        LOGGER.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}