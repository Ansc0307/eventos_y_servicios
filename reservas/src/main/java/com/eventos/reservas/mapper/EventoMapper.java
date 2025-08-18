package com.eventos.reservas.mapper;

import com.eventos.reservas.gestion.model.Evento;
import com.eventos.reservas.dto.EventoDTO;

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
