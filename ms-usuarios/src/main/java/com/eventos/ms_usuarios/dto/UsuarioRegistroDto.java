package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "UsuarioRegistro", description = "Datos para registrar un usuario (creación en Keycloak + BD)")
public class UsuarioRegistroDto {

  @Schema(description = "Nombre completo", example = "Juan Pérez", maxLength = 100)
  @NotBlank
  @Size(max = 100)
  private String nombre;

  @Schema(description = "Correo electrónico", example = "juan.perez@example.com", maxLength = 150)
  @NotBlank
  @Email
  @Size(max = 150)
  private String email;

  @Schema(description = "Contraseña", example = "Clave1234")
  @NotBlank
  private String password;

  @Schema(description = "Número de teléfono", example = "+591 69705051", maxLength = 20)
  @Size(max = 20)
  private String telefono;

  @Schema(description = "Rol a registrar", example = "ORGANIZADOR")
  @NotNull
  private RolUsuario rol;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
}
