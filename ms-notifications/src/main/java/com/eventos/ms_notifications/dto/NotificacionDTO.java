package com.eventos.ms_notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "DTO que representa una notificación dentro del sistema.")
public class NotificacionDTO {

    @Schema(description = "Identificador único de la notificación", example = "1")
    private Long id;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 100, message = "El asunto no puede superar los 100 caracteres")
    @Schema(description = "Asunto o título breve de la notificación", example = "Actualización de evento")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Contenido o cuerpo principal de la notificación", example = "Tu evento ha sido actualizado correctamente.")
    private String mensaje;

    @Schema(description = "Fecha y hora en la que se creó la notificación", example = "2025-10-16T12:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Indica si la notificación ya fue leída por el usuario", example = "false")
    private Boolean leido = false;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(description = "Objeto que representa la prioridad asociada a la notificación")
    private PrioridadSimpleDTO prioridad;

    @NotNull(message = "El tipo de notificación es obligatorio")
    @Schema(description = "Objeto que representa el tipo de notificación asociado")
    private TipoSimpleDTO tipoNotificacion;

    @NotNull(message = "El userId es obligatorio")
    @Schema(description = "Identificador del usuario destinatario", example = "1005")
    private Long userId;

    // Subclases internas simples para evitar anidación infinita
    public static class PrioridadSimpleDTO {
        @Schema(example = "1")
        private Long id;
        @Schema(example = "ALTA")
        private String nombre;

        public PrioridadSimpleDTO() {}
        public PrioridadSimpleDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class TipoSimpleDTO {
        @Schema(example = "1")
        private Long id;
        @Schema(example = "ALERTA")
        private String nombre;

        public TipoSimpleDTO() {}
        public TipoSimpleDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    // Getters y Setters principales
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    public PrioridadSimpleDTO getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadSimpleDTO prioridad) { this.prioridad = prioridad; }

    public TipoSimpleDTO getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(TipoSimpleDTO tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
