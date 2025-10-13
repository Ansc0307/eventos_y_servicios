package com.eventos.ms_notifications.mapper;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.stereotype.Component;

@Component
public class TipoNotificacionMapper {

    public TipoNotificacionDTO toDto(TipoNotificacion entity) {
        if (entity == null) return null;
        return new TipoNotificacionDTO(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion()
        );
    }

    public TipoNotificacion toEntity(TipoNotificacionDTO dto) {
        if (dto == null) return null;
        return new TipoNotificacion(
            dto.getId(),
            dto.getNombre(),
            dto.getDescripcion()
        );
    }
}
