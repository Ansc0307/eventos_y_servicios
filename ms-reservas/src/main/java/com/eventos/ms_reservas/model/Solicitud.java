package com.eventos.ms_reservas.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "estado_solicitud", length = 100, nullable = false)
    private String estadoSolicitud;

    @Column(name = "id_organizador", nullable = false)
    private Integer idOrganizador;

    @Column(name = "id_proovedor", nullable = false)
    private Integer idProovedor;

    @Column(name = "id_oferta", nullable = false)
    private Integer idOferta;

    // Getters y Setters
    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public Integer getIdOrganizador() {
        return idOrganizador;
    }

    public void setIdOrganizador(Integer idOrganizador) {
        this.idOrganizador = idOrganizador;
    }

    public Integer getIdProovedor() {
        return idProovedor;
    }

    public void setIdProovedor(Integer idProovedor) {
        this.idProovedor = idProovedor;
    }

    public Integer getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Integer idOferta) {
        this.idOferta = idOferta;
    }
}