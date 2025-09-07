
package com.eventos.ms_reservas;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReservaControllerTest {

	@Autowired
	private WebTestClient client;


	@Test
	void crudReserva() {
		String reservaId = "res123";
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
			.accept(APPLICATION_JSON)
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

		// Obtener reserva eliminada
		client.get().uri("/v1/reserva/" + reservaId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isNotFound();
	}

}
