package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrioridadControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PrioridadRepository prioridadRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        prioridadRepository.deleteAll();

        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + "/v1/prioridades")
                .build();
    }

    @Test
    void crearPrioridad_DeberiaRetornar201() {
        PrioridadDTO prioridad = new PrioridadDTO();
        prioridad.setNombre("Alta");
        prioridad.setDescripcion("Requiere atención inmediata");

        webTestClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prioridad)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Alta");
    }

    @Test
    void listarTodas_DeberiaRetornarLista() {
        prioridadRepository.save(new Prioridad(null, "Media", "Importancia media"));

        webTestClient.get()
                .uri("")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].nombre").isEqualTo("Media");
    }

    @Test
    void obtenerPorId_DeberiaRetornarPrioridadCorrecta() {
        var prioridad = prioridadRepository.save(new Prioridad(null, "Baja", "Poca urgencia"));

        webTestClient.get()
                .uri("/{id}", prioridad.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Baja");
    }

    @Test
    void actualizarPrioridad_DeberiaRetornarActualizado() {
        var prioridad = prioridadRepository.save(new Prioridad(null, "Alta", "Urgente"));

        PrioridadDTO actualizada = new PrioridadDTO();
        actualizada.setNombre("Alta Actualizada");
        actualizada.setDescripcion("Descripción modificada");

        webTestClient.put()
                .uri("/{id}", prioridad.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actualizada)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Alta Actualizada");
    }

    @Test
    void eliminarPrioridad_DeberiaRetornarNoContent() {
        var prioridad = prioridadRepository.save(new Prioridad(null, "Eliminar", "Temporal"));

        webTestClient.delete()
                .uri("/{id}", prioridad.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/{id}", prioridad.getId())
                .exchange()
                .expectStatus().is5xxServerError();
    }
}

