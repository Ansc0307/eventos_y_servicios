package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un tipo de notificación")
public class TipoNotificacionDTO {

    @Schema(description = "ID del tipo de notificación", example = "2")
    private Long id;

    @Schema(description = "Nombre del tipo", example = "ALERTA")
    private String nombre;

    @Schema(description = "Descripción del tipo", example = "Notificación que alerta al usuario de un evento importante")
    private String descripcion;

    @Schema(description = "Si requiere confirmación de lectura", example = "true")
    private Boolean requiereAck;

    @Schema(description = "Ícono representativo", example = "fa-warning")
    private String icono;

    @Schema(description = "Si el tipo está activo", example = "true")
    private Boolean activo;

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

    public Boolean getRequiereAck() {
        return requiereAck;
    }
    public void setRequiereAck(Boolean requiereAck) {
        this.requiereAck = requiereAck;
    }

    public String getIcono() {
        return icono;
    }
    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
