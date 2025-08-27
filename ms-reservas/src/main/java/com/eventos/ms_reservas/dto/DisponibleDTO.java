package com.eventos.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DisponibleDTO {

    @Schema(description = "Identificador de la disponibilidad", example = "1")
    private Long id;

    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 3, max = 20, message = "El estado debe tener entre 3 y 20 caracteres")
    @Schema(description = "Estado de la disponibilidad (ejemplo: DISPONIBLE, OCUPADO)", example = "DISPONIBLE")
    private String estado;

    @NotNull(message = "La capacidad no puede ser nula")
    @Schema(description = "Número máximo de personas disponibles", example = "50")
    private Integer capacidad;

    private String serviceAddress;

    // Constructor vacío
    public DisponibleDTO() {}

    // Constructor con parámetros
    public DisponibleDTO(Long id, String estado, Integer capacidad, String serviceAddress) {
        this.id = id;
        this.estado = estado;
        this.capacidad = capacidad;
        this.serviceAddress = serviceAddress;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getServiceAddress() { return serviceAddress; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }

    @Override
    public String toString() {
        return "DisponibleDTO [id=" + id + ", estado=" + estado + ", capacidad=" + capacidad 
                + ", serviceAddress=" + serviceAddress + "]";
    }
}
