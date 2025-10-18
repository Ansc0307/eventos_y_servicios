package com.eventos.ms_reservas.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleReservaNotFoundWithId() {
        String reservaId = "123";
        String message = "Reserva no encontrada: " + reservaId;
        ReservaNotFoundException exception = new ReservaNotFoundException(reservaId, message);

        Mono<ResponseEntity<Map<String, Object>>> result = globalExceptionHandler.handleReservaNotFound(exception);

        StepVerifier.create(result)
            .assertNext(response -> {
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                Map<String, Object> body = response.getBody();
                assertNotNull(body);
                assertEquals(message, body.get("error"));
                assertEquals("/v1/reservas/" + reservaId, body.get("path"));
            })
            .verifyComplete();
    }

    @Test
    void testHandleReservaNotFoundWithoutId() {
        String message = "Reserva no encontrada";
        ReservaNotFoundException exception = new ReservaNotFoundException(message);

        Mono<ResponseEntity<Map<String, Object>>> result = globalExceptionHandler.handleReservaNotFound(exception);

        StepVerifier.create(result)
            .assertNext(response -> {
                assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                Map<String, Object> body = response.getBody();
                assertNotNull(body);
                assertEquals(message, body.get("error"));
                assertEquals("/v1/reservas", body.get("path"));
            })
            .verifyComplete();
    }

    

    @Test
    void testErrorResponseFormatConsistency() {
        // Test that both reserva and evento errors follow the same format as disponible/solicitud
        String id = "789";
        ReservaNotFoundException reservaException = new ReservaNotFoundException(id, "Test reserva error");
       

        Mono<ResponseEntity<Map<String, Object>>> reservaResult = globalExceptionHandler.handleReservaNotFound(reservaException);

        // Verify both have the same structure
        StepVerifier.create(reservaResult)
            .assertNext(response -> {
                Map<String, Object> body = response.getBody();
                assertNotNull(body);
                assertTrue(body.containsKey("error"));
                assertTrue(body.containsKey("path"));
                assertEquals(2, body.size()); // Only error and path fields
            })
            .verifyComplete();

      
    }
}