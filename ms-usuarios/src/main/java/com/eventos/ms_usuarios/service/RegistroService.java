package com.eventos.ms_usuarios.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.dto.UsuarioRegistroDto;
import com.eventos.ms_usuarios.exception.EmailDuplicadoException;
import com.eventos.ms_usuarios.exception.PasswordDebilException;
import com.eventos.ms_usuarios.model.Usuario;
import com.eventos.ms_usuarios.repository.UsuarioRepository;

@Service
public class RegistroService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private KeycloakAdminService keycloakAdminService;

  @Transactional
  public UsuarioDto registrar(UsuarioRegistroDto dto) {
    if (usuarioRepository.existsByEmail(dto.getEmail())) {
      throw new EmailDuplicadoException(dto.getEmail());
    }

    if (!esPasswordFuerte(dto.getPassword())) {
      throw new PasswordDebilException();
    }

    String token = keycloakAdminService.getServiceAccountToken();

    String username = dto.getEmail().trim().toLowerCase();
    String email = dto.getEmail().trim();

    String nombre = dto.getNombre() == null ? "" : dto.getNombre().trim();
    // Keycloak suele pedir UPDATE_PROFILE si faltan firstName/lastName.
    // Como solo tenemos un campo "nombre", lo mapeamos de forma simple:
    // - si hay espacios: firstName=primer token, lastName=resto
    // - si no: firstName=nombre, lastName=nombre
    String firstName;
    String lastName;
    if (nombre.isBlank()) {
      firstName = "Usuario";
      lastName = "";
    } else {
      int idx = nombre.indexOf(' ');
      if (idx > 0) {
        firstName = nombre.substring(0, idx).trim();
        lastName = nombre.substring(idx + 1).trim();
        if (lastName.isBlank()) {
          lastName = firstName;
        }
      } else {
        firstName = nombre;
        lastName = nombre;
      }
    }

    String keycloakUserId = null;

    try {
      keycloakUserId = keycloakAdminService.createUser(token, username, email, firstName, lastName);
      keycloakAdminService.setPassword(token, keycloakUserId, dto.getPassword());
      keycloakAdminService.assignRealmRole(token, keycloakUserId, dto.getRol().name());

      try {
        keycloakAdminService.sendVerifyEmail(token, keycloakUserId);
      } catch (HttpStatusCodeException ignore) {
        // No bloqueamos el registro si Keycloak no puede enviar el correo (p.ej. SMTP
        // no configurado).
      }

      // En BD guardamos perfil + enlace al sub. Password NO se usa para login;
      // guardamos un placeholder hasheado para cumplir NOT NULL.
      String placeholder = passwordEncoder.encode(UUID.randomUUID().toString());
      Usuario usuario = new Usuario(dto.getNombre(), email, placeholder, dto.getRol());
      usuario.setKeycloakId(keycloakUserId);
      usuario.setActivo(false);
      if (dto.getTelefono() != null) {
        usuario.setTelefono(dto.getTelefono());
      }

      Usuario guardado = usuarioRepository.save(usuario);

      UsuarioDto res = new UsuarioDto();
      res.setId(guardado.getId());
      res.setKeycloakId(guardado.getKeycloakId());
      res.setNombre(guardado.getNombre());
      res.setEmail(guardado.getEmail());
      res.setTelefono(guardado.getTelefono());
      res.setRol(guardado.getRol());
      res.setActivo(guardado.isActivo());
      res.setCreadoEn(guardado.getCreadoEn());
      res.setActualizadoEn(guardado.getActualizadoEn());
      return res;

    } catch (HttpClientErrorException ex) {
      if (keycloakUserId != null) {
        try {
          keycloakAdminService.deleteUser(token, keycloakUserId);
        } catch (Exception ignore) {
        }
      }
      if (keycloakAdminService.isEmailAlreadyUsed(ex)) {
        throw new EmailDuplicadoException(dto.getEmail());
      }
      throw ex;

    } catch (DataIntegrityViolationException ex) {
      if (keycloakUserId != null) {
        try {
          keycloakAdminService.deleteUser(token, keycloakUserId);
        } catch (Exception ignore) {
        }
      }
      throw ex;
    }
  }

  private boolean esPasswordFuerte(String pwd) {
    if (pwd == null || pwd.length() < 8)
      return false;
    boolean tieneLetra = pwd.chars().anyMatch(Character::isLetter);
    boolean tieneDigito = pwd.chars().anyMatch(Character::isDigit);
    return tieneLetra && tieneDigito;
  }
}
