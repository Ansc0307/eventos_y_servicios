package com.eventos.ms_reservas.model;

import java.time.LocalDateTime;

public class Reserva {
    private Long idReserva;
    private Integer idSolicitud;
    private LocalDateTime fechaReservaInicio;
    private LocalDateTime fechaReservaFin;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Reserva() {}

    public Reserva(Long idReserva, Integer idSolicitud, LocalDateTime fechaReservaInicio, 
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
