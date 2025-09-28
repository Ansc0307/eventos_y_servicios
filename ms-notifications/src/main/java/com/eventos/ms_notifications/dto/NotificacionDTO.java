package com.eventos.ms_notifications.dto;

import java.util.List;

public class NotificacionDTO {
    private String titulo;
    private String mensaje;
    private String tipo; // INFO, ALERTA, RECORDATORIO
    private List<Long> destinatariosIds;

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Long> getDestinatariosIds() {
        return destinatariosIds;
    }
    public void setDestinatariosIds(List<Long> destinatariosIds) {
        this.destinatariosIds = destinatariosIds;
    }
}
