package com.eventos.notifications.dto;

/*** este con record es más corto, pero por si acaso lo dejo
 public record Notificacion(String id, String titulo, String mensaje) {
 }
 ***/

public class NotificationDTO {
    private String id;
    private String titulo;
    private String mensaje;
    private boolean leido;

    // Constructor vacío
    public NotificationDTO() {}

    // Constructor con parámetros
    public NotificationDTO(String id, String titulo, String mensaje, boolean leido) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.leido = leido;
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

    public boolean isLeido() {
        return leido;
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

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}


