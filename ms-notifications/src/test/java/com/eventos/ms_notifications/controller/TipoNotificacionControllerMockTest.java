package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.config.TestSecurityConfig;
import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class TipoNotificacionControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void crearTipoNotificacion_Exitosa() {
        TipoNotificacionDTO nueva = new TipoNotificacionDTO(null, "AVISO_PRUEBA", "Aviso de prueba");

        webTestClient.post().uri("/v1/tipos-notificacion")
                .bodyValue(nueva)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("AVISO_PRUEBA")
                .jsonPath("$.descripcion").isEqualTo("Aviso de prueba");
    }

    @Test
    void listarTiposNotificacion_RetornaLista() {
        webTestClient.get().uri("/v1/tipos-notificacion")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Test
    void obtenerPorId_CuandoExiste_RetornaDTO() {
        webTestClient.get().uri("/v1/tipos-notificacion/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void actualizarTipoNotificacion_Exitosa() {
        TipoNotificacionDTO editado = new TipoNotificacionDTO(null, "AVISO_EDITADO", "Actualizado desde test");

        webTestClient.put().uri("/v1/tipos-notificacion/1")
                .bodyValue(editado)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("AVISO_EDITADO")
                .jsonPath("$.descripcion").isEqualTo("Actualizado desde test");
    }

    @Test
    void eliminarTipoNotificacion_Exitosa() {
        // Crea primero uno temporal para eliminarlo
        TipoNotificacionDTO temp = new TipoNotificacionDTO(null, "TEMP_ELIMINAR", "Temporal para eliminar");
        Long idCreado = webTestClient.post().uri("/v1/tipos-notificacion")
                .bodyValue(temp)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TipoNotificacionDTO.class)
                .returnResult()
                .getResponseBody()
                .getId();

        // Luego lo elimina
        webTestClient.delete().uri("/v1/tipos-notificacion/{id}", idCreado)
                .exchange()
                .expectStatus().isNoContent();
    }
}
