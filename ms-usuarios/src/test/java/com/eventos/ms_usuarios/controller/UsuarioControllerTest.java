package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.model.RolUsuario;
import com.eventos.ms_usuarios.model.Usuario;
import com.eventos.ms_usuarios.repository.UsuarioRepository;
import com.eventos.ms_usuarios.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Instant;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsuarioControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private UsuarioRepository usuarioRepository;

  @MockitoBean
  private UsuarioService usuarioService;

  @Test
  @DisplayName("POST /usuarios crea usuario y devuelve 201")
  void crearUsuario_ok() {
    UsuarioCreacionDto dto = new UsuarioCreacionDto("Juan Perez", "juan@example.com", "Clave123",
        RolUsuario.ORGANIZADOR);

    Mockito.when(usuarioService.crearUsuario(Mockito.any())).thenAnswer(inv -> {
      Usuario u = new Usuario(dto.getNombre(), dto.getEmail(), "hash", dto.getRol());
      // simular id y timestamps
      try {
        var idField = Usuario.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(u, 1L);
        var creadoField = Usuario.class.getDeclaredField("creadoEn");
        creadoField.setAccessible(true);
        creadoField.set(u, Instant.now());
        var actField = Usuario.class.getDeclaredField("actualizadoEn");
        actField.setAccessible(true);
        actField.set(u, Instant.now());
      } catch (Exception ignored) {
      }
      return new com.eventos.ms_usuarios.dto.UsuarioDto(1L, u.getNombre(), u.getEmail(), u.getRol(), Instant.now(),
          Instant.now());
    });

    webTestClient.post().uri("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{" +
            "\"nombre\":\"Juan Perez\"," +
            "\"email\":\"juan@example.com\"," +
            "\"password\":\"Clave123\"," +
            "\"rol\":\"ORGANIZADOR\"}")
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.email").isEqualTo("juan@example.com");
  }

  @Test
  @DisplayName("GET /usuarios/{id} devuelve usuario si existe")
  void obtenerUsuario_ok() {
    Mockito.when(usuarioService.obtenerPorId(1L)).thenReturn(
        new com.eventos.ms_usuarios.dto.UsuarioDto(1L, "Ana", "ana@example.com", RolUsuario.PROVEEDOR, Instant.now(),
            Instant.now()));

    webTestClient.get().uri("/usuarios/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.email").isEqualTo("ana@example.com");
  }

  @Test
  @DisplayName("GET /usuarios lista usuarios")
  void listarUsuarios_ok() {
    Mockito.when(usuarioService.listar()).thenReturn(List.of(
        new com.eventos.ms_usuarios.dto.UsuarioDto(1L, "Ana", "ana@example.com", RolUsuario.PROVEEDOR, Instant.now(),
            Instant.now()),
        new com.eventos.ms_usuarios.dto.UsuarioDto(2L, "Luis", "luis@example.com", RolUsuario.ORGANIZADOR,
            Instant.now(), Instant.now())));

    webTestClient.get().uri("/usuarios")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$[0].id").isEqualTo(1)
        .jsonPath("$[1].id").isEqualTo(2);
  }

  @Test
  @DisplayName("POST /usuarios con password débil devuelve 400")
  void crearUsuario_passwordDebil() {
    Mockito.when(usuarioService.crearUsuario(Mockito.any()))
        .thenThrow(new com.eventos.ms_usuarios.exception.PasswordDebilException());

    webTestClient.post().uri("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{" +
            "\"nombre\":\"Juan Perez\"," +
            "\"email\":\"juan@example.com\"," +
            "\"password\":\"123\"," +
            "\"rol\":\"ORGANIZADOR\"}")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @DisplayName("POST /usuarios con email duplicado devuelve 400")
  void crearUsuario_emailDuplicado() {
    Mockito.when(usuarioService.crearUsuario(Mockito.any()))
        .thenThrow(new com.eventos.ms_usuarios.exception.EmailDuplicadoException("repetido@example.com"));

    webTestClient.post().uri("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{" +
            "\"nombre\":\"Usuario X\"," +
            "\"email\":\"repetido@example.com\"," +
            "\"password\":\"Clave123\"," +
            "\"rol\":\"ORGANIZADOR\"}")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @DisplayName("POST /usuarios sin nombre -> 400 validación")
  void crearUsuario_validacion() {
    webTestClient.post().uri("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{" +
            "\"email\":\"no-nombre@example.com\"," +
            "\"password\":\"Clave123\"," +
            "\"rol\":\"PROVEEDOR\"}")
        .exchange()
        .expectStatus().isBadRequest();
  }
}
