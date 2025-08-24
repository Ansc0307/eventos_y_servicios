package com.eventos.ms_usuarios.dto;

import com.eventos.ms_usuarios.model.RolUsuario;
import java.time.Instant;

public class UsuarioDto {
  private Long id;
  private String nombre;
  private String email;
  private RolUsuario rol;
  private Instant creadoEn;
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

  public RolUsuario getRol() {
    return rol;
  }

  public void setRol(RolUsuario rol) {
    this.rol = rol;
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
