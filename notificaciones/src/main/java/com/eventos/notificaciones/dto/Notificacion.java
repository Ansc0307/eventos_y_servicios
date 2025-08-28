package com.eventos.notificaciones.dto;

/*** este con record es más corto, pero por si acaso lo dejo
public record Notificacion(String id, String titulo, String mensaje) {
}
 ***/

public class Notificacion {
    private String id;
    private String titulo;
    private String mensaje;

    // Constructor vacío
    public Notificacion() {}

    // Constructor con parámetros
    public Notificacion(String id, String titulo, String mensaje) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

