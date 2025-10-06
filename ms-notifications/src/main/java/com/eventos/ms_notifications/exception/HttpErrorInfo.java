package com.eventos.ms_notifications.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class HttpErrorInfo {
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime timestamp;
    private final String path;
    private final HttpStatus httpStatus;
    private final String message;
    private final Map<String, String> details;

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        this.timestamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
        this.details = null;
    }

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message, Map<String, String> details) {
        this.timestamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
        this.details = details;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }
    public int getStatus() { return httpStatus.value(); }
    public String getError() { return httpStatus.getReasonPhrase(); }
    public String getMessage() { return message; }
    public Map<String, String> getDetails() { return details; }
}