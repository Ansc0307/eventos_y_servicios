package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para resultados de consulta con JOIN de notificaciones")
public class NotificacionConNombresDTO {
    
    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @Schema(description = "Asunto o título breve de la notificación", example = "Actualización de evento")
    private String asunto;

    @Schema(description = "Contenido o cuerpo principal de la notificación", example = "Tu evento ha sido actualizado correctamente.")
    private String mensaje;

    @Schema(description = "Fecha y hora en la que se creó la notificación", example = "2025-10-16T12:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Indica si la notificación ya fue leída por el usuario", example = "false")
    private Boolean leido = false;

    @Schema(description = "Identificador del usuario destinatario", example = "1005")
    private Long userId;

    @Schema(description = "ID de la prioridad", example = "1")
    private Long prioridadId;

    @Schema(description = "Nombre de la prioridad", example = "ALTA")
    private String prioridadNombre;

    @Schema(description = "ID del tipo de notificación", example = "1")
    private Long tipoId;

    @Schema(description = "Nombre del tipo de notificación", example = "ALERTA")
    private String tipoNombre;

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

    public String getPrioridadNombre() { return prioridadNombre; }
    public void setPrioridadNombre(String prioridadNombre) { this.prioridadNombre = prioridadNombre; }

    public Long getTipoId() { return tipoId; }
    public void setTipoId(Long tipoId) { this.tipoId = tipoId; }

    public String getTipoNombre() { return tipoNombre; }
    public void setTipoNombre(String tipoNombre) { this.tipoNombre = tipoNombre; }

    /**
     * Convierte este DTO al NotificacionDTO estándar
     */
    public NotificacionDTO toNotificacionDTO() {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(this.id);
        dto.setAsunto(this.asunto);
        dto.setMensaje(this.mensaje);
        dto.setFechaCreacion(this.fechaCreacion);
        dto.setLeido(this.leido);
        dto.setUserId(this.userId);
        
        // Prioridad con nombre
        NotificacionDTO.PrioridadSimpleDTO prioridad = new NotificacionDTO.PrioridadSimpleDTO();
        prioridad.setId(this.prioridadId);
        prioridad.setNombre(this.prioridadNombre);
        dto.setPrioridad(prioridad);
        
        // Tipo con nombre
        NotificacionDTO.TipoSimpleDTO tipo = new NotificacionDTO.TipoSimpleDTO();
        tipo.setId(this.tipoId);
        tipo.setNombre(this.tipoNombre);
        dto.setTipoNotificacion(tipo);
        
        return dto;
    }
}