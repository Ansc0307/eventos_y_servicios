package com.eventos.ms_reservas.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReservaNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Reserva no encontrada";
        ReservaNotFoundException exception = new ReservaNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getId());
    }

    @Test
    void testConstructorWithIdAndMessage() {
        String id = "123";
        String message = "Reserva no encontrada: " + id;
        ReservaNotFoundException exception = new ReservaNotFoundException(id, message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(id, exception.getId());
    }

    @Test
    void testGetIdReturnsCorrectValue() {
        String expectedId = "456";
        ReservaNotFoundException exception = new ReservaNotFoundException(expectedId, "Test message");
        
        assertEquals(expectedId, exception.getId());
    }

    @Test
    void testBackwardCompatibility() {
        String message = "Legacy error message";
        ReservaNotFoundException exception = new ReservaNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getId());
        assertTrue(exception instanceof RuntimeException);
    }
}