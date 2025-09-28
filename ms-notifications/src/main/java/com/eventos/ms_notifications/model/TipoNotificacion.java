// TipoNotificacion.java
package com.eventos.ms_notifications.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_notificacion")
public class TipoNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nombre; // INFORMATIVA, ALERTA, RECORDATORIO, etc.

    @Column(length = 200)
    private String descripcion;

    @Column(name = "requiere_ack", nullable = false) 
    private Boolean requiereAck = false; // si necesita confirmación de lectura

    @Column(length = 50) 
    private String icono; // ej: fa-bell, fa-warning

    @Column(nullable = false)
    private Boolean activo = true;

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getRequiereAck() {
        return requiereAck;
    }
    public void setRequiereAck(Boolean requiereAck) {
        this.requiereAck = requiereAck;
    }

    public String getIcono() {
        return icono;
    }
    public void setIcono(String icono) {
        this.icono = icono;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    //Constructores
    public TipoNotificacion() {}
    
    public TipoNotificacion(String nombre, String descripcion, Boolean requiereAck, String icono, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.requiereAck = requiereAck;
        this.icono = icono;
        this.activo = activo;
    }
}