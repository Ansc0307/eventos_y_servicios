package com.eventos.ms_reservas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.EventoController;
import com.eventos.ms_reservas.model.Evento;
import com.eventos.ms_reservas.service.EventoService;

@WebFluxTest(controllers = EventoController.class)
class EventoControllerTests {

    @Autowired
    private WebTestClient client;

    @MockBean
    private EventoService eventoService;

    @Test
    void crudEvento() {
        String eventoId = "evt123";

        // Stubs
        given(eventoService.save(any(Evento.class)))
            .willAnswer(invocation -> invocation.getArgument(0));
        given(eventoService.getById(eq(eventoId)))
            .willReturn(new Evento(eventoId, "Concierto"))
            .willReturn(null); // después de eliminar
        given(eventoService.update(eq(eventoId), any(Evento.class)))
            .willReturn(new Evento(eventoId, "Cancelado"));
        given(eventoService.delete(eq(eventoId)))
            .willReturn(true);

        // Crear evento
        client.post().uri("/v1/evento")
            .contentType(APPLICATION_JSON)
            .bodyValue("{\"id\":\"" + eventoId + "\",\"descripcion\":\"Concierto\"}")
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.id").isEqualTo(eventoId)
            .jsonPath("$.descripcion").isEqualTo("Concierto");

        // Obtener evento
        client.get().uri("/v1/evento/" + eventoId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(eventoId)
            .jsonPath("$.descripcion").isEqualTo("Concierto");

        // Actualizar evento
        client.put().uri("/v1/evento/" + eventoId)
            .contentType(APPLICATION_JSON)
            .bodyValue("{\"id\":\"" + eventoId + "\",\"descripcion\":\"Cancelado\"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(eventoId)
            .jsonPath("$.descripcion").isEqualTo("Cancelado");

        // Eliminar evento
        client.delete().uri("/v1/evento/" + eventoId)
            .exchange()
            .expectStatus().isNoContent();

        // Obtener evento eliminado - verificar formato de error JSON
        client.get().uri("/v1/evento/" + eventoId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.path").isEqualTo("/v1/eventos/" + eventoId);
    }

    @Test
    void testErrorResponseFormatOnUpdate() {
        String nonExistentId = "999";
        
        // Mock service to return null for non-existent evento
        given(eventoService.update(eq(nonExistentId), any(Evento.class)))
            .willReturn(null);

        // Test update of non-existent evento
        client.put().uri("/v1/evento/" + nonExistentId)
            .contentType(APPLICATION_JSON)
            .bodyValue("{\"id\":\"" + nonExistentId + "\",\"descripcion\":\"Test\"}")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.path").isEqualTo("/v1/eventos/" + nonExistentId);
    }

    @Test
    void testErrorResponseFormatOnDelete() {
        String nonExistentId = "888";
        
        // Mock service to return false for non-existent evento
        given(eventoService.delete(eq(nonExistentId)))
            .willReturn(false);

        // Test delete of non-existent evento
        client.delete().uri("/v1/evento/" + nonExistentId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.path").isEqualTo("/v1/eventos/" + nonExistentId);
    }
}