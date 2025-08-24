package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioCreacionDto {
  @NotBlank
  @Size(max = 100)
  private String nombre;
  @NotBlank
  @Email
  @Size(max = 150)
  private String email;
  @NotBlank
  @Size(min = 6, max = 60)
  private String password;
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
