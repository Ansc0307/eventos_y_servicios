package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO que representa un tipo de notificación")
public class TipoNotificacionDTO {

    @Schema(description = "ID del tipo de notificación", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 30, message = "El nombre no puede exceder los 30 caracteres")
    @Schema(description = "Nombre único del tipo de notificación", example = "ALERTA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    @Schema(description = "Descripción del tipo de notificación", example = "Notificación que alerta al usuario de un evento importante")
    private String descripcion;

    @Schema(description = "Si requiere confirmación de lectura", example = "true")
    private Boolean requiereAck = false;

    @Size(max = 50, message = "El ícono no puede exceder los 50 caracteres")
    @Schema(description = "Ícono representativo", example = "fa-warning")
    private String icono;

    @Schema(description = "Si el tipo está activo", example = "true")
    private Boolean activo = true;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getRequiereAck() { return requiereAck; }
    public void setRequiereAck(Boolean requiereAck) { this.requiereAck = requiereAck; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}