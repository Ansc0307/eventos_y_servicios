package com.eventos.ms_notifications.dto;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa una notificación dentro del sistema")
public class NotificactionDTO {
    
    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @Schema(description = "Identificador del usuario al que va dirigida la notificación", example = "1001")
    private Long userId;
    
    @Schema(description = "Asunto o título de la notificación", example = "Nueva reserva creada")
    private String asunto;

    @Schema(description = "Mensaje detallado de la notificación", example = "Su reserva ha sido creada exitosamente.")
    private String mensaje;

    @Schema(description = "Prioridad de la notificación", example = "alta")
    private String prioridad;

    @Schema(description = "Fecha y hora de creación de la notificación", example = "2024-06-15T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Indica si la notificación ha sido leída", example = "false")
    private Boolean leido;

    @Schema(description = "Tipo de notificación", example = "reserva, evento, general")
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
