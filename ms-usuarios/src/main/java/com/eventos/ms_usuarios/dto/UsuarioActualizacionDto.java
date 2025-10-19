package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "UsuarioActualizacion", description = "Datos para actualizar parcialmente un usuario")
public class UsuarioActualizacionDto {

  @Schema(description = "Nombre completo", example = "Juan Pérez", maxLength = 100)
  @Size(max = 100)
  private String nombre;

  @Schema(description = "Número de teléfono", example = "+591 69705051", maxLength = 20)
  @Size(max = 20)
  private String telefono;

  @Schema(description = "Rol del usuario", example = "ORGANIZADOR")
  private RolUsuario rol;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
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
