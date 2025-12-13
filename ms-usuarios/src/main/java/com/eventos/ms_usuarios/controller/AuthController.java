package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.dto.UsuarioRegistroDto;
import com.eventos.ms_usuarios.service.RegistroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints públicos para registro")
public class AuthController {

  @Autowired
  private RegistroService registroService;

  @Operation(summary = "Registrar usuario", description = "Crea el usuario en Keycloak y en BD como inactivo (pendiente de verificación de correo)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario registrado"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "409", description = "Email duplicado")
  })
  @PostMapping("/register")
  public ResponseEntity<UsuarioDto> register(@Valid @RequestBody UsuarioRegistroDto dto) {
    UsuarioDto creado = registroService.registrar(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }
}
