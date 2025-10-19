package com.eventos.ms_reservas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.NoDisponibilidadController;
import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.service.NoDisponibilidadService;

@WebFluxTest(controllers = NoDisponibilidadController.class)
class NoDisponibilidadControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private NoDisponibilidadService noDisponibleService;

    @Test
    void crudNoDisponibilidad() {
        Integer ndId = 1;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicio = now.plusDays(1);
        LocalDateTime fin = now.plusDays(2);

        // DTOs de prueba
        NoDisponibilidadDTO ndCreado = new NoDisponibilidadDTO(ndId, 123, "Mantenimiento", inicio, fin, null);
        NoDisponibilidadDTO ndActualizado = new NoDisponibilidadDTO(ndId, 123, "Actualización", inicio, fin, null);

        // --- Stubs del servicio ---
        given(noDisponibleService.crearNoDisponible(any(NoDisponibilidadDTO.class)))
            .willReturn(ndCreado);

        // Simular flujo: creado -> actualizado -> eliminado -> no encontrado
        given(noDisponibleService.obtenerPorId(eq(ndId)))
            .willReturn(Optional.of(ndCreado))
            .willReturn(Optional.of(ndActualizado))
            .willReturn(Optional.empty());

        given(noDisponibleService.actualizar(eq(ndId), any(NoDisponibilidadDTO.class)))
            .willReturn(ndActualizado);

        doNothing().when(noDisponibleService).eliminarNoDisponible(eq(ndId));

        // --- Crear ---
        client.post().uri("/v1/no-disponibilidades")
            .bodyValue(ndCreado)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.idNoDisponibilidad").isEqualTo(ndId.intValue())
            .jsonPath("$.motivo").isEqualTo("Mantenimiento");

        // --- Obtener (después de crear) ---
        client.get().uri("/v1/no-disponibilidades/" + ndId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.idNoDisponibilidad").isEqualTo(ndId.intValue())
            .jsonPath("$.motivo").isEqualTo("Mantenimiento");

        // --- Actualizar ---
        client.put().uri("/v1/no-disponibilidades/" + ndId)
            .bodyValue(ndActualizado)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.idNoDisponibilidad").isEqualTo(ndId.intValue())
            .jsonPath("$.motivo").isEqualTo("Actualización");

        // --- Eliminar ---
        client.delete().uri("/v1/no-disponibilidades/" + ndId)
            .exchange()
            .expectStatus().isOk();

    } // <-- cerramos crudNoDisponibilidad

    @Test
    void testErrorResponseFormatOnGet() {
        Integer nonExistentId = 999;

        // Stub para no encontrar el registro
        given(noDisponibleService.obtenerPorId(eq(nonExistentId)))
            .willReturn(Optional.empty());

        // Obtener no existente - verificar formato de error JSON
        client.get().uri("/v1/no-disponibilidades/" + nonExistentId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.error").exists()
            .jsonPath("$.path").isEqualTo("/v1/no-disponibilidades/" + nonExistentId);
    }
} // <-- cerramos la clase
