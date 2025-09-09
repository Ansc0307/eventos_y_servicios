package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "UsuarioCreacion", description = "Datos para crear un usuario nuevo")
public class UsuarioCreacionDto {
  @Schema(description = "Nombre completo", example = "Juan Pérez", maxLength = 100, required = true)
  @NotBlank
  @Size(max = 100)
  private String nombre;

  @Schema(description = "Correo electrónico único", example = "juan.perez@example.com", required = true)
  @NotBlank
  @Email
  @Size(max = 150)
  private String email;

  @Schema(description = "Contraseña en texto plano (será hasheada)", example = "Clave123", minLength = 8, maxLength = 60, required = true)
  @NotBlank
  @Size(min = 8, max = 60)
  private String password;

  @Schema(description = "Rol del usuario", example = "ORGANIZADOR", required = true)
  @NotNull
  private RolUsuario rol;

  public UsuarioCreacionDto() {
  }

  public UsuarioCreacionDto(String nombre, String email, String password, RolUsuario rol) {
    this.nombre = nombre;
    this.email = email;
    this.password = password;
    this.rol = rol;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public RolUsuario getRol() {
    return rol;
  }

  public void setRol(RolUsuario rol) {
    this.rol = rol;
  }
}
