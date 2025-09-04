package com.eventos.ms_usuarios.service;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.exception.EmailDuplicadoException;
import com.eventos.ms_usuarios.exception.PasswordDebilException;
import com.eventos.ms_usuarios.model.RolUsuario;
import com.eventos.ms_usuarios.model.Usuario;
import com.eventos.ms_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UsuarioService usuarioService; // usa inyección por campos con @Autowired pero Mockito la llena

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("crearUsuario lanza EmailDuplicadoException si email existe")
  void crearUsuario_emailDuplicado() {
    when(usuarioRepository.existsByEmail("a@a.com")).thenReturn(true);
    UsuarioCreacionDto dto = new UsuarioCreacionDto("A", "a@a.com", "Clave123", RolUsuario.ORGANIZADOR);
    assertThrows(EmailDuplicadoException.class, () -> usuarioService.crearUsuario(dto));
  }

  @Test
  @DisplayName("crearUsuario lanza PasswordDebilException si password no cumple")
  void crearUsuario_passwordDebil() {
    when(usuarioRepository.existsByEmail("b@b.com")).thenReturn(false);
    UsuarioCreacionDto dto = new UsuarioCreacionDto("B", "b@b.com", "123", RolUsuario.PROVEEDOR);
    assertThrows(PasswordDebilException.class, () -> usuarioService.crearUsuario(dto));
  }

  @Test
  @DisplayName("crearUsuario ok devuelve dto con email")
  void crearUsuario_ok() {
    when(usuarioRepository.existsByEmail("c@c.com")).thenReturn(false);
    when(passwordEncoder.encode("Clave123")).thenReturn("hash");
    when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
      Usuario u = inv.getArgument(0);
      // simular id via reflexión
      try {
        var f = Usuario.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(u, 10L);
      } catch (Exception ignored) {
      }
      return u;
    });
    UsuarioCreacionDto dto = new UsuarioCreacionDto("C", "c@c.com", "Clave123", RolUsuario.PROVEEDOR);
    var result = usuarioService.crearUsuario(dto);
    assertEquals("c@c.com", result.getEmail());
    assertEquals(10L, result.getId());
  }
}
