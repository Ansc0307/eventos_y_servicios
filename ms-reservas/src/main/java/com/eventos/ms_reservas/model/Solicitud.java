package com.eventos.ms_reservas.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Solicitud {


      @Schema(description = "Identificador único de la solicitud", example = "1")
    private String id;

    @NotBlank(message = "El nombre del recurso es obligatorio")
    @Schema(description = "Nombre del recurso solicitado", example = "Reserva de salón de eventos")
    private String nombreRecurso;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    @Schema(description = "Fecha y hora de inicio de la reserva", example = "2025-08-26T18:00:00")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    @Schema(description = "Fecha y hora de fin de la reserva", example = "2025-08-26T21:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Estado actual de la solicitud", example = "aceptada")
    private String estado;






    /*private String id;
    private String nombreRecurso;    // antes descripcion
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;           // "pendiente", "aprobada", "rechazada"

    public Solicitud() {}

    public Solicitud(String id, String nombreRecurso, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado) {
        this.id = id;
        this.nombreRecurso = nombreRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }*/

    public String getId() {
        return id;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
