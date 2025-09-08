package com.eventos.ms_notifications;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class NotificationControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    void getNotificationByIdOk() {
        long id = 1L;

        client.get()
                .uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo((int) id)
                .jsonPath("$.asunto").isEqualTo("Asunto de prueba")
                .jsonPath("$.mensaje").isEqualTo("Este es un mensaje de prueba.");
    }

    @Test
    void getNotificationInvalidParameter() {
        long id = -5L;

        client.get()
                .uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("El ID de la notificación debe ser positivo: " + id);
    }

    @Test
    void getNotificationNotFound() {
        long id = 100L;

        client.get()
                .uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(NOT_FOUND)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("No se encontró la notificación con ID: " + id);
    }

    @Test
    void createNotificationOk() {
        String body = """
            {
              "userId": 1,
              "asunto": "Nueva notificación",
              "mensaje": "Mensaje creado desde test",
              "prioridad": "ALTA",
              "leido": false,
              "tipoNotificacion": "INFORMATIVA"
            }
            """;

        client.post()
                .uri("/v1/notificaciones")
                .contentType(APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.asunto").isEqualTo("Nueva notificación")
                .jsonPath("$.mensaje").isEqualTo("Mensaje creado desde test");
    }

    @Test
    void getAllNotificationsOk() {
        client.get()
                .uri("/v1/notificaciones/")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(5)
                .jsonPath("$[0].id").isEqualTo(1);
    }
}
