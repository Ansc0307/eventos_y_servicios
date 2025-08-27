package com.eventos.ms_reservas.model;
import java.time.LocalDateTime;
public class Disponible {
  
   private String id;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean disponible;  // true = disponible, false = ocupado

    public Disponible() {}

    public Disponible(String id, String descripcion, LocalDateTime fechaInicio, LocalDateTime fechaFin, boolean disponible) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
