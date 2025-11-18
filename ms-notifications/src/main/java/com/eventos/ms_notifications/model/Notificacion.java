package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Table("notificacion")
@Schema(description = "Entidad que representa una notificación enviada a un usuario del sistema.")
public class Notificacion {

    @Id
    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @Column("asunto")
    @Schema(description = "Asunto o título breve de la notificación", example = "Actualización de evento")
    private String asunto;

    @Column("mensaje")
    @Schema(description = "Contenido o mensaje principal de la notificación", example = "Tu evento ha sido actualizado correctamente.")
    private String mensaje;

    @Column("fecha_creacion")
    @Schema(description = "Fecha y hora de creación de la notificación", example = "2025-10-16T12:30:00")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column("leido")
    @Schema(description = "Indica si la notificación fue leída por el usuario", example = "false")
    private Boolean leido = false;

    @Column("user_id")
    @Schema(description = "Identificador del usuario que recibe la notificación", example = "1005")
    private Long userId;

    @Column("id_prioridad")
    @Schema(description = "ID de la prioridad asociada a la notificación.")
    private Long prioridadId;

    @Column("id_tipo")
    @Schema(description = "ID del tipo de notificación.")
    private Long tipoNotificacionId;

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

    public Long getPrioridadId() { return prioridadId; }
    public void setPrioridadId(Long prioridadId) { this.prioridadId = prioridadId; }

    public Long getTipoNotificacionId() { return tipoNotificacionId; }
    public void setTipoNotificacionId(Long tipoNotificacionId) { this.tipoNotificacionId = tipoNotificacionId; }
}
