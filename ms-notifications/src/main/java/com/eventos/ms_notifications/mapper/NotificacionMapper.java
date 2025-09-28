package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.NotificacionCreateDTO;
import com.eventos.ms_notifications.dto.NotificacionResponseDTO;
import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;

public class NotificacionMapper {

    // Convertir de DTO de creación -> Entidad
    public static Notificacion toEntity(NotificacionCreateDTO dto, Prioridad prioridad, TipoNotificacion tipo) {
        Notificacion entity = new Notificacion();
        entity.setUserId(dto.getUserId());
        entity.setAsunto(dto.getAsunto());
        entity.setMensaje(dto.getMensaje());
        entity.setPrioridad(prioridad);
        entity.setTipoNotificacion(tipo);
        // fechaCreacion y leido se setean por defecto en la entidad
        return entity;
    }

    // Convertir de Entidad -> DTO de respuesta
    public static NotificacionResponseDTO toResponseDTO(Notificacion entity) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());

        // Mapear prioridad
        PrioridadDTO prioridadDTO = new PrioridadDTO();
        prioridadDTO.setId(entity.getPrioridad().getId());
        prioridadDTO.setNombre(entity.getPrioridad().getNombre());
        dto.setPrioridad(prioridadDTO);

        // Mapear tipo de notificación
        TipoNotificacionDTO tipoDTO = new TipoNotificacionDTO();
        tipoDTO.setId(entity.getTipoNotificacion().getId());
        tipoDTO.setNombre(entity.getTipoNotificacion().getNombre());
        dto.setTipo(tipoDTO);

        dto.setAsunto(entity.getAsunto());
        dto.setMensaje(entity.getMensaje());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setLeido(entity.getLeido());

        return dto;
    }
}
