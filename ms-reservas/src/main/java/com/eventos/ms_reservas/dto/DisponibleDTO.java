package com.eventos.ms_reservas.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class DisponibleDTO {

  @Schema(description = "Identificador único de la disponibilidad", example = "10")
    private String id;

    @Schema(description = "Descripción del evento o recurso", example = "Eventos disponibles entre X e Y")
    private String descripcion;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @Schema(description = "Fecha y hora de inicio de la disponibilidad", example = "2025-08-26T10:00:00")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    @Schema(description = "Fecha y hora de fin de la disponibilidad", example = "2025-08-26T11:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Indica si está disponible", example = "true")
    private boolean disponible;

     public DisponibleDTO() {}

    // Constructor completo
    public DisponibleDTO(String id, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, boolean disponible) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.disponible = disponible;
    }




    /* 
    private String id;
    private String descripcion;  // antes descripcion
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean disponible;

    public DisponibleDTO() {}

    public DisponibleDTO(String id, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, boolean disponible) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.disponible = disponible;
    }*/

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "DisponibleDTO [id=" + id + ", descripcion=" + descripcion + ", fechaInicio=" + fechaInicio 
                + ", fechaFin=" + fechaFin + ", disponible=" + disponible +"]";
    }
}