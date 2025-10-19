package com.eventos.ms_reservas.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class NoDisponibilidadDTO {

    @Schema(description = "Identificador único de la no disponibilidad", example = "1")
    private Integer idNoDisponibilidad;

    @NotNull(message = "El ID de la oferta es obligatorio")
    @Positive(message = "El ID de la oferta debe ser un número positivo")
    @Schema(description = "Identificador de la oferta asociada", example = "303")
    private Integer idOferta;

    @NotBlank(message = "El motivo no puede estar vacío")
    @Size(max = 100, message = "El motivo no puede superar los 100 caracteres")
    @Schema(description = "Motivo por el cual el servicio o local no está disponible", example = "Mantenimiento del local")
    private String motivo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio de la no disponibilidad", example = "2025-10-16T09:00:00")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de fin de la no disponibilidad", example = "2025-10-17T18:00:00")
    private LocalDateTime fechaFin;

    @Schema(description = "Identificador de la reserva asociada (si existe)", example = "15")
    private Integer idReserva;

   public NoDisponibilidadDTO(Integer idNoDisponibilidad, Integer idOferta, String motivo, LocalDateTime fechaInicio, LocalDateTime fechaFin, Integer idReserva) {
    this.idNoDisponibilidad = idNoDisponibilidad;
    this.idOferta = idOferta;
    this.motivo = motivo;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.idReserva = idReserva;
}

public NoDisponibilidadDTO() {
    // Constructor vacío requerido para mapeos y frameworks
}
    // getters and setters


    // Getters y Setters
    public Integer getIdNoDisponibilidad() { return idNoDisponibilidad; }
    public void setIdNoDisponibilidad(Integer idNoDisponibilidad) { this.idNoDisponibilidad = idNoDisponibilidad; }

    public Integer getIdOferta() { return idOferta; }
    public void setIdOferta(Integer idOferta) { this.idOferta = idOferta; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
}