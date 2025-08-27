package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "REST API para usuarios")
public class UsuarioController {

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario creado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "409", description = "Email duplicado")
  })
  @PostMapping
  public ResponseEntity<UsuarioDto> crear(
      @Valid @org.springframework.web.bind.annotation.RequestBody UsuarioCreacionDto dto) {
    UsuarioDto creado = usuarioService.crearUsuario(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }

  @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDto> obtenerUno(
      @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(usuarioService.obtenerPorId(id));
  }

  @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa")
  })
  @GetMapping
  public ResponseEntity<List<UsuarioDto>> listar() {
    return ResponseEntity.ok(usuarioService.listar());
  }
}
