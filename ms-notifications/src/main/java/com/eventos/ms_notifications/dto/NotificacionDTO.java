package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Schema(description = "DTO que representa una notificación")
public class NotificacionDTO {

    @Schema(description = "ID de la notificación", example = "1")
    private Long id;

    @NotNull(message = "El user ID es obligatorio")
    @Positive(message = "El user ID debe ser un número positivo")
    @Schema(description = "ID del usuario destinatario", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 100, message = "El asunto no puede exceder los 100 caracteres")
    @Schema(description = "Asunto de la notificación", example = "Recordatorio de evento", requiredMode = Schema.RequiredMode.REQUIRED)
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede exceder los 500 caracteres")
    @Schema(description = "Contenido del mensaje", example = "Tienes un evento programado para mañana", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(description = "ID de la prioridad", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long prioridadId;

    @NotNull(message = "El tipo de notificación es obligatorio")
    @Schema(description = "ID del tipo de notificación", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tipoNotificacionId;

    @Schema(description = "Fecha de creación de la notificación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Si la notificación ha sido leída", example = "false")
    private Boolean leido = false;

    // Campos adicionales para mostrar información relacionada
    @Schema(description = "Nombre de la prioridad", example = "ALTA")
    private String prioridadNombre;

    @Schema(description = "Nombre del tipo de notificación", example = "ALERTA")
    private String tipoNotificacionNombre;

    @Schema(description = "Color de la prioridad", example = "#FF0000")
    private String prioridadColor;

    @Schema(description = "Si el tipo requiere confirmación", example = "true")
    private Boolean tipoRequiereAck;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Long getPrioridadId() { return prioridadId; }
    public void setPrioridadId(Long prioridadId) { this.prioridadId = prioridadId; }

    public Long getTipoNotificacionId() { return tipoNotificacionId; }
    public void setTipoNotificacionId(Long tipoNotificacionId) { this.tipoNotificacionId = tipoNotificacionId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    public String getPrioridadNombre() { return prioridadNombre; }
    public void setPrioridadNombre(String prioridadNombre) { this.prioridadNombre = prioridadNombre; }

    public String getTipoNotificacionNombre() { return tipoNotificacionNombre; }
    public void setTipoNotificacionNombre(String tipoNotificacionNombre) { this.tipoNotificacionNombre = tipoNotificacionNombre; }

    public String getPrioridadColor() { return prioridadColor; }
    public void setPrioridadColor(String prioridadColor) { this.prioridadColor = prioridadColor; }

    public Boolean getTipoRequiereAck() { return tipoRequiereAck; }
    public void setTipoRequiereAck(Boolean tipoRequiereAck) { this.tipoRequiereAck = tipoRequiereAck; }
}