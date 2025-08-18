package com.eventos.reservas.dto;
import java.time.LocalDateTime;

public class SolicitudDTO {
    private String id;
    private String nombreRecurso;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;

    public SolicitudDTO() {}

    public SolicitudDTO(String id, String nombreRecurso, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado) {
        this.id = id;
        this.nombreRecurso = nombreRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

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
