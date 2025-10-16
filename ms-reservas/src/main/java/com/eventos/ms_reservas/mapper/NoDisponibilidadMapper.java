package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.model.NoDisponibilidad;

public class NoDisponibilidadMapper {

    public static NoDisponibilidadDTO toDTO(NoDisponibilidad entidad) {
        if (entidad == null) return null;

        NoDisponibilidadDTO dto = new NoDisponibilidadDTO();
        dto.setIdNoDisponibilidad(entidad.getIdNoDisponibilidad());
        dto.setIdOferta(entidad.getIdOferta());
        dto.setMotivo(entidad.getMotivo());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFin(entidad.getFechaFin());
        dto.setIdReserva(entidad.getIdReserva());

        return dto;
    }

    public static NoDisponibilidad toEntity(NoDisponibilidadDTO dto) {
        if (dto == null) return null;

        NoDisponibilidad entidad = new NoDisponibilidad();
        entidad.setIdNoDisponibilidad(dto.getIdNoDisponibilidad());
        entidad.setIdOferta(dto.getIdOferta());
        entidad.setMotivo(dto.getMotivo());
        entidad.setFechaInicio(dto.getFechaInicio());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setIdReserva(dto.getIdReserva());

        return entidad;
    }
}