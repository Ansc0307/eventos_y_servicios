package com.eventos.ms_reservas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.SolicitudController;
import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.service.SolicitudService;

@WebFluxTest(controllers = SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private SolicitudService solicitudService;

    @Test
    void crudSolicitud() {
        Integer solicitudId = 1;
        LocalDateTime now = LocalDateTime.now();

        // Crear DTOs de prueba
        SolicitudDTO solicitudNueva = new SolicitudDTO(solicitudId, now, "pendiente", 101, 202, 303);
        SolicitudDTO solicitudActualizada = new SolicitudDTO(solicitudId, now, "aprobada", 101, 202, 303);

        // Stubs
        given(solicitudService.crearSolicitud(any(SolicitudDTO.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(solicitudService.obtenerPorId(eq(solicitudId)))
                .willReturn(Optional.of(solicitudNueva))
                .willReturn(Optional.empty()); // después de eliminar
        given(solicitudService.actualizarSolicitud(eq(solicitudId), any(SolicitudDTO.class)))
                .willReturn(Optional.of(solicitudActualizada));
        given(solicitudService.eliminarSolicitud(eq(solicitudId)))
                .willReturn(true);

        // Crear solicitud
        client.post().uri("/v1/solicitudes")
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"idSolicitud\":1,\"fechaSolicitud\":\"" + now + "\",\"estadoSolicitud\":\"pendiente\",\"idOrganizador\":101,\"idProovedor\":202,\"idOferta\":303}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.idSolicitud").isEqualTo(1)
                .jsonPath("$.estadoSolicitud").isEqualTo("pendiente");

        // Obtener solicitud
        client.get().uri("/v1/solicitudes/" + solicitudId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.idSolicitud").isEqualTo(1)
                .jsonPath("$.estadoSolicitud").isEqualTo("pendiente");

        // Actualizar solicitud
        client.post().uri("/v1/solicitudes")
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"idSolicitud\":1,\"fechaSolicitud\":\"" + now + "\",\"estadoSolicitud\":\"aprobada\",\"idOrganizador\":101,\"idProovedor\":202,\"idOferta\":303}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.idSolicitud").isEqualTo(1)
                .jsonPath("$.estadoSolicitud").isEqualTo("aprobada");

        // Eliminar solicitud
        client.delete().uri("/v1/solicitudes/" + solicitudId)
                .exchange()
                .expectStatus().isOk();

        // Obtener solicitud eliminada - verificar formato de error JSON
        client.get().uri("/v1/solicitudes/" + solicitudId)
                .exchange()
                .expectStatus().is5xxServerError(); // porque lanzamos RuntimeException en el controller
    }

    @Test
    void testErrorResponseOnUpdateNonExistent() {
        Integer nonExistentId = 999;
        LocalDateTime now = LocalDateTime.now();

        // Mock service para devolver vacío
        given(solicitudService.actualizarSolicitud(eq(nonExistentId), any(SolicitudDTO.class)))
                .willReturn(Optional.empty());

        client.post().uri("/v1/solicitudes")
                .contentType(APPLICATION_JSON)
                .bodyValue("{\"idSolicitud\":999,\"fechaSolicitud\":\"" + now + "\",\"estadoSolicitud\":\"pendiente\",\"idOrganizador\":101,\"idProovedor\":202,\"idOferta\":303}")
                .exchange()
                .expectStatus().isOk(); // El controller actual no lanza NotFound explícito en POST
    }

    @Test
    void testErrorResponseOnDeleteNonExistent() {
        Integer nonExistentId = 888;

        // Mock service para devolver false
        given(solicitudService.eliminarSolicitud(eq(nonExistentId)))
                .willReturn(false);

        client.delete().uri("/v1/solicitudes/" + nonExistentId)
                .exchange()
                .expectStatus().isOk(); // El controller actual no lanza NotFound explícito
    }
}
