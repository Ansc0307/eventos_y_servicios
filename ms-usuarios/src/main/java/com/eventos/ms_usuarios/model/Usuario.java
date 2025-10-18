package com.eventos.ms_usuarios.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
    @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email")
})
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 150)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RolUsuario rol;

  @Column(nullable = false)
  private boolean activo = true;

  @Column(length = 20)
  private String telefono;

  @Column(nullable = false, updatable = false)
  private Instant creadoEn;

  @Column(nullable = false)
  private Instant actualizadoEn;

  // Relación inversa: un usuario puede tener muchos registros de login
  // No cambia el esquema porque el lado propietario ya existe en UsuarioLoginLog
  // (usuario_id)
  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @OrderBy("creadoEn DESC")
  private List<UsuarioLoginLog> loginLogs = new ArrayList<>();

  public Usuario() {
  }

  public Usuario(String nombre, String email, String password, RolUsuario rol) {
    this.nombre = nombre;
    this.email = email;
    this.password = password;
    this.rol = rol;
  }

  @PrePersist
  public void prePersist() {
    Instant now = Instant.now();
    this.creadoEn = now;
    this.actualizadoEn = now;
  }

  @PreUpdate
  public void preUpdate() {
    this.actualizadoEn = Instant.now();
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

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public Instant getCreadoEn() {
    return creadoEn;
  }

  public Instant getActualizadoEn() {
    return actualizadoEn;
  }

  public List<UsuarioLoginLog> getLoginLogs() {
    return loginLogs;
  }

  public void setLoginLogs(List<UsuarioLoginLog> loginLogs) {
    this.loginLogs = loginLogs;
  }
}
