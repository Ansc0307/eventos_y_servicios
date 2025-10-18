package com.eventos.ms_usuarios.service;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.dto.UsuarioActualizacionDto;
import com.eventos.ms_usuarios.model.Usuario;
import com.eventos.ms_usuarios.repository.UsuarioRepository;
import com.eventos.ms_usuarios.exception.EmailDuplicadoException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.StringUtils;
import com.eventos.ms_usuarios.model.RolUsuario;
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
    if (dto.getTelefono() != null) {
      usuario.setTelefono(dto.getTelefono());
    }
    if (dto.getActivo() != null) {
      usuario.setActivo(dto.getActivo());
    }
    Usuario guardado = usuarioRepository.save(usuario);
    UsuarioDto res = new UsuarioDto();
    res.setId(guardado.getId());
    res.setNombre(guardado.getNombre());
    res.setEmail(guardado.getEmail());
    res.setTelefono(guardado.getTelefono());
    res.setRol(guardado.getRol());
    res.setActivo(guardado.isActivo());
    res.setCreadoEn(guardado.getCreadoEn());
    res.setActualizadoEn(guardado.getActualizadoEn());
    return res;
  }

  @Transactional(readOnly = true)
  public UsuarioDto obtenerPorId(Long id) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    UsuarioDto res = new UsuarioDto();
    res.setId(u.getId());
    res.setNombre(u.getNombre());
    res.setEmail(u.getEmail());
    res.setTelefono(u.getTelefono());
    res.setRol(u.getRol());
    res.setActivo(u.isActivo());
    res.setCreadoEn(u.getCreadoEn());
    res.setActualizadoEn(u.getActualizadoEn());
    return res;
  }

  @Transactional(readOnly = true)
  public List<UsuarioDto> listar() {
    return usuarioRepository.findAll().stream()
        .map(u -> {
          UsuarioDto d = new UsuarioDto();
          d.setId(u.getId());
          d.setNombre(u.getNombre());
          d.setEmail(u.getEmail());
          d.setTelefono(u.getTelefono());
          d.setRol(u.getRol());
          d.setActivo(u.isActivo());
          d.setCreadoEn(u.getCreadoEn());
          d.setActualizadoEn(u.getActualizadoEn());
          return d;
        })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<UsuarioDto> listar(Pageable pageable) {
    return usuarioRepository.findAll(pageable)
        .map(u -> {
          UsuarioDto d = new UsuarioDto();
          d.setId(u.getId());
          d.setNombre(u.getNombre());
          d.setEmail(u.getEmail());
          d.setTelefono(u.getTelefono());
          d.setRol(u.getRol());
          d.setActivo(u.isActivo());
          d.setCreadoEn(u.getCreadoEn());
          d.setActualizadoEn(u.getActualizadoEn());
          return d;
        });
  }

  @Transactional(readOnly = true)
  public Page<UsuarioDto> listarActivos(Pageable pageable) {
    return usuarioRepository.findByActivoTrue(pageable)
        .map(u -> {
          UsuarioDto d = new UsuarioDto();
          d.setId(u.getId());
          d.setNombre(u.getNombre());
          d.setEmail(u.getEmail());
          d.setTelefono(u.getTelefono());
          d.setRol(u.getRol());
          d.setActivo(u.isActivo());
          d.setCreadoEn(u.getCreadoEn());
          d.setActualizadoEn(u.getActualizadoEn());
          return d;
        });
  }

  @Transactional(readOnly = true)
  public Page<UsuarioDto> listar(Pageable pageable,
      String nombre,
      String email,
      String telefono,
      String rol,
      Boolean activo) {
    Usuario probe = new Usuario();
    if (StringUtils.hasText(nombre)) {
      probe.setNombre(nombre);
    }
    if (StringUtils.hasText(email)) {
      probe.setEmail(email);
    }
    if (StringUtils.hasText(telefono)) {
      probe.setTelefono(telefono);
    }
    if (activo != null) {
      probe.setActivo(activo);
    }
    if (StringUtils.hasText(rol)) {
      try {
        probe.setRol(RolUsuario.valueOf(rol.trim().toUpperCase()));
      } catch (IllegalArgumentException ex) {
        // rol inválido: ignorar filtro
      }
    }

    ExampleMatcher matcher = ExampleMatcher.matchingAll()
        .withIgnoreNullValues()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    return usuarioRepository.findAll(Example.of(probe, matcher), pageable)
        .map(u -> {
          UsuarioDto d = new UsuarioDto();
          d.setId(u.getId());
          d.setNombre(u.getNombre());
          d.setEmail(u.getEmail());
          d.setTelefono(u.getTelefono());
          d.setRol(u.getRol());
          d.setActivo(u.isActivo());
          d.setCreadoEn(u.getCreadoEn());
          d.setActualizadoEn(u.getActualizadoEn());
          return d;
        });
  }

  @Transactional(readOnly = true)
  public Page<UsuarioDto> listarPorDominioEmail(Pageable pageable, String dominio) {
    return usuarioRepository.buscarPorDominioEmail(dominio, pageable)
        .map(u -> {
          UsuarioDto d = new UsuarioDto();
          d.setId(u.getId());
          d.setNombre(u.getNombre());
          d.setEmail(u.getEmail());
          d.setTelefono(u.getTelefono());
          d.setRol(u.getRol());
          d.setActivo(u.isActivo());
          d.setCreadoEn(u.getCreadoEn());
          d.setActualizadoEn(u.getActualizadoEn());
          return d;
        });
  }

  @Transactional
  public void desactivarUsuario(Long id) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    if (u.isActivo()) {
      u.setActivo(false);
      usuarioRepository.save(u);
    }
  }

  @Transactional
  public UsuarioDto activarUsuario(Long id) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    if (!u.isActivo()) {
      u.setActivo(true);
      usuarioRepository.save(u);
    }
    UsuarioDto d = new UsuarioDto();
    d.setId(u.getId());
    d.setNombre(u.getNombre());
    d.setEmail(u.getEmail());
    d.setTelefono(u.getTelefono());
    d.setRol(u.getRol());
    d.setActivo(u.isActivo());
    d.setCreadoEn(u.getCreadoEn());
    d.setActualizadoEn(u.getActualizadoEn());
    return d;
  }

  @Transactional
  public UsuarioDto actualizarParcial(Long id, UsuarioActualizacionDto dto) {
    Usuario u = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    if (dto.getNombre() != null) {
      u.setNombre(dto.getNombre());
    }
    if (dto.getTelefono() != null) {
      u.setTelefono(dto.getTelefono());
    }
    if (dto.getRol() != null) {
      u.setRol(dto.getRol());
    }
    Usuario guardado = usuarioRepository.save(u);
    UsuarioDto d = new UsuarioDto();
    d.setId(guardado.getId());
    d.setNombre(guardado.getNombre());
    d.setEmail(guardado.getEmail());
    d.setTelefono(guardado.getTelefono());
    d.setRol(guardado.getRol());
    d.setActivo(guardado.isActivo());
    d.setCreadoEn(guardado.getCreadoEn());
    d.setActualizadoEn(guardado.getActualizadoEn());
    return d;
  }

  private boolean esPasswordFuerte(String pwd) {
    if (pwd == null || pwd.length() < 8)
      return false;
    boolean tieneLetra = pwd.chars().anyMatch(Character::isLetter);
    boolean tieneDigito = pwd.chars().anyMatch(Character::isDigit);
    return tieneLetra && tieneDigito;
  }
}
