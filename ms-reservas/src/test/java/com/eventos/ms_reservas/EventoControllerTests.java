package com.eventos.ms_reservas;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class EventoControllerTests {

    @Autowired
    private WebTestClient client;


    @Test
    void crudEvento() {
        String eventoId = "evt123";
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
            .accept(APPLICATION_JSON)
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

        // Obtener evento eliminado
        client.get().uri("/v1/evento/" + eventoId)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

}