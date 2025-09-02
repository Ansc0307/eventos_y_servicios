package com.eventos.ms_reservas;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.SolicitudController;
import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.SolicitudNotFoundException;
import com.eventos.ms_reservas.exception.SolicitudPendienteException;
import com.eventos.ms_reservas.service.SolicitudService;

import java.time.LocalDateTime;

@WebFluxTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private SolicitudService solicitudService;

    @Test
    void getSolicitudFound() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setId("1");
        dto.setNombreRecurso("Reserva test");
        dto.setEstado("aceptada");
        dto.setFechaInicio(LocalDateTime.now().plusHours(1));
        dto.setFechaFin(LocalDateTime.now().plusHours(2));

        when(solicitudService.obtenerPorId(1)).thenReturn(dto);

        client.get()
              .uri("/v1/solicitudes/1")
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.id").isEqualTo("1")
              .jsonPath("$.nombreRecurso").isEqualTo("Reserva test")
              .jsonPath("$.estado").isEqualTo("aceptada");
    }

    @Test
    void getSolicitudNotFound() {
        when(solicitudService.obtenerPorId(999))
                .thenThrow(new SolicitudNotFoundException("No se encontró la solicitud"));

        client.get()
              .uri("/v1/solicitudes/999")
              .exchange()
              .expectStatus().isNotFound()
              .expectBody()
              .jsonPath("$.error").isEqualTo("No se encontró la solicitud")
              .jsonPath("$.path").isEqualTo("/v1/solicitudes/999");
    }

    @Test
    void getSolicitudPendiente() {
        when(solicitudService.obtenerPorId(5))
                .thenThrow(new SolicitudPendienteException("La solicitud aún está pendiente"));

        client.get()
              .uri("/v1/solicitudes/5")
              .exchange()
              .expectStatus().isEqualTo(409)
              .expectBody()
              .jsonPath("$.error").isEqualTo("La solicitud aún está pendiente")
              .jsonPath("$.path").isEqualTo("/v1/solicitudes/5");
    }

    @Test
    void createSolicitudValid() {
        SolicitudDTO input = new SolicitudDTO();
        input.setNombreRecurso("Reserva test");
        input.setFechaInicio(LocalDateTime.now().plusHours(1));
        input.setFechaFin(LocalDateTime.now().plusHours(2));

        SolicitudDTO output = new SolicitudDTO();
        output.setId("1");
        output.setNombreRecurso("Reserva test");
        output.setEstado("pendiente");
        output.setFechaInicio(input.getFechaInicio());
        output.setFechaFin(input.getFechaFin());

        when(solicitudService.crearSolicitud(input)).thenReturn(output);

        client.post()
              .uri("/v1/solicitudes")
              .bodyValue(input)
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .jsonPath("$.id").isEqualTo("1")
              .jsonPath("$.estado").isEqualTo("pendiente");
    }

    @Test
    void deleteSolicitudFound() {
        doNothing().when(solicitudService).eliminarSolicitud(1);

        client.delete()
              .uri("/v1/solicitudes/1")
              .exchange()
              .expectStatus().isOk();
    }

    @Test
    void deleteSolicitudNotFound() {
        doThrow(new SolicitudNotFoundException("No se puede eliminar"))
                .when(solicitudService).eliminarSolicitud(999);

        client.delete()
              .uri("/v1/solicitudes/999")
              .exchange()
              .expectStatus().isNotFound()
              .expectBody()
              .jsonPath("$.error").isEqualTo("No se puede eliminar")
              .jsonPath("$.path").isEqualTo("/v1/solicitudes/999");
    }
}
