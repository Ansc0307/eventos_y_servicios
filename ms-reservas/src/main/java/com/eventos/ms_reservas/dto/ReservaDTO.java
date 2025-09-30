package com.eventos.ms_reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para Reserva")
public class ReservaDTO {

    @Schema(description = "Identificador único de la reserva", example = "1", required = false)
    private Long idReserva;

    @Schema(description = "Identificador de la solicitud", example = "123", required = true)
    @NotNull(message = "El id de solicitud no puede estar vacío")
    private Integer idSolicitud;

    @Schema(description = "Fecha de inicio de la reserva", required = true)
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    private LocalDateTime fechaReservaInicio;

    @Schema(description = "Fecha de fin de la reserva", required = true)
    @NotNull(message = "La fecha de fin no puede estar vacía")
    private LocalDateTime fechaReservaFin;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMADA", required = true)
    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 100, message = "El estado no puede tener más de 100 caracteres")
    private String estado;

    @Schema(description = "Fecha de creación de la reserva", required = false)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", required = false)
    private LocalDateTime fechaActualizacion;

    public ReservaDTO() {}

    public ReservaDTO(Long idReserva, Integer idSolicitud, LocalDateTime fechaReservaInicio, 
                      LocalDateTime fechaReservaFin, String estado, LocalDateTime fechaCreacion, 
                      LocalDateTime fechaActualizacion) {
        this.idReserva = idReserva;
        this.idSolicitud = idSolicitud;
        this.fechaReservaInicio = fechaReservaInicio;
        this.fechaReservaFin = fechaReservaFin;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters
    public Long getIdReserva() {
        return idReserva;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public LocalDateTime getFechaReservaInicio() {
        return fechaReservaInicio;
    }

    public LocalDateTime getFechaReservaFin() {
        return fechaReservaFin;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    // Setters
    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public void setFechaReservaInicio(LocalDateTime fechaReservaInicio) {
        this.fechaReservaInicio = fechaReservaInicio;
    }

    public void setFechaReservaFin(LocalDateTime fechaReservaFin) {
        this.fechaReservaFin = fechaReservaFin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}