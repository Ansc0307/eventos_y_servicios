package com.eventos.ms_usuarios.controller;

import com.eventos.ms_usuarios.dto.UsuarioDto;
import com.eventos.ms_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Usuario", description = "REST API para usuarios")
public class MeController {

  @Autowired
  private UsuarioService usuarioService;

  @Operation(summary = "Mi usuario", description = "Devuelve el usuario autenticado (por sub de Keycloak) y activa en BD si el email ya fue verificado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Operación exitosa"),
      @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado en BD para ese sub")
  })
  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<UsuarioDto> me(JwtAuthenticationToken authentication) {
    String sub = authentication.getToken().getSubject();
    String accessToken = authentication.getToken().getTokenValue();
    Boolean emailVerifiedClaim = null;
    try {
      emailVerifiedClaim = authentication.getToken().getClaimAsBoolean("email_verified");
    } catch (Exception ignore) {
    }
    return ResponseEntity.ok(usuarioService.obtenerMiUsuario(sub, accessToken, emailVerifiedClaim));
  }
}
