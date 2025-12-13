package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "Usuario", description = "Usuario expuesto en las respuestas")
public class UsuarioDto {
  @Schema(description = "Identificador único", example = "1")
  private Long id;
  @Schema(description = "Identificador del usuario en Keycloak (sub)", example = "150b6ac9-d103-48bf-bc99-3de763dbd78b")
  private String keycloakId;
  @Schema(description = "Nombre completo", example = "Juan Pérez")
  private String nombre;
  @Schema(description = "Correo electrónico", example = "juan.perez@example.com")
  private String email;
  @Schema(description = "Número de teléfono", example = "+591 69705051")
  private String telefono;
  @Schema(description = "Rol asignado", example = "ORGANIZADOR")
  private RolUsuario rol;
  @Schema(description = "Cuenta activa", example = "true")
  private Boolean activo;
  @Schema(description = "Fecha de creación", example = "2025-09-01T14:20:15Z")
  private Instant creadoEn;
  @Schema(description = "Última fecha de actualización", example = "2025-09-01T14:25:10Z")
  private Instant actualizadoEn;

  public UsuarioDto() {
  }

  public UsuarioDto(Long id, String nombre, String email, RolUsuario rol, Instant creadoEn, Instant actualizadoEn) {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.rol = rol;
    this.creadoEn = creadoEn;
    this.actualizadoEn = actualizadoEn;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public RolUsuario getRol() {
    return rol;
  }

  public void setRol(RolUsuario rol) {
    this.rol = rol;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public Instant getCreadoEn() {
    return creadoEn;
  }

  public void setCreadoEn(Instant creadoEn) {
    this.creadoEn = creadoEn;
  }

  public Instant getActualizadoEn() {
    return actualizadoEn;
  }

  public void setActualizadoEn(Instant actualizadoEn) {
    this.actualizadoEn = actualizadoEn;
  }
}
