package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.EventoDTO;
import com.eventos.reservas.gestion.model.Evento;
import com.eventos.reservas.mapper.EventoMapper;
import com.eventos.reservas.exception.EventoNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/evento")
public class EventoController {

    @GetMapping("/{id}")
    public EventoDTO obtenerEvento(@PathVariable String id) {
        int idInt;

        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new EventoNotFoundException("ID de evento no es un número válido: " + id);
        }

        if (idInt < 1) {
            throw new EventoNotFoundException("ID de evento inválido: " + id);
        }

        String descripcion = (idInt % 2 == 0) ? "Evento de boda" : "Evento de cumpleaños";
        Evento evento = new Evento(id, descripcion);
        return EventoMapper.toDTO(evento);
    }
}
