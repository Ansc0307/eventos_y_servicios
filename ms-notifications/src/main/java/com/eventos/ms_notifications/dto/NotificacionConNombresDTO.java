package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para resultados de consulta con JOIN de notificaciones")
public class NotificacionConNombresDTO {

    private Long id;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private Boolean leido;
    private Long userId;

    private Long prioridadId;
    private String prioridadNombre;

    private Long tipoId;
    private String tipoNombre;

    // =====================
    // Getters & Setters
    // =====================

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

    // =====================
    // Mapper a DTO público
    // =====================

    public NotificacionDTO toNotificacionDTO() {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(this.id);
        dto.setAsunto(this.asunto);
        dto.setMensaje(this.mensaje);
        dto.setFechaCreacion(this.fechaCreacion);
        dto.setLeido(this.leido);
        dto.setUserId(this.userId);

        NotificacionDTO.PrioridadSimpleDTO prioridad = new NotificacionDTO.PrioridadSimpleDTO();
        prioridad.setId(this.prioridadId);
        prioridad.setNombre(this.prioridadNombre);
        dto.setPrioridad(prioridad);

        NotificacionDTO.TipoSimpleDTO tipo = new NotificacionDTO.TipoSimpleDTO();
        tipo.setId(this.tipoId);
        tipo.setNombre(this.tipoNombre);
        dto.setTipoNotificacion(tipo);

        return dto;
    }
}
