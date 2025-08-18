package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.EventoDTO;
import com.eventos.reservas.gestion.model.Evento;
import com.eventos.reservas.mapper.EventoMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/evento")
public class EventoController {

    @GetMapping("/{id}")
    public EventoDTO obtenerEvento(@PathVariable String id) {
        Evento evento = new Evento(id, "Evento de prueba");
        return EventoMapper.toDTO(evento);
    }
}
