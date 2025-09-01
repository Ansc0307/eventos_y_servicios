package com.eventos.ms_usuarios.service;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.model.Usuario;
import com.eventos.ms_usuarios.repository.UsuarioRepository;
import com.eventos.ms_usuarios.exception.EmailDuplicadoException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;
import com.eventos.ms_usuarios.exception.RecursoNoEncontradoException;
import com.eventos.ms_usuarios.exception.PasswordDebilException;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional
  public UsuarioDto crearUsuario(UsuarioCreacionDto dto) {
    // Validación sencilla: email único
    if (usuarioRepository.existsByEmail(dto.getEmail())) {
      throw new EmailDuplicadoException(dto.getEmail());
    }
    // Validación simple de fuerza de contraseña (mínimo 8 caracteres, al menos una
    // letra y un número)
    if (!esPasswordFuerte(dto.getPassword())) {
      throw new PasswordDebilException();
    }
    String hash = passwordEncoder.encode(dto.getPassword());
    Usuario usuario = new Usuario(dto.getNombre(), dto.getEmail(), hash, dto.getRol());
    Usuario guardado = usuarioRepository.save(usuario);
    return new UsuarioDto(
        guardado.getId(),
        guardado.getNombre(),
        guardado.getEmail(),
        guardado.getRol(),
        guardado.getCreadoEn(),
        guardado.getActualizadoEn());
  }

  @Transactional(readOnly = true)
  public UsuarioDto obtenerPorId(Long id) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    return new UsuarioDto(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getCreadoEn(), u.getActualizadoEn());
  }

  @Transactional(readOnly = true)
  public List<UsuarioDto> listar() {
    return usuarioRepository.findAll().stream()
        .map(u -> new UsuarioDto(u.getId(), u.getNombre(), u.getEmail(), u.getRol(), u.getCreadoEn(),
            u.getActualizadoEn()))
        .collect(Collectors.toList());
  }

  private boolean esPasswordFuerte(String pwd) {
    if (pwd == null || pwd.length() < 8)
      return false;
    boolean tieneLetra = pwd.chars().anyMatch(Character::isLetter);
    boolean tieneDigito = pwd.chars().anyMatch(Character::isDigit);
    return tieneLetra && tieneDigito;
  }
}
