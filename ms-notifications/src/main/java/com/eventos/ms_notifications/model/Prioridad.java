package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "prioridad")
@Schema(description = "Entidad que representa una prioridad en el sistema")
public class Prioridad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la prioridad", example = "1")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    @Schema(description = "Nombre único de la prioridad", example = "ALTA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    @Schema(description = "Descripción detallada de la prioridad", example = "Atención inmediata requerida")
    private String descripcion;

    @Column(name = "nivel", nullable = false)
    @Schema(description = "Nivel jerárquico (1=alta, 2=media, 3=baja)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer nivel;

    @Column(name = "color_hex", length = 7)
    @Schema(description = "Color en formato HEX", example = "#FF0000")
    private String colorHex;

    @Column(name = "activo", nullable = false)
    @Schema(description = "Estado de la prioridad", example = "true")
    private Boolean activo = true;

    // Constructores, Getters y Setters
    public Prioridad() {}

    public Prioridad(String nombre, String descripcion, Integer nivel, String colorHex, Boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.colorHex = colorHex;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}