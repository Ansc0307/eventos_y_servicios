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

    @Column(nullable = false, length = 100)
    private String asunto;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prioridad_id", nullable = false)
    private Prioridad prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoNotificacion tipoNotificacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean leido = false;

    // Constructores
    public Notificacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Notificacion(Long userId, String asunto, String mensaje, Prioridad prioridad, 
                       TipoNotificacion tipoNotificacion, Boolean leido) {
        this();
        this.userId = userId;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.prioridad = prioridad;
        this.tipoNotificacion = tipoNotificacion;
        this.leido = leido != null ? leido : false;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public TipoNotificacion getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
}