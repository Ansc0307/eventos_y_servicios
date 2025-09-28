package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa una prioridad")
public class PrioridadDTO {

    @Schema(description = "ID de la prioridad", example = "1")
    private Long id;

    @Schema(description = "Nombre de la prioridad", example = "ALTA")
    private String nombre;

    @Schema(description = "Descripción de la prioridad", example = "Atención inmediata")
    private String descripcion;

    @Schema(description = "Nivel jerárquico (1=alta, 2=media, 3=baja)", example = "1")
    private Integer nivel;

    @Schema(description = "Color en formato HEX", example = "#FF0000")
    private String colorHex;

    @Schema(description = "Si está activa la prioridad", example = "true")
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

    public Integer getNivel() {
        return nivel;
    }
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getColorHex() {
        return colorHex;
    }
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
