package com.eventos.ms_reservas.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventoNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Evento no encontrado";
        EventoNotFoundException exception = new EventoNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getId());
    }

    @Test
    void testConstructorWithIdAndMessage() {
        String id = "789";
        String message = "Evento no encontrado: " + id;
        EventoNotFoundException exception = new EventoNotFoundException(id, message);
        
        assertEquals(message, exception.getMessage());
        assertEquals(id, exception.getId());
    }

    @Test
    void testGetIdReturnsCorrectValue() {
        String expectedId = "101";
        EventoNotFoundException exception = new EventoNotFoundException(expectedId, "Test message");
        
        assertEquals(expectedId, exception.getId());
    }

    @Test
    void testBackwardCompatibility() {
        String message = "Legacy error message";
        EventoNotFoundException exception = new EventoNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getId());
        assertTrue(exception instanceof RuntimeException);
    }
}