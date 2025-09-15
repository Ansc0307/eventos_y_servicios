
package com.eventos.ms_reservas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.eventos.ms_reservas.controller.ReservaController;
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.service.ReservaService;

@WebFluxTest(controllers = ReservaController.class)
class ReservaControllerTest {

	@Autowired
	private WebTestClient client;

	@MockBean
	private ReservaService reservaService;

	@Test
	void crudReserva() {
		String reservaId = "res123";

		// Stubs
		given(reservaService.save(any(Reserva.class)))
			.willAnswer(invocation -> invocation.getArgument(0));
		given(reservaService.getById(eq(reservaId)))
			.willReturn(new Reserva(reservaId, "APROBADA"))
			.willReturn(null); // después de eliminar
		given(reservaService.update(eq(reservaId), any(Reserva.class)))
			.willReturn(new Reserva(reservaId, "CANCELADA"));
		given(reservaService.delete(eq(reservaId)))
			.willReturn(true);

		// Crear reserva
		client.post().uri("/v1/reserva")
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"id\":\"" + reservaId + "\",\"estado\":\"APROBADA\"}")
			.exchange()
			.expectStatus().isCreated()
			.expectBody()
			.jsonPath("$.id").isEqualTo(reservaId)
			.jsonPath("$.estado").isEqualTo("APROBADA");

		// Obtener reserva
		client.get().uri("/v1/reserva/" + reservaId)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(reservaId)
			.jsonPath("$.estado").isEqualTo("APROBADA");

		// Actualizar reserva
		client.put().uri("/v1/reserva/" + reservaId)
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"id\":\"" + reservaId + "\",\"estado\":\"CANCELADA\"}")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.id").isEqualTo(reservaId)
			.jsonPath("$.estado").isEqualTo("CANCELADA");

		// Eliminar reserva
		client.delete().uri("/v1/reserva/" + reservaId)
			.exchange()
			.expectStatus().isNoContent();

		// Obtener reserva eliminada - verificar formato de error JSON
		client.get().uri("/v1/reserva/" + reservaId)
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
			.jsonPath("$.error").exists()
			.jsonPath("$.path").isEqualTo("/v1/reservas/" + reservaId);
	}

	@Test
	void testErrorResponseFormatOnUpdate() {
		String nonExistentId = "999";
		
		// Mock service to return null for non-existent reserva
		given(reservaService.update(eq(nonExistentId), any(Reserva.class)))
			.willReturn(null);

		// Test update of non-existent reserva
		client.put().uri("/v1/reserva/" + nonExistentId)
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"id\":\"" + nonExistentId + "\",\"estado\":\"APROBADA\"}")
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
			.jsonPath("$.error").exists()
			.jsonPath("$.path").isEqualTo("/v1/reservas/" + nonExistentId);
	}

	@Test
	void testErrorResponseFormatOnDelete() {
		String nonExistentId = "888";
		
		// Mock service to return false for non-existent reserva
		given(reservaService.delete(eq(nonExistentId)))
			.willReturn(false);

		// Test delete of non-existent reserva
		client.delete().uri("/v1/reserva/" + nonExistentId)
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
			.jsonPath("$.error").exists()
			.jsonPath("$.path").isEqualTo("/v1/reservas/" + nonExistentId);
	}
}