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

import com.eventos.ms_reservas.dto.SolicitudDTO;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SolicitudControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    void createSolicitud() {
        SolicitudDTO solicitud = new SolicitudDTO(
                "1",
                "Reserva test",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3),
                "pendiente"
        );

        client.post()
                .uri("/v1/solicitudes")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .bodyValue(solicitud)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombreRecurso").isEqualTo("Reserva test")
                .jsonPath("$.estado").isEqualTo("pendiente");
    }

    @Test
    void getSolicitudById() {
        int id = 1;

        client.get()
                .uri("/v1/solicitudes/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(String.valueOf(id));
    }

    @Test
    void getSolicitudInvalidParameterString() {
        client.get()
                .uri("/v1/solicitudes/no-integer")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/solicitudes/no-integer")
                .jsonPath("$.error").isEqualTo("Bad Request");
    }

    @Test
    void getSolicitudNotFound() {
        int idNotFound = 999;

        client.get()
                .uri("/v1/solicitudes/" + idNotFound)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/solicitudes/" + idNotFound);
    }

    @Test
    void getSolicitudInvalidParameterNegativeValue() {
        int idInvalid = -1;

        client.get()
                .uri("/v1/solicitudes/" + idInvalid)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/solicitudes/" + idInvalid);
    }
}
