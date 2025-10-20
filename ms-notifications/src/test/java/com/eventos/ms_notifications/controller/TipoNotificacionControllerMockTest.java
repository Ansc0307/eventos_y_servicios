package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.service.TipoNotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

@WebFluxTest(controllers = TipoNotificacionController.class)
class TipoNotificacionControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TipoNotificacionService tipoNotificacionService;

    private TipoNotificacionDTO tipo1;
    private TipoNotificacionDTO tipo2;

    @BeforeEach
    void setup() {
        tipo1 = new TipoNotificacionDTO(1L, "ALERTA", "Notificación de alerta");
        tipo2 = new TipoNotificacionDTO(2L, "INFORMATIVA", "Notificación informativa");
    }

    @Test
    void listarTodos_Retorna200() {
        List<TipoNotificacionDTO> lista = Arrays.asList(tipo1, tipo2);
        Mockito.when(tipoNotificacionService.obtenerTodas()).thenReturn(lista);

        webTestClient.get()
                .uri("/v1/tipos-notificacion")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].nombre").isEqualTo("ALERTA")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].nombre").isEqualTo("INFORMATIVA");
    }

    @Test
    void obtenerPorId_Existente_Retorna200() {
        Mockito.when(tipoNotificacionService.obtenerPorId(1L)).thenReturn(tipo1);

        webTestClient.get()
                .uri("/v1/tipos-notificacion/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("ALERTA");
    }

    @Test
    void crear_NuevoTipo_Retorna201() {
        TipoNotificacionDTO nuevo = new TipoNotificacionDTO(null, "RECORDATORIO", "Recordatorio al usuario");
        TipoNotificacionDTO creado = new TipoNotificacionDTO(3L, "RECORDATORIO", "Recordatorio al usuario");

        // Usar any() para que Mockito acepte cualquier DTO
        Mockito.when(tipoNotificacionService.crear(Mockito.any(TipoNotificacionDTO.class)))
                .thenReturn(creado);

        webTestClient.post()
                .uri("/v1/tipos-notificacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nuevo)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.nombre").isEqualTo("RECORDATORIO");
    }

    @Test
    void actualizar_Existente_Retorna200() {
        TipoNotificacionDTO actualizado = new TipoNotificacionDTO(1L, "ALERTA-EDIT", "Editada");

        // Mockito permite cualquier DTO, pero solo para id=1
        Mockito.when(tipoNotificacionService.actualizar(
                Mockito.eq(1L), Mockito.any(TipoNotificacionDTO.class)))
                .thenReturn(actualizado);

        webTestClient.put()
                .uri("/v1/tipos-notificacion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tipo1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("ALERTA-EDIT");
    }

    @Test
    void eliminar_Existente_Retorna200() {
        Mockito.doNothing().when(tipoNotificacionService).eliminar(1L);

        webTestClient.delete()
                .uri("/v1/tipos-notificacion/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Tipo de notificación con ID 1 eliminada correctamente");
    }
}
