package com.eventos.ms_reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para Evento")
public class EventoDTO {

    @Schema(description = "Identificador único del evento", example = "evt123", required = true)
    @NotBlank(message = "El id no puede estar vacío")
    @Size(max = 50, message = "El id no puede tener más de 50 caracteres")
    private String id;

    @Schema(description = "Descripción del evento", example = "Concierto de rock", required = true)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    public EventoDTO() {}

    public EventoDTO(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}