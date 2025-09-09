package com.eventos.ms_reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para Reserva")
public class ReservaDTO {

    @Schema(description = "Identificador único de la reserva", example = "res123", required = true)
    @NotBlank(message = "El id no puede estar vacío")
    @Size(max = 50, message = "El id no puede tener más de 50 caracteres")
    private String id;

    @Schema(description = "Estado actual de la reserva", example = "CONFIRMADA", required = true)
    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 20, message = "El estado no puede tener más de 20 caracteres")
    private String estado;

    public ReservaDTO() {}

    public ReservaDTO(String id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}