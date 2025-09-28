package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para devolver información de una notificación")
public class NotificacionResponseDTO {

    @Schema(description = "ID único de la notificación", example = "1001")
    private Long id;

    @Schema(description = "ID del usuario destinatario", example = "123")
    private Long userId;

    @Schema(description = "Prioridad de la notificación")
    private PrioridadDTO prioridad;

    @Schema(description = "Tipo de la notificación")
    private TipoNotificacionDTO tipo;

    @Schema(description = "Asunto de la notificación", example = "Reunión programada")
    private String asunto;

    @Schema(description = "Mensaje de la notificación", example = "La reunión será el 10 de octubre a las 9am")
    private String mensaje;

    @Schema(description = "Fecha de creación de la notificación", example = "2025-09-27T22:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Si la notificación fue leída", example = "false")
    private Boolean leido;

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

    public PrioridadDTO getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(PrioridadDTO prioridad) {
        this.prioridad = prioridad;
    }

    public TipoNotificacionDTO getTipo() {
        return tipo;
    }
    public void setTipo(TipoNotificacionDTO tipo) {
        this.tipo = tipo;
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
}
