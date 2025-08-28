package com.eventos.ms_reservas.mapper;

import com.eventos.ms_reservas.dto.EventoDTO;
import com.eventos.ms_reservas.model.Evento;

public class EventoMapper {

    public static EventoDTO toDTO(Evento evento) {
        return new EventoDTO(evento.getId(), evento.getDescripcion());
    }

    public static Evento toEntity(EventoDTO dto) {
        Evento evento = new Evento();
        evento.setId(dto.getId());
        evento.setDescripcion(dto.getDescripcion());
        return evento;
    }
}
