package com.eventos.reservas.mapper;

import com.eventos.reservas.dto.ReservaDTO;
import com.eventos.reservas.gestion.model.Reserva;

public class ReservaMapper {

    public static ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(reserva.getId(), reserva.getEstado());
    }

    public static Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setEstado(dto.getEstado());
        return reserva;
    }
}
