package com.eventos.ms_reservas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.NoDisponibilidadController;
import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.exception.NoDisponibleNotFoundException;
import com.eventos.ms_reservas.service.NoDisponibilidadService;

@WebFluxTest(controllers = NoDisponibilidadController.class)
class NoDisponibilidadControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private NoDisponibilidadService service;

    @Test
    void crudNoDisponibilidad() {
        Long ndId = 1L;
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(2);

        NoDisponibilidadDTO ndCreada = new NoDisponibilidadDTO(ndId, 101, "Mantenimiento", inicio, fin, null);
        NoDisponibilidadDTO ndActualizada = new NoDisponibilidadDTO(ndId, 101, "Vacaciones", inicio, fin, null);

        // Stubs
        given(service.crearNoDisponible(any(NoDisponibilidadDTO.class)))
            .willReturn(ndCreada);
        given(service.obtenerPorId(eq(ndId)))
            .willReturn(java.util.Optional.of(ndCreada))
            .willReturn(java.util.Optional.empty()); // después de eliminar
        given(service.actualizar(eq(ndId), any(NoDisponibilidadDTO.class)))
            .willReturn(ndActualizada);
        given(service.eliminarNoDisponible(eq(ndId)))
            .willReturn(void.class); // para WebFlux normalmente no devuelve nada

        // Crear
        client.post().uri("/v1/no-disponibilidades")
            .contentType(APPLICATION_JSON)
            .bodyValue("{\"idOferta\":101,\"motivo\":\"Mantenimiento\",\"fechaInicio\":\"" + inicio + "\",\"fechaFin\":\"" + fin + "\"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.idNoDisponibilidad").isEqualTo(ndId.intValue())
            .jsonPath("$.motivo").isEqualTo("Mantenimiento");

        // Obtener
        client.get().uri("/v1/no-disponibilidades/" + ndId)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.idNoDisponibilidad").isEqualTo(ndId.intValue())
            .jsonPath("$.motivo").isEqualTo("Mantenimiento");

        // Actualizar
        client.post().uri("/v1/no-disponibilidades")
            .contentType(APPLICATION_JSON)
            .bodyValue("{\"idOferta\":101,\"motivo\":\"Vacaciones\",\"fechaInicio\":\"" + inicio + "\",\"fechaFin\":\"" + fin + "\"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.motivo").isEqualTo("Vacaciones");

        // Eliminar
        client.delete().uri("/v1/no-disponibilidades/" + ndId)
            .exchange()
            .expectStatus().isOk();
    }
}
