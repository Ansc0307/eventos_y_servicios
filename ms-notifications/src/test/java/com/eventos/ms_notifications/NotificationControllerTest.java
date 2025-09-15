package com.eventos.ms_notifications;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.eventos.ms_notifications.dto.NotificationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class NotificationControllerTest {

    @Autowired
    private WebTestClient client;

    // GET por ID válido
    @Test
    void getNotificationById_OK() {
        Long id = 1L;

        client.get().uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.userId").isEqualTo(1);
    }

    // GET por ID no encontrado
    @Test
    void getNotificationById_NotFound() {
        Long id = 100L;

        client.get().uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/notificaciones/" + id)
                .jsonPath("$.message").isEqualTo("No se encontró la notificación con ID: " + id);
    }

    // GET por ID inválido (<= 0)
    @Test
    void getNotificationById_InvalidInput() {
        Long id = -1L;

        client.get().uri("/v1/notificaciones/" + id)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/v1/notificaciones/" + id)
                .jsonPath("$.message").isEqualTo("El ID de la notificación debe ser positivo: " + id);
    }

    // GET ALL
    @Test
    void getAllNotifications_OK() {
        client.get().uri("/v1/notificaciones/")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(5);
    }

    // POST OK
    @Test
    void createNotification_OK() {
        NotificationDTO dto = new NotificationDTO(
                null,
                200L,
                "Asunto test",
                "Mensaje test",
                "ALTA",
                LocalDateTime.now(),
                false,
                "INFORMATIVA"
        );

        client.post().uri("/v1/notificaciones")
                .contentType(APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(999)
                .jsonPath("$.asunto").isEqualTo("Asunto test");
    }

    // POST con validación inválida (sin asunto)
    @Test
    void createNotification_ValidationError() {
        NotificationDTO dto = new NotificationDTO(
                null,
                200L,
                "", // asunto vacío
                "Mensaje test",
                "MEDIA",
                LocalDateTime.now(),
                false,
                "ALERTA"
        );

        client.post().uri("/v1/notificaciones")
                .contentType(APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.asunto").isEqualTo("El asunto es obligatorio");
    }

    // PUT OK
    @Test
    void updateNotification_OK() {
        NotificationDTO dto = new NotificationDTO(
                null,
                200L,
                "Asunto actualizado",
                "Mensaje actualizado",
                "BAJA",
                LocalDateTime.now(),
                true,
                "ALERTA"
        );

        client.put().uri("/v1/notificaciones/10")
                .contentType(APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.asunto").isEqualTo("Asunto actualizado");
    }

    // DELETE OK
    @Test
    void deleteNotification_OK() {
        client.delete().uri("/v1/notificaciones/20")
                .exchange()
                .expectStatus().isOk();
    }

    // DELETE con error (id inválido)
    @Test
    void deleteNotification_InvalidId() {
        client.delete().uri("/v1/notificaciones/-1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("El ID de la notificación debe ser positivo: -1");
    }
}
