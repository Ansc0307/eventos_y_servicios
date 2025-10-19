package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Schema(description = "Entidad que representa una notificación enviada a un usuario del sistema.")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Asunto o título breve de la notificación", example = "Actualización de evento")
    private String asunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Contenido o mensaje principal de la notificación", example = "Tu evento ha sido actualizado correctamente.")
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Schema(description = "Fecha y hora de creación de la notificación", example = "2025-10-16T12:30:00")
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    @Schema(description = "Indica si la notificación fue leída por el usuario", example = "false")
    private Boolean leido = false;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "Identificador del usuario que recibe la notificación", example = "1005")
    private Long userId;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prioridad", nullable = false)
    @Schema(description = "Prioridad asociada a la notificación.")
    private Prioridad prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo", nullable = false)
    @Schema(description = "Tipo de notificación.")
    private TipoNotificacion tipoNotificacion;

    // Constructores
    public Notificacion() {}

    public Notificacion(Long id, String asunto, String mensaje, LocalDateTime fechaCreacion, Boolean leido, Long userId, Prioridad prioridad, TipoNotificacion tipoNotificacion) {
        this.id = id;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.leido = leido;
        this.userId = userId;
        this.prioridad = prioridad;
        this.tipoNotificacion = tipoNotificacion;
    }

    // Inicialización automática
    @PrePersist
    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (leido == null) {
            leido = false;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Prioridad prioridad) { this.prioridad = prioridad; }

    public TipoNotificacion getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }
}
