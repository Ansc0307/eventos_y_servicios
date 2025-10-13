package com.eventos.ms_notifications.exception;

import org.springframework.http.HttpStatus;

public class BaseException {
    private final HttpStatus status;
    private final String reason;

    public BaseException(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}
