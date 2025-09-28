package com.eventos.ms_notifications.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "prioridad_id", nullable = false)
    private Prioridad prioridad;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoNotificacion tipoNotificacion;

    @Column(nullable = false, length = 100)
    private String asunto;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean leido = false;

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }
    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getAsunto() {
        return asunto;
    }
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getLeido() {
        return leido;
    }
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    // constructores
    public Notificacion() {
    }

    public Notificacion(Long userId, Prioridad prioridad, TipoNotificacion tipoNotificacion,
                       String asunto, String mensaje, LocalDateTime fechaCreacion, Boolean leido) {
        this.userId = userId;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.prioridad = prioridad;
        this.tipoNotificacion = tipoNotificacion;
        this.fechaCreacion = fechaCreacion;
        this.leido = leido;
    }
}
