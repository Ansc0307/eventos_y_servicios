// TipoNotificacion.java
package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tipo_notificacion")
public class TipoNotificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del tipo de notificacion", example = "1", required = true)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(description = "Nombre del tipo de notificacion INFORMATIVA, ALERTA, RECORDATORIO, PROMOCION o SISTEMA", example = "INFORMATIVA")
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    @Schema(description = "Descripción opcional del tipo de notificacion", example = "Notificación informativa para el usuario")
    private String descripcion;

    // Constructores
    public TipoNotificacion() {}

    public TipoNotificacion(Long id, String nombre, String descripcion) {
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