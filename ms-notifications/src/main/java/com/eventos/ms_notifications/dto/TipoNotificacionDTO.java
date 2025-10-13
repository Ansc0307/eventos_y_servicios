package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO que representa un tipo de notificación dentro del sistema")
public class TipoNotificacionDTO {

    @Schema(description = "ID del tipo de notificación", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(
        description = "Nombre del tipo de notificación (ej. INFORMATIVA, ALERTA, RECORDATORIO, PROMOCION o SISTEMA)",
        example = "ALERTA",
        required = true
    )
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    @Schema(
        description = "Descripción opcional del tipo de notificación",
        example = "Notificación para alertar al usuario sobre un evento importante"
    )
    private String descripcion;

    // Constructores
    public TipoNotificacionDTO() {}

    public TipoNotificacionDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
