package com.eventos.ms_reservas;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;

import com.eventos.ms_reservas.controller.DisponibleController;
import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.exception.DisponibleNotFoundException;
import com.eventos.ms_reservas.exception.DisponibleOcupadoException;
import com.eventos.ms_reservas.exception.FechaInvalidaException;
import com.eventos.ms_reservas.service.DisponibleService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(DisponibleController.class)
class DisponibleControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private DisponibleService disponibleService;

    @Test
    void getDisponibleFound() {
        DisponibleDTO dto = new DisponibleDTO("1", "Evento test",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                true);

        when(disponibleService.obtenerPorId(1L)).thenReturn(dto);

        client.get()
              .uri("/v1/disponibles/1")
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.id").isEqualTo("1")
              .jsonPath("$.descripcion").isEqualTo("Evento test")
              .jsonPath("$.disponible").isEqualTo(true);
    }

    @Test
    void getDisponibleNotFound() {
        when(disponibleService.obtenerPorId(999L))
                .thenThrow(new DisponibleNotFoundException(999L, "No encontrado"));

        client.get()
              .uri("/v1/disponibles/999")
              .exchange()
              .expectStatus().isNotFound()
              .expectBody()
              .jsonPath("$.error").isEqualTo("No encontrado")
              .jsonPath("$.path").isEqualTo("/v1/disponibles/999");
    }

    @Test
    void getDisponibleFechaInvalida() {
        when(disponibleService.obtenerPorId(2L))
                .thenThrow(new FechaInvalidaException(2L, "Fecha inválida"));

        client.get()
              .uri("/v1/disponibles/2")
              .exchange()
              .expectStatus().isEqualTo(422)
              .expectBody()
              .jsonPath("$.error").isEqualTo("Fecha inválida")
              .jsonPath("$.path").isEqualTo("/v1/disponibles/2");
    }

    @Test
    void getDisponibleOcupado() {
        when(disponibleService.obtenerPorId(3L))
                .thenThrow(new DisponibleOcupadoException(3L, "Recurso ocupado"));

        client.get()
              .uri("/v1/disponibles/3")
              .exchange()
              .expectStatus().isEqualTo(422)
              .expectBody()
              .jsonPath("$.error").isEqualTo("Recurso ocupado")
              .jsonPath("$.path").isEqualTo("/v1/disponibles/3");
    }

    @Test
    void listDisponibles() {
        DisponibleDTO dto = new DisponibleDTO("1", "Evento test",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                true);

        when(disponibleService.listar()).thenReturn(List.of(dto));

        client.get()
              .uri("/v1/disponibles")
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$[0].id").isEqualTo("1")
              .jsonPath("$[0].descripcion").isEqualTo("Evento test");
    }

    @Test
    void createDisponibleValid() {
        DisponibleDTO input = new DisponibleDTO("1", "Evento test",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                true);

        when(disponibleService.crearDisponible(any(DisponibleDTO.class))).thenReturn(input);


        client.post()
              .uri("/v1/disponibles")
              .bodyValue(input)
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.id").isEqualTo("1")
              .jsonPath("$.disponible").isEqualTo(true);
    }

    @Test
    void deleteDisponibleFound() {
        doNothing().when(disponibleService).eliminarDisponible(1L);

        client.delete()
              .uri("/v1/disponibles/1")
              .exchange()
              .expectStatus().isOk();
    }

    @Test
    void deleteDisponibleNotFound() {
        doThrow(new DisponibleNotFoundException(999L, "No se puede eliminar"))
                .when(disponibleService).eliminarDisponible(999L);

        client.delete()
              .uri("/v1/disponibles/999")
              .exchange()
              .expectStatus().isNotFound()
              .expectBody()
              .jsonPath("$.error").isEqualTo("No se puede eliminar")
              .jsonPath("$.path").isEqualTo("/v1/disponibles/999");
    }
}
