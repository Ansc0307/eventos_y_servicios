package com.eventos.ms_notifications.dto;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO que representa una notificación dentro del sistema")
public class NotificationDTO {
    
    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @NotNull(message = "El userId no puede ser nulo")
    @Positive(message = "El userId debe ser positivo")
    @Schema(description = "Identificador del usuario al que va dirigida la notificación", example = "1001")
    private Long userId;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 100, message = "El asunto no puede superar 100 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar 500 caracteres")
    private String mensaje;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "BAJA|MEDIA|ALTA", message = "La prioridad debe ser BAJA, MEDIA o ALTA")
    private String prioridad;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El campo leido no puede ser nulo")
    private Boolean leido;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    @Pattern(regexp = "INFORMATIVA|ALERTA|RECORDATORIO|PROMOCION|SISTEMA", message = "El tipo de notificación debe ser INFORMATIVA, ALERTA, RECORDATORIO, PROMOCION o SISTEMA")
    private String tipoNotificacion;

    //Constructores
    public NotificationDTO() {}

    public NotificationDTO(Long id, Long userId, String asunto, String mensaje, String prioridad, LocalDateTime fechaCreacion, Boolean leido, String tipoNotificacion) {
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
