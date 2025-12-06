package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    // Convierte de entidad a DTO
    public NotificacionDTO toDto(Notificacion entity) {
        if (entity == null) return null;

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entity.getId());
        dto.setAsunto(entity.getAsunto());
        dto.setMensaje(entity.getMensaje());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setLeido(entity.getLeido());
        dto.setUserId(entity.getUserId());

        // Convertir prioridadId a PrioridadSimpleDTO
        if (entity.getPrioridadId() != null) {
            dto.setPrioridad(new NotificacionDTO.PrioridadSimpleDTO(
                    entity.getPrioridadId(),
                    null // El nombre no lo tenemos, se podría setear desde servicio si es necesario
            ));
        }

        // Convertir tipoNotificacionId a TipoSimpleDTO
        if (entity.getTipoNotificacionId() != null) {
            dto.setTipoNotificacion(new NotificacionDTO.TipoSimpleDTO(
                    entity.getTipoNotificacionId(),
                    null // El nombre no lo tenemos, se podría setear desde servicio si es necesario
            ));
        }

        return dto;
    }

    // Convierte de DTO a entidad
    public Notificacion toEntity(NotificacionDTO dto) {
        if (dto == null) return null;

        Notificacion entity = new Notificacion();
        entity.setId(dto.getId());
        entity.setAsunto(dto.getAsunto());
        entity.setMensaje(dto.getMensaje());
        // Si fechaCreacion es null, usar LocalDateTime.now()
        entity.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : java.time.LocalDateTime.now());
        entity.setLeido(dto.getLeido());
        entity.setUserId(dto.getUserId());

        // Solo seteamos los IDs relacionados
        if (dto.getPrioridad() != null) {
            entity.setPrioridadId(dto.getPrioridad().getId());
        }

        if (dto.getTipoNotificacion() != null) {
            entity.setTipoNotificacionId(dto.getTipoNotificacion().getId());
        }

        return entity;
    }
}
