package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear una nueva notificación")
public class NotificacionCreateDTO {

    @Schema(description = "ID del usuario destinatario", example = "123")
    @NotNull(message = "El userId no puede ser nulo")
    private Long userId;

    @Schema(description = "ID de la prioridad", example = "1")
    @NotNull(message = "La prioridad es obligatoria")
    private Long prioridadId;

    @Schema(description = "ID del tipo de notificación", example = "2")
    @NotNull(message = "El tipo de notificación es obligatorio")
    private Long tipoId;

    @Schema(description = "Asunto de la notificación", example = "Reunión programada")
    @NotBlank(message = "El asunto no puede estar vacío")
    @Size(max = 100, message = "El asunto no puede superar 100 caracteres")
    private String asunto;

    @Schema(description = "Mensaje detallado de la notificación", example = "La reunión será el 10 de octubre a las 9am")
    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar 500 caracteres")
    private String mensaje;

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrioridadId() {
        return prioridadId;
    }
    public void setPrioridadId(Long prioridadId) {
        this.prioridadId = prioridadId;
    }

    public Long getTipoId() {
        return tipoId;
    }
    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
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
}
