package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuario", description = "REST API para usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

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

  @Operation(summary = "Listar usuarios (paginado)", description = "Lista usuarios con paginación y orden")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa")
  })
  @GetMapping("/page")
  public ResponseEntity<Page<UsuarioDto>> listarPage(
      @ParameterObject Pageable pageable,
      @Parameter(description = "Nombre contiene (ignore-case)", example = "ana") @RequestParam(required = false) String nombre,
      @Parameter(description = "Email contiene (ignore-case)", example = "@gmail.com") @RequestParam(required = false) String email,
      @Parameter(description = "Teléfono contiene (ignore-case)", example = "999") @RequestParam(required = false) String telefono,
      @Parameter(description = "Rol del usuario", example = "ORGANIZADOR") @RequestParam(required = false) String rol,
      @Parameter(description = "Solo activos/inactivos", example = "true") @RequestParam(required = false) Boolean activo) {
    return ResponseEntity.ok(usuarioService.listar(pageable, nombre, email, telefono, rol, activo));
  }
}
