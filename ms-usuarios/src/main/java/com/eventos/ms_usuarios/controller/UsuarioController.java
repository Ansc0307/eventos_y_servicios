package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping
  public ResponseEntity<UsuarioDto> crear(@Valid @RequestBody UsuarioCreacionDto dto) {
    UsuarioDto creado = usuarioService.crearUsuario(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDto> obtenerUno(@PathVariable Long id) {
    return ResponseEntity.ok(usuarioService.obtenerPorId(id));
  }

  @GetMapping
  public ResponseEntity<List<UsuarioDto>> listar() {
    return ResponseEntity.ok(usuarioService.listar());
  }
}
