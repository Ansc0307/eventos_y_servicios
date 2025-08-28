package com.eventos.ms_notifications.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFound(ServerWebExchange exchange, NotFoundException ex) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, exchange.getRequest().getPath().toString(), ex);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInput(ServerWebExchange exchange, InvalidInputException ex) {
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, exchange.getRequest().getPath().toString(), ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus status, String path, Exception ex) {
        LOGGER.debug("Returning HTTP status: {} for path: {}, message: {}", status, path, ex.getMessage());
        return new HttpErrorInfo(status, path, ex.getMessage());
    }
}
