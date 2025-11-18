package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.config.TestSecurityConfig;
import com.eventos.ms_notifications.dto.PrioridadDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class PrioridadControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void crearPrioridad_Exitosa() {
        PrioridadDTO nueva = new PrioridadDTO(null, "PRUEBA3", "Prioridad de prueba 3");

        webTestClient.post().uri("/v1/prioridades")
                .bodyValue(nueva)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("PRUEBA3")
                .jsonPath("$.descripcion").isEqualTo("Prioridad de prueba 3");
    }

    @Test
    void listarPrioridades_RetornaLista() {
        webTestClient.get().uri("/v1/prioridades")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].nombre").isEqualTo("BAJA")
                .jsonPath("$[1].nombre").isEqualTo("MEDIA")
                .jsonPath("$[2].nombre").isEqualTo("ALTA");
    }

    @Test
    void obtenerPorId_CuandoExiste_RetornaDTO() {
        webTestClient.get().uri("/v1/prioridades/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("BAJA");
    }

    @Test
    void actualizarPrioridad_Exitosa() {
        PrioridadDTO editada = new PrioridadDTO(null, "BAJA-EDIT", "Actualizada");

        webTestClient.put().uri("/v1/prioridades/1")
                .bodyValue(editada)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("BAJA-EDIT")
                .jsonPath("$.descripcion").isEqualTo("Actualizada");
    }
}
