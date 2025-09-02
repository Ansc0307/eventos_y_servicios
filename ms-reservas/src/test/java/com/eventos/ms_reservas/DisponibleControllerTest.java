package com.eventos.ms_reservas;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.dto.DisponibleDTO;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DisponibleControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    void createDisponible() {
        DisponibleDTO disponible = new DisponibleDTO(
                "1",
                "Evento test",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3),
                true
        );

        client.post()
                .uri("/v1/disponible")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(disponible)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.descripcion").isEqualTo("Evento test")
                .jsonPath("$.disponible").isEqualTo(true);
    }

    @Test
    void getDisponibleById() {
        String id = "1";

        client.get()
                .uri("/v1/disponible/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(id);
    }

    @Test
    void getDisponibleInvalidParameterString() {
        client.get()
                .uri("/v1/disponible/no-integer")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/disponible/no-integer")
                .jsonPath("$.error").isEqualTo("Bad Request");
    }

    @Test
    void getDisponibleNotFound() {
        String idNotFound = "999";

        client.get()
                .uri("/v1/disponible/" + idNotFound)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/disponible/" + idNotFound);
    }

    @Test
    void getDisponibleFechaInvalida() {
        String idFechaInvalida = "fechaInvalida";

        client.get()
                .uri("/v1/disponible/" + idFechaInvalida)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/disponible/" + idFechaInvalida);
    }

    @Test
    void getDisponibleOcupado() {
        String idOcupado = "ocupado";

        client.get()
                .uri("/v1/disponible/" + idOcupado)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/disponible/" + idOcupado);
    }
}
