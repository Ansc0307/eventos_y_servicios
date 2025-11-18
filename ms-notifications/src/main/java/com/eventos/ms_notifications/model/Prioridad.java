package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("prioridad")
@Schema(description = "Entidad que representa los niveles de prioridad de las notificaciones.")
public class Prioridad {

    @Id
    @Schema(description = "ID de la prioridad", example = "4", required = true)
    private Long id;

    @Column("nombre")
    @Schema(description = "Nombre de la prioridad (ej. ALTA, MEDIA, BAJA)", example = "URGENTE")
    private String nombre;

    @Column("descripcion")
    @Schema(description = "Descripción opcional de la prioridad", example = "Atención inmediata")
    private String descripcion;

    public Prioridad() {}

    public Prioridad(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
