
package com.eventos.ms_reservas;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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
	void getReservaById() {
		String reservaId = "2";
		client.get().uri("/v1/reserva/" + reservaId)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.id").isEqualTo(reservaId);
	}

	@Test
	void getReservaInvalidParameterString() {
		client.get().uri("/v1/reserva/no-integer")
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(BAD_REQUEST)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.error").exists();
	}

	@Test
	void getReservaNotFound() {
		String reservaIdNotFound = "0";
		client.get().uri("/v1/reserva/" + reservaIdNotFound)
			.accept(APPLICATION_JSON)
			.exchange()
			.expectStatus().isEqualTo(BAD_REQUEST)
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.error").exists();
	}
}
