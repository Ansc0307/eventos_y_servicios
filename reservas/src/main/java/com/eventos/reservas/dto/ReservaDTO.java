package com.eventos.reservas.dto;

public class ReservaDTO {
    private String id;
    private String estado;

    public ReservaDTO() {}

    public ReservaDTO(String id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
