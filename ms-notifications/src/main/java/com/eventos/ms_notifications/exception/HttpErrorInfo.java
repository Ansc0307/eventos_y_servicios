package com.eventos.ms_notifications.exception;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

public class HttpErrorInfo {

    private final String timestamp;
    private final String path;
    private final String message;
    private final int status;
    private final String error;

    public HttpErrorInfo(HttpStatus status, String path, String message) {
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.path = path;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
