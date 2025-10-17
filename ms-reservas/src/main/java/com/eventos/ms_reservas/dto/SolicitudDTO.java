package com.eventos.ms_reservas.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class SolicitudDTO {

    @Schema(description = "Identificador único de la solicitud", example = "1")
    private Integer idSolicitud;

    @NotNull(message = "La fecha de la solicitud no puede ser nula")
    @PastOrPresent(message = "La fecha de la solicitud no puede estar en el futuro")
    @Schema(description = "Fecha en que se realizó la solicitud", example = "2025-10-01T15:30:00")
    private LocalDateTime fechaSolicitud;

    @NotBlank(message = "El estado de la solicitud no puede estar vacío")
    @Schema(description = "Estado actual de la solicitud", example = "pendiente")
    private String estadoSolicitud;

    @NotNull(message = "El ID del organizador es obligatorio")
    @Positive(message = "El ID del organizador debe ser un número positivo")
    @Schema(description = "Identificador del organizador que realiza la solicitud", example = "101")
    private Integer idOrganizador;

    @NotNull(message = "El ID del proveedor es obligatorio")
    @Positive(message = "El ID del proveedor debe ser un número positivo")
    @Schema(description = "Identificador del proveedor asociado a la solicitud", example = "202")
    private Integer idProovedor;

    @NotNull(message = "El ID de la oferta es obligatorio")
    @Positive(message = "El ID de la oferta debe ser un número positivo")
    @Schema(description = "Identificador de la oferta relacionada con la solicitud", example = "303")
    private Integer idOferta;

    // Constructor vacío
    public SolicitudDTO() {}

    // Constructor con campos
    public SolicitudDTO(Integer idSolicitud, LocalDateTime fechaSolicitud, String estadoSolicitud, Integer idOrganizador, Integer idProovedor, Integer idOferta) {
        this.idSolicitud = idSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.estadoSolicitud = estadoSolicitud;
        this.idOrganizador = idOrganizador;
        this.idProovedor = idProovedor;
        this.idOferta = idOferta;
    }

    // Getters y Setters
    public Integer getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Integer idSolicitud) { this.idSolicitud = idSolicitud; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getEstadoSolicitud() { return estadoSolicitud; }
    public void setEstadoSolicitud(String estadoSolicitud) { this.estadoSolicitud = estadoSolicitud; }

    public Integer getIdOrganizador() { return idOrganizador; }
    public void setIdOrganizador(Integer idOrganizador) { this.idOrganizador = idOrganizador; }

    public Integer getIdProovedor() { return idProovedor; }
    public void setIdProovedor(Integer idProovedor) { this.idProovedor = idProovedor; }

    public Integer getIdOferta() { return idOferta; }
    public void setIdOferta(Integer idOferta) { this.idOferta = idOferta; }
}