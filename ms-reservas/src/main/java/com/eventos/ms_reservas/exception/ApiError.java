package com.eventos.ms_reservas.exception;

public class ApiError {
    private int status;
    private String error;
    private String message;
    private String path;
    private Object details; // opcional para listas de validación u otros datos

    public ApiError(int status, String error, String message, String path, Object details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    // Getters y setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Object getDetails() { return details; }
    public void setDetails(Object details) { this.details = details; }
}
