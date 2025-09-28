package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.model.Prioridad;

public class PrioridadMapper {

    // De Entidad -> DTO
    public static PrioridadDTO toDTO(Prioridad entidad) {
        if(entidad == null) return null;

        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setNivel(entidad.getNivel());
        dto.setColorHex(entidad.getColorHex());
        dto.setActivo(entidad.getActivo());
        return dto;
    }

    // De DTO -> Entidad
    public static Prioridad toEntity(PrioridadDTO dto) {
        if(dto == null) return null;

        Prioridad entidad = new Prioridad();
        entidad.setId(dto.getId()); // opcional, solo si vas a actualizar
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setNivel(dto.getNivel());
        entidad.setColorHex(dto.getColorHex());
        entidad.setActivo(dto.getActivo());
        return entidad;
    }
}
