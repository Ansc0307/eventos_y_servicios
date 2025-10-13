package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.stereotype.Component;

@Component
public class PrioridadMapper {

    public PrioridadDTO toDto(Prioridad entity) {
        if (entity == null) return null;
        return new PrioridadDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }

    public Prioridad toEntity(PrioridadDTO dto) {
        if (dto == null) return null;
        return new Prioridad(
            dto.getId(),
            dto.getNombre(),
            dto.getDescripcion()
        );
    }
}
