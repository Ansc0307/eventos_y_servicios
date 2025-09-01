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
}
