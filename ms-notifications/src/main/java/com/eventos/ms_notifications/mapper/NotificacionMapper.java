package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    /**
     * Convierte una entidad Notificacion a un DTO NotificacionDTO
     */
    public NotificacionDTO toDTO(Notificacion entidad) {
        if (entidad == null) {
            return null;
        }

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entidad.getId());
        dto.setUserId(entidad.getUserId());
        dto.setAsunto(entidad.getAsunto());
        dto.setMensaje(entidad.getMensaje());
        dto.setFechaCreacion(entidad.getFechaCreacion());
        dto.setLeido(entidad.getLeido());

        // Información de las relaciones
        if (entidad.getPrioridad() != null) {
            dto.setPrioridadId(entidad.getPrioridad().getId());
            dto.setPrioridadNombre(entidad.getPrioridad().getNombre());
            dto.setPrioridadColor(entidad.getPrioridad().getColorHex());
        }

        if (entidad.getTipoNotificacion() != null) {
            dto.setTipoNotificacionId(entidad.getTipoNotificacion().getId());
            dto.setTipoNotificacionNombre(entidad.getTipoNotificacion().getNombre());
            dto.setTipoRequiereAck(entidad.getTipoNotificacion().getRequiereAck());
        }

        return dto;
    }

    /**
     * Convierte un DTO NotificacionDTO a una entidad Notificacion
     * Para creación (sin ID)
     */
    public Notificacion toEntity(NotificacionDTO dto, Prioridad prioridad, TipoNotificacion tipoNotificacion) {
        if (dto == null) {
            return null;
        }

        Notificacion entidad = new Notificacion();
        // No seteamos el ID para creación
        entidad.setUserId(dto.getUserId());
        entidad.setAsunto(dto.getAsunto());
        entidad.setMensaje(dto.getMensaje());
        entidad.setPrioridad(prioridad);
        entidad.setTipoNotificacion(tipoNotificacion);
        entidad.setLeido(dto.getLeido() != null ? dto.getLeido() : false);
        
        // Si se proporciona fecha de creación, se usa; si no, se genera automáticamente
        if (dto.getFechaCreacion() != null) {
            entidad.setFechaCreacion(dto.getFechaCreacion());
        }

        return entidad;
    }

    /**
     * Actualiza una entidad existente con datos del DTO
     * Para actualizaciones (con ID)
     */
    public void updateEntityFromDTO(NotificacionDTO dto, Notificacion entidad, 
                                   Prioridad prioridad, TipoNotificacion tipoNotificacion) {
        if (dto == null || entidad == null) {
            return;
        }

        entidad.setUserId(dto.getUserId());
        entidad.setAsunto(dto.getAsunto());
        entidad.setMensaje(dto.getMensaje());
        
        if (prioridad != null) {
            entidad.setPrioridad(prioridad);
        }
        
        if (tipoNotificacion != null) {
            entidad.setTipoNotificacion(tipoNotificacion);
        }
        
        if (dto.getLeido() != null) {
            entidad.setLeido(dto.getLeido());
        }
    }
}