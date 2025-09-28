// Prioridad.java
package com.eventos.ms_notifications.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prioridad")
public class Prioridad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String nombre; // ALTA, MEDIA, BAJA

    @Column(length = 200) 
    private String descripcion; 
    
    @Column(nullable = false) 
    private Integer nivel; // jerarquía (1=alta, 2=media, 3=baja) 
    
    @Column(name = "color_hex", length = 7) 
    private String colorHex; // Ejemplo: #FF0000 
    
    @Column(nullable = false) private 
    Boolean activo = true;

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

    public Integer getNivel() {
        return nivel;
    }
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getColorHex() {
        return colorHex;
    }
    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    //Constructores
    public Prioridad() {}

    public Prioridad(String nombre, String descripcion, Integer nivel, String colorHex, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.colorHex = colorHex;
        this.activo = activo;
    }

}
