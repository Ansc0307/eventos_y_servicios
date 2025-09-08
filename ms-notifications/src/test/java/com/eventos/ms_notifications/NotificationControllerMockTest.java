package com.eventos.ms_notifications;

import com.eventos.ms_notifications.dto.NotificationDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.HttpStatus.NOT_FOUND;
//import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class NotificationControllerMockTest {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        // Caso válido
        when(notificationService.getNotificacionById(1L))
                .thenReturn(new NotificationDTO(
                        1L, 10L, "Asunto Mock", "Mensaje Mock",
                        "ALTA", LocalDateTime.now(), false, "INFORMATIVA"
                ));

        // Not Found
        when(notificationService.getNotificacionById(100L))
                .thenThrow(new NotFoundException("No se encontró la notificación con ID: 100"));

        // Invalid
        when(notificationService.getNotificacionById(-1L))
                .thenThrow(new InvalidInputException("El ID de la notificación debe ser positivo: -1"));

        // Lista de notificaciones
        when(notificationService.getAllNotifications())
                .thenReturn(List.of(
                        new NotificationDTO(1L, 10L, "Asunto 1", "Mensaje 1",
                                "ALTA", LocalDateTime.now(), false, "INFORMATIVA"),
                        new NotificationDTO(2L, 20L, "Asunto 2", "Mensaje 2",
                                "BAJA", LocalDateTime.now(), false, "ALERTA")
                ));
    }

    @Test
    void getNotificationById_ok() {
        client.get().uri("/v1/notificaciones/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.asunto").isEqualTo("Asunto Mock");
    }

    @Test
    void getNotificationById_notFound() {
        client.get().uri("/v1/notificaciones/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("No se encontró la notificación con ID: 100");
    }

    @Test
    void getNotificationById_invalidInput() {
    client.get().uri("/v1/notificaciones/-1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()   // <-- aquí el cambio
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("El ID de la notificación debe ser positivo: -1");
}

    @Test
    void getAllNotifications_ok() {
        client.get().uri("/v1/notificaciones/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].asunto").isEqualTo("Asunto 1")
                .jsonPath("$[1].asunto").isEqualTo("Asunto 2");
    }
}
