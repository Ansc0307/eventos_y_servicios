
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
		Integer reservaId = 1;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime inicio = now.plusDays(1);
		LocalDateTime fin = now.plusDays(2);

		// Crear reserva de prueba
		Reserva reservaAprobada = new Reserva(reservaId, 123, inicio, fin, "APROBADA", now, now);
		Reserva reservaCancelada = new Reserva(reservaId, 123, inicio, fin, "CANCELADA", now, now);

		// Stubs
		given(reservaService.save(any(Reserva.class)))
			.willAnswer(invocation -> {
				Reserva r = invocation.getArgument(0);
				r.setIdReserva(reservaId);
				return r;
			});
		given(reservaService.getById(eq(reservaId)))
			.willReturn(reservaAprobada)
			.willReturn(null); // después de eliminar
		given(reservaService.update(eq(reservaId), any(Reserva.class)))
			.willReturn(reservaCancelada);
		given(reservaService.delete(eq(reservaId)))
			.willReturn(true);

		// Crear reserva
		client.post().uri("/v1/reserva")
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"idSolicitud\":123,\"fechaReservaInicio\":\"" + inicio + "\",\"fechaReservaFin\":\"" + fin + "\",\"estado\":\"APROBADA\"}")
			.exchange()
			.expectStatus().isCreated()
			.expectBody()
			.jsonPath("$.idReserva").isEqualTo(reservaId)
			.jsonPath("$.estado").isEqualTo("APROBADA");

		// Obtener reserva
		client.get().uri("/v1/reserva/" + reservaId)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.idReserva").isEqualTo(reservaId)
			.jsonPath("$.estado").isEqualTo("APROBADA");

		// Actualizar reserva
		client.put().uri("/v1/reserva/" + reservaId)
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"idSolicitud\":123,\"fechaReservaInicio\":\"" + inicio + "\",\"fechaReservaFin\":\"" + fin + "\",\"estado\":\"CANCELADA\"}")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.idReserva").isEqualTo(reservaId)
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
		Integer nonExistentId = 999;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime inicio = now.plusDays(1);
		LocalDateTime fin = now.plusDays(2);
		
		// Mock service to return null for non-existent reserva
		given(reservaService.update(eq(nonExistentId), any(Reserva.class)))
			.willReturn(null);

		// Test update of non-existent reserva
		client.put().uri("/v1/reserva/" + nonExistentId)
			.contentType(APPLICATION_JSON)
			.bodyValue("{\"idSolicitud\":123,\"fechaReservaInicio\":\"" + inicio + "\",\"fechaReservaFin\":\"" + fin + "\",\"estado\":\"APROBADA\"}")
			.exchange()
			.expectStatus().isNotFound()
			.expectBody()
			.jsonPath("$.error").exists()
			.jsonPath("$.path").isEqualTo("/v1/reservas/" + nonExistentId);
	}

	@Test
	void testErrorResponseFormatOnDelete() {
		Integer nonExistentId = 888;
		
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