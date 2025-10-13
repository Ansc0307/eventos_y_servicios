package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "prioridad")
@Schema(description = "Entidad que representa los niveles de prioridad de las notificaciones")
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la prioridad", example = "4", required = true)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(description = "Nombre de la prioridad (ej. ALTA, MEDIA, BAJA)", example = "URGENTE")
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    @Schema(description = "Descripción opcional de la prioridad", example = "Atención inmediata")
    private String descripcion;

    // Constructores
    public Prioridad() {}

    public Prioridad(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

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
}

