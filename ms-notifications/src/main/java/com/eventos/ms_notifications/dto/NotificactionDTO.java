package com.eventos.ms_notifications.dto;
import java.time.LocalDateTime;

public class NotificactionDTO {
    private Long id;
    private Long userId;
    private String asunto;
    private String mensaje;
    private String prioridad;
    private LocalDateTime fechaCreacion;
    private Boolean leido;
    private String tipoNotificacion;

    //Constructores
    public NotificactionDTO() {}

    public NotificactionDTO(Long id, Long userId, String asunto, String mensaje, String prioridad, LocalDateTime fechaCreacion, Boolean leido, String tipoNotificacion) {
        this.id = id;
        this.userId = userId;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.prioridad = prioridad;
        this.fechaCreacion = fechaCreacion;
        this.leido = leido;
        this.tipoNotificacion = tipoNotificacion;
    }

    //Getters y Setters
    public Long getId() {
        return id;
    } 
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAsunto() {
        return asunto;
    }
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPrioridad() {
        return prioridad;
    }
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getLeido() {
        return leido;
    }
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }
    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

}
