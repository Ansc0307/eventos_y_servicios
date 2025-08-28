package com.eventos.ms_reservas.dto;

public class EventoDTO {
    private String id;
    private String Descripcion;

    public EventoDTO() {}

    public EventoDTO(String id, String Descripcion) {
        this.id = id;
        this.Descripcion = Descripcion;
    }

    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}

