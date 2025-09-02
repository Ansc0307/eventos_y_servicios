package com.eventos.ofertas.exception;

import com.eventos.ofertas.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OfertaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiError> handleNotFound(OfertaNotFoundException ex, ServerWebExchange exchange) {
        ApiError api = new ApiError(404, "Not Found", ex.getMessage(), exchange.getRequest().getPath().value());
        return Mono.just(api);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiError> handleDuplicate(DuplicateTituloException ex, ServerWebExchange exchange) {
        ApiError api = new ApiError(409, "Conflict", ex.getMessage(), exchange.getRequest().getPath().value());
        return Mono.just(api);
    }
    // Validación para WebFlux
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleWebFluxValidation(WebExchangeBindException ex, ServerWebExchange exchange) {
        var fieldErrors = ex.getFieldErrors().stream()
            .collect(Collectors.toMap(fe -> fe.getField(), fe -> fe.getDefaultMessage(), (a,b)->a, LinkedHashMap::new));
        ApiError api = new ApiError(400, "Bad Request", "Errores de validación", exchange.getRequest().getPath().value());
        api.setFieldErrors(fieldErrors);
        return Mono.just(api);
    }
    // Por compatibilidad si alguna vez queda un @Valid en MVC
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleMvcValidation(MethodArgumentNotValidException ex, ServerWebExchange exchange) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(fe -> fe.getField(), fe -> fe.getDefaultMessage(), (a,b)->a, LinkedHashMap::new));
        ApiError api = new ApiError(400, "Bad Request", "Errores de validación", exchange.getRequest().getPath().value());
        api.setFieldErrors(fieldErrors);
        return Mono.just(api);
    }
}
