package com.eventos.ms_notifications.exception;

import static org.springframework.http.HttpStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFound(ServerHttpRequest request, NotFoundException ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInput(ServerHttpRequest request, InvalidInputException ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public @ResponseBody HttpErrorInfo handleConflict(ServerHttpRequest request, ConflictException ex) {
        return createHttpErrorInfo(CONFLICT, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequest(ServerHttpRequest request, BadRequestException ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public @ResponseBody HttpErrorInfo handleValidationException(ServerHttpRequest request, WebExchangeBindException ex) {
        String errores = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new HttpErrorInfo(BAD_REQUEST, request.getPath().pathWithinApplication().value(), errores);
    }

    // HANDLERS para errores de KEYCLOAK
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public @ResponseBody HttpErrorInfo handleAccessDenied(ServerHttpRequest request, AccessDeniedException ex) {
        return createHttpErrorInfo(FORBIDDEN, request, new Exception("Acceso denegado: no tienes permisos para este recurso."));
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public @ResponseBody HttpErrorInfo handleUnauthorized(ServerHttpRequest request, AuthenticationException ex) {
        return createHttpErrorInfo(UNAUTHORIZED, request, new Exception("No se pudo autenticar la solicitud: token inválido o ausente."));
    }

    // 
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody HttpErrorInfo handleGenericException(ServerHttpRequest request, Exception ex) {
        return createHttpErrorInfo(INTERNAL_SERVER_ERROR, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus status, ServerHttpRequest request, Exception ex) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor";
        LOGGER.error("[{}] Error HTTP {} en {}: {}", ex.getClass().getSimpleName(), status, path, message);
        return new HttpErrorInfo(status, path, message);
    }
}
