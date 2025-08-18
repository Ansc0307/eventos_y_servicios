package com.eventos.reservas.mapper;

import com.eventos.reservas.disponibilidad.model.Solicitud;
import com.eventos.reservas.dto.SolicitudDTO;

public class SolicitudMapper {

    public static SolicitudDTO toDTO(Solicitud solicitud) {
        return new SolicitudDTO(
                solicitud.getId(),
                solicitud.getNombreRecurso(),
                solicitud.getFechaInicio(),
                solicitud.getFechaFin(),
                solicitud.getEstado()
        );
    }

    public static Solicitud toEntity(SolicitudDTO dto) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(dto.getId());
        solicitud.setNombreRecurso(dto.getNombreRecurso());
        solicitud.setFechaInicio(dto.getFechaInicio());
        solicitud.setFechaFin(dto.getFechaFin());
        solicitud.setEstado(dto.getEstado());
        return solicitud;
    }
}
