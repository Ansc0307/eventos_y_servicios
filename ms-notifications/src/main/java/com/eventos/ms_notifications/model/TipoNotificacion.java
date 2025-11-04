package com.eventos.ms_notifications.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("tipo_notificacion")
@Schema(description = "Entidad que representa los tipos de notificación (ej. ALERTA, INFORMATIVA, SISTEMA).")
public class TipoNotificacion {

    @Id
    @Schema(description = "ID del tipo de notificación", example = "1", required = true)
    private Long id;

    @Column("nombre")
    @Schema(description = "Nombre del tipo de notificación", example = "ALERTA")
    private String nombre;

    @Column("descripcion")
    @Schema(description = "Descripción opcional del tipo de notificación", example = "Notificación informativa para el usuario")
    private String descripcion;

    public TipoNotificacion() {}

    public TipoNotificacion(Long id, String nombre, String descripcion) {
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
