package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.model.Reserva;

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
