// InvalidInputException.java
package com.eventos.ms_notifications.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {}
    public InvalidInputException(String message) { super(message); }
    public InvalidInputException(String message, Throwable cause) { super(message, cause); }
    public InvalidInputException(Throwable cause) { super(cause); }
}