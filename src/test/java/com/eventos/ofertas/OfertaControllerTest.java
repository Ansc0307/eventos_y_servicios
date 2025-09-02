package com.eventos.ofertas;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.entity.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class OfertaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private OfertaDTO base;

    @BeforeEach
    void setUp() {
        base = new OfertaDTO();
        base.setProviderId(1L);
        base.setTitulo("Salón Primavera");
        base.setDescripcion("Espacio para 200 personas con catering incluido y estacionamiento.");
        base.setCategoria(Categoria.ESPACIO);
        base.setPrecioBase(new BigDecimal("4500.00"));
        base.setMediaUrls(List.of("https://example.com/foto1.jpg"));
    }

    @Test
    void crearYObtener() {
        // Crear
        OfertaDTO created = webTestClient.post().uri("/ofertas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(base)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OfertaDTO.class)
                .returnResult()
                .getResponseBody();

        // Verifica que created no sea null
        assert created != null;

        // Obtener
        webTestClient.get().uri("/ofertas/" + created.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.titulo").isEqualTo("Salón Primavera");
    }
}