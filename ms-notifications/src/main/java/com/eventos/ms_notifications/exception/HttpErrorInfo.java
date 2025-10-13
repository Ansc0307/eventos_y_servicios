package com.eventos.ms_notifications.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class HttpErrorInfo {
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;
    private final String path;
    private final HttpStatus httpStatus;
    private final String message;

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        this.timestamp = LocalDateTime.now();
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }
    public int getStatus() { return httpStatus.value(); }
    public String getError() { return httpStatus.getReasonPhrase(); }
    public String getMessage() { return message; }
}