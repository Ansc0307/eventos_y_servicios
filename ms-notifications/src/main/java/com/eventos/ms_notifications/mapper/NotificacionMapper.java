package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionDTO toDto(Notificacion entity) {
        if (entity == null) return null;

        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(entity.getId());
        dto.setAsunto(entity.getAsunto());
        dto.setMensaje(entity.getMensaje());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setLeido(entity.getLeido());
        dto.setUserId(entity.getUserId());

        // 🔗 Convertir prioridad a su versión simple
        if (entity.getPrioridad() != null) {
            dto.setPrioridad(
                new NotificacionDTO.PrioridadSimpleDTO(
                    entity.getPrioridad().getId(),
                    entity.getPrioridad().getNombre()
                )
            );
        }

        // 🔗 Convertir tipoNotificacion a su versión simple
        if (entity.getTipoNotificacion() != null) {
            dto.setTipoNotificacion(
                new NotificacionDTO.TipoSimpleDTO(
                    entity.getTipoNotificacion().getId(),
                    entity.getTipoNotificacion().getNombre()
                )
            );
        }

        return dto;
    }

    public Notificacion toEntity(NotificacionDTO dto) {
        if (dto == null) return null;

        Notificacion entity = new Notificacion();
        entity.setId(dto.getId());
        entity.setAsunto(dto.getAsunto());
        entity.setMensaje(dto.getMensaje());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setLeido(dto.getLeido());
        entity.setUserId(dto.getUserId());

        // ⚙️ Solo seteamos los objetos relacionados con su ID
        if (dto.getPrioridad() != null) {
            Prioridad prioridad = new Prioridad();
            prioridad.setId(dto.getPrioridad().getId());
            entity.setPrioridad(prioridad);
        }

        if (dto.getTipoNotificacion() != null) {
            TipoNotificacion tipo = new TipoNotificacion();
            tipo.setId(dto.getTipoNotificacion().getId());
            entity.setTipoNotificacion(tipo);
        }

        return entity;
    }
}
