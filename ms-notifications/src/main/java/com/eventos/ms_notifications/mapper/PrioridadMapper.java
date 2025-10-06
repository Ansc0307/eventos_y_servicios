package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.stereotype.Component;

@Component
public class PrioridadMapper {

    /**
     * Convierte una entidad Prioridad a un DTO PrioridadDTO
     */
    public PrioridadDTO toDTO(Prioridad entidad) {
        if (entidad == null) {
            return null;
        }

        PrioridadDTO dto = new PrioridadDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setNivel(entidad.getNivel());
        dto.setColorHex(entidad.getColorHex());
        dto.setActivo(entidad.getActivo());
        return dto;
    }

    /**
     * Convierte un DTO PrioridadDTO a una entidad Prioridad
     * Para creación (sin ID)
     */
    public Prioridad toEntity(PrioridadDTO dto) {
        if (dto == null) {
            return null;
        }

        Prioridad entidad = new Prioridad();
        // No seteamos el ID para creación
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setNivel(dto.getNivel());
        entidad.setColorHex(dto.getColorHex());
        entidad.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return entidad;
    }

    /**
     * Actualiza una entidad existente con datos del DTO
     * Para actualizaciones (con ID)
     */
    public void updateEntityFromDTO(PrioridadDTO dto, Prioridad entidad) {
        if (dto == null || entidad == null) {
            return;
        }

        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setNivel(dto.getNivel());
        entidad.setColorHex(dto.getColorHex());
        
        if (dto.getActivo() != null) {
            entidad.setActivo(dto.getActivo());
        }
    }
}