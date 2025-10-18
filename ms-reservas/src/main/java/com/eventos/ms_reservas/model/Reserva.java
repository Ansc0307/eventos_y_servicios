package com.eventos.ms_reservas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    // Relación conceptual OneToOne con Solicitud (entidad externa en otro microservicio)
    @Column(name = "id_solicitud", nullable = false)
    private Integer idSolicitud;

    @Column(name = "fecha_reserva_inicio", nullable = false)
    private LocalDateTime fechaReservaInicio;

    @Column(name = "fecha_reserva_fin", nullable = false)
    private LocalDateTime fechaReservaFin;

    @Column(name = "estado", length = 100, nullable = false)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación OneToOne con NoDisponibilidad
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NoDisponibilidad noDisponibilidad;

    public Reserva() {}

    public Reserva(Integer idReserva, Integer idSolicitud, LocalDateTime fechaReservaInicio, 
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
    public Integer getIdReserva() {
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
    public void setIdReserva(Integer idReserva) {
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

    public NoDisponibilidad getNoDisponibilidad() {
        return noDisponibilidad;
    }

    public void setNoDisponibilidad(NoDisponibilidad noDisponibilidad) {
        this.noDisponibilidad = noDisponibilidad;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
