package com.eventos.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SolicitudDTO {

    @Schema(description = "Identificador de la solicitud", example = "101")
    private Long id;

    @NotBlank(message = "El nombre del solicitante es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre completo del solicitante", example = "Juan Pérez")
    private String solicitante;

    @NotBlank(message = "El evento es obligatorio")
    @Size(min = 3, max = 100, message = "El evento debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre del evento solicitado", example = "Conferencia de Tecnología")
    private String evento;

    @NotNull(message = "La cantidad de asistentes no puede ser nula")
    @Schema(description = "Número de asistentes esperados", example = "120")
    private Integer asistentes;

    private String serviceAddress;

    // Constructor vacío
    public SolicitudDTO() {}

    // Constructor con parámetros
    public SolicitudDTO(Long id, String solicitante, String evento, Integer asistentes, String serviceAddress) {
        this.id = id;
        this.solicitante = solicitante;
        this.evento = evento;
        this.asistentes = asistentes;
        this.serviceAddress = serviceAddress;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public Integer getAsistentes() { return asistentes; }
    public void setAsistentes(Integer asistentes) { this.asistentes = asistentes; }

    public String getServiceAddress() { return serviceAddress; }
    public void setServiceAddress(String serviceAddress) { this.serviceAddress = serviceAddress; }

    @Override
    public String toString() {
        return "SolicitudDTO [id=" + id + ", solicitante=" + solicitante 
                + ", evento=" + evento + ", asistentes=" + asistentes 
                + ", serviceAddress=" + serviceAddress + "]";
    }
}
