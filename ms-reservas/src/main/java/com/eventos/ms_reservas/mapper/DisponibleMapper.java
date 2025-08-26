package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.disponibilidad.model.Disponible;
import com.eventos.ms_reservas.dto.DisponibleDTO;

public class DisponibleMapper {

    public static DisponibleDTO toDTO(Disponible disponible) {
        return new DisponibleDTO(
                disponible.getId(),
                disponible.getDescripcion(),
                disponible.getFechaInicio(),
                disponible.getFechaFin(),
                disponible.isDisponible()
        );
    }

    public static Disponible toEntity(DisponibleDTO dto) {
        Disponible disponible = new Disponible();
        disponible.setId(dto.getId());
        disponible.setDescripcion(dto.getDescripcion());
        disponible.setFechaInicio(dto.getFechaInicio());
        disponible.setFechaFin(dto.getFechaFin());
        disponible.setDisponible(dto.isDisponible());
        return disponible;
    }
}
