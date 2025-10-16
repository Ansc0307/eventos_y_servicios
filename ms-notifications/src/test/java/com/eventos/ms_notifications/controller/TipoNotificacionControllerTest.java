package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TipoNotificacionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TipoNotificacionRepository tipoNotificacionRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        tipoNotificacionRepository.deleteAll();

        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/v1/tipos-notificacion")
                .build();
    }

    @Test
    void crearTipoNotificacion_DeberiaRetornar201() {
        TipoNotificacionDTO tipo = new TipoNotificacionDTO();
        tipo.setNombre("Recordatorio");
        tipo.setDescripcion("Notificación para recordatorios automáticos");

        webTestClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tipo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Recordatorio");
    }

    @Test
    void listarTodos_DeberiaRetornarLista() {
        // Crear un registro antes de listar
        TipoNotificacionDTO tipo = new TipoNotificacionDTO();
        tipo.setNombre("Evento Nuevo");
        tipo.setDescripcion("Notifica sobre nuevos eventos");

        tipoNotificacionRepository.save(
                new com.eventos.ms_notifications.model.TipoNotificacion(null, tipo.getNombre(), tipo.getDescripcion())
        );

        webTestClient.get()
                .uri("")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].nombre").isEqualTo("Evento Nuevo");
    }

    @Test
    void obtenerPorId_DeberiaRetornarTipoCorrecto() {
        var tipo = tipoNotificacionRepository.save(
                new com.eventos.ms_notifications.model.TipoNotificacion(null, "Promoción", "Descuentos y promociones")
        );

        webTestClient.get()
                .uri("/{id}", tipo.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Promoción");
    }

    @Test
    void actualizarTipoNotificacion_DeberiaRetornarActualizado() {
        var tipo = tipoNotificacionRepository.save(
                new com.eventos.ms_notifications.model.TipoNotificacion(null, "Alerta", "Notificaciones de alerta")
        );

        TipoNotificacionDTO actualizado = new TipoNotificacionDTO();
        actualizado.setNombre("Alerta Modificada");
        actualizado.setDescripcion("Descripción actualizada");

        webTestClient.put()
                .uri("/{id}", tipo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actualizado)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Alerta Modificada");
    }

    @Test
    void eliminarTipoNotificacion_DeberiaRetornarNoContent() {
        var tipo = tipoNotificacionRepository.save(
                new com.eventos.ms_notifications.model.TipoNotificacion(null, "Eliminar", "Para eliminar")
        );

        webTestClient.delete()
                .uri("/{id}", tipo.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/{id}", tipo.getId())
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
