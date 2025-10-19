package com.eventos.ms_usuarios.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "usuarios_login_log")
public class UsuarioLoginLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @Column(nullable = false, length = 150)
  private String email;

  @Column(nullable = false)
  private boolean exito;

  @Column(nullable = false, length = 50)
  private String motivo;

  @Column(length = 45)
  private String ip;

  @Column(columnDefinition = "text")
  private String userAgent;

  @Column(nullable = false, updatable = false)
  private Instant creadoEn;

  @PrePersist
  public void prePersist() {
    this.creadoEn = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isExito() {
    return exito;
  }

  public void setExito(boolean exito) {
    this.exito = exito;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public Instant getCreadoEn() {
    return creadoEn;
  }
}
