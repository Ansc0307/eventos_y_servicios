package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.stereotype.Component;

@Component
public class TipoNotificacionMapper {

    /**
     * Convierte una entidad TipoNotificacion a un DTO TipoNotificacionDTO
     */
    public TipoNotificacionDTO toDTO(TipoNotificacion entidad) {
        if (entidad == null) {
            return null;
        }

        TipoNotificacionDTO dto = new TipoNotificacionDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setRequiereAck(entidad.getRequiereAck());
        dto.setIcono(entidad.getIcono());
        dto.setActivo(entidad.getActivo());
        return dto;
    }

    /**
     * Convierte un DTO TipoNotificacionDTO a una entidad TipoNotificacion
     * Para creación (sin ID)
     */
    public TipoNotificacion toEntity(TipoNotificacionDTO dto) {
        if (dto == null) {
            return null;
        }

        TipoNotificacion entidad = new TipoNotificacion();
        // No seteamos el ID para creación
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setRequiereAck(dto.getRequiereAck() != null ? dto.getRequiereAck() : false);
        entidad.setIcono(dto.getIcono());
        entidad.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return entidad;
    }

    /**
     * Actualiza una entidad existente con datos del DTO
     * Para actualizaciones (con ID)
     */
    public void updateEntityFromDTO(TipoNotificacionDTO dto, TipoNotificacion entidad) {
        if (dto == null || entidad == null) {
            return;
        }

        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        
        if (dto.getRequiereAck() != null) {
            entidad.setRequiereAck(dto.getRequiereAck());
        }
        
        entidad.setIcono(dto.getIcono());
        
        if (dto.getActivo() != null) {
            entidad.setActivo(dto.getActivo());
        }
    }
}