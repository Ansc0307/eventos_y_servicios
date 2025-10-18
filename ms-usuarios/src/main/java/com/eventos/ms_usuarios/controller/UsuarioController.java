package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioCreacionDto;
import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.dto.UsuarioActualizacionDto;
import com.eventos.ms_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "403", description = "Prohibido. Requiere rol ORGANIZADOR"),
      @ApiResponse(responseCode = "409", description = "Email duplicado")
  })
  @PostMapping
  @PreAuthorize("hasRole('ORGANIZADOR')")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UsuarioDto> crear(
      @Valid @org.springframework.web.bind.annotation.RequestBody UsuarioCreacionDto dto) {
    UsuarioDto creado = usuarioService.crearUsuario(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }

  @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UsuarioDto> obtenerUno(
      @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(usuarioService.obtenerPorId(id));
  }

  @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token")
  })
  @GetMapping
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<List<UsuarioDto>> listar() {
    return ResponseEntity.ok(usuarioService.listar());
  }

  @Operation(summary = "Listar usuarios (paginado)", description = "Lista usuarios con paginación y orden")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token")
  })
  @GetMapping("/page")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Page<UsuarioDto>> listarPage(
      @ParameterObject Pageable pageable,
      @Parameter(description = "Nombre contiene (ignore-case)", example = "ana") @RequestParam(required = false) String nombre,
      @Parameter(description = "Email contiene (ignore-case)", example = "@gmail.com") @RequestParam(required = false) String email,
      @Parameter(description = "Teléfono contiene (ignore-case)", example = "999") @RequestParam(required = false) String telefono,
      @Parameter(description = "Rol del usuario", example = "ORGANIZADOR") @RequestParam(required = false) String rol,
      @Parameter(description = "Solo activos/inactivos", example = "true") @RequestParam(required = false) Boolean activo) {
    Page<UsuarioDto> page = usuarioService.listar(pageable, nombre, email, telefono, rol, activo);
    if (page.isEmpty()) {
      HttpHeaders headers = new HttpHeaders();
      if (activo != null && !activo) {
        headers.add("X-Info", "No hay usuarios desactivados");
      } else if (activo != null && activo) {
        headers.add("X-Info", "No hay usuarios activos");
      } else {
        headers.add("X-Info", "No hay usuarios para el filtro dado");
      }
      return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Listar usuarios activos (derived)", description = "Usa findByActivoTrue(pageable)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token")
  })
  @GetMapping("/activos")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Page<UsuarioDto>> listarActivos(@ParameterObject Pageable pageable) {
    Page<UsuarioDto> page = usuarioService.listarActivos(pageable);
    if (page.isEmpty()) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Info", "No hay usuarios activos");
      return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Buscar por dominio de email (native)", description = "Usa native query: buscarPorDominioEmail(dominio)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token")
  })
  @GetMapping("/por-dominio")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Page<UsuarioDto>> listarPorDominio(
      @ParameterObject Pageable pageable,
      @Parameter(description = "Dominio de email sin @", example = "gmail.com", required = true) @RequestParam String dominio) {
    Page<UsuarioDto> page = usuarioService.listarPorDominioEmail(pageable, dominio);
    if (page.isEmpty()) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Info", "No hay usuarios para el dominio especificado");
      return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Desactivar usuario (soft delete)", description = "Marca el usuario como inactivo sin eliminarlo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuario desactivado"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "403", description = "Prohibido. Requiere rol ORGANIZADOR"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ORGANIZADOR')")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<Void> desactivar(
      @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long id) {
    usuarioService.desactivarUsuario(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Reactivar usuario", description = "Marca el usuario como activo nuevamente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario reactivado"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "403", description = "Prohibido. Requiere rol ORGANIZADOR"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PatchMapping("/{id}/activar")
  @PreAuthorize("hasRole('ORGANIZADOR')")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UsuarioDto> activar(
      @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long id) {
    return ResponseEntity.ok(usuarioService.activarUsuario(id));
  }

  @Operation(summary = "Actualizar usuario (parcial)", description = "Actualiza parcialmente el usuario: nombre, teléfono y rol")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "403", description = "Prohibido. Requiere rol ORGANIZADOR"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ORGANIZADOR')")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UsuarioDto> actualizarParcial(
      @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long id,
      @Valid @org.springframework.web.bind.annotation.RequestBody UsuarioActualizacionDto dto) {
    return ResponseEntity.ok(usuarioService.actualizarParcial(id, dto));
  }
}
