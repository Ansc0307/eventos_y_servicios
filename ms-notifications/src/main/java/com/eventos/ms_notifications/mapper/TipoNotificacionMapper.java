package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.TipoNotificacion;

public class TipoNotificacionMapper {

    // De Entidad -> DTO
    public static TipoNotificacionDTO toDTO(TipoNotificacion entidad) {
        if(entidad == null) return null;

        TipoNotificacionDTO dto = new TipoNotificacionDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setRequiereAck(entidad.getRequiereAck());
        dto.setIcono(entidad.getIcono());
        dto.setActivo(entidad.getActivo());
        return dto;
    }

    // De DTO -> Entidad
    public static TipoNotificacion toEntity(TipoNotificacionDTO dto) {
        if(dto == null) return null;

        TipoNotificacion entidad = new TipoNotificacion();
        entidad.setId(dto.getId()); // opcional, solo si vas a actualizar
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setRequiereAck(dto.getRequiereAck());
        entidad.setIcono(dto.getIcono());
        entidad.setActivo(dto.getActivo());
        return entidad;
    }
}
