package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.EventoDTO;
import com.eventos.reservas.gestion.model.Evento;
import com.eventos.reservas.mapper.EventoMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/evento")
public class EventoController {
    private static final Logger LOGG = LoggerFactory.getLogger(ReservaController.class);
    @GetMapping("/{id}")
    public EventoDTO obtenerEvento(@PathVariable String id) {
        String descripcion= "Evento de cumpleaños";
        try {
            int idInt = Integer.parseInt(id);
            if (idInt < 1) {
                LOGG.error("ID inválido: {}", id);
                return null; 
            }
            if(idInt % 2 == 0){
                descripcion = "Evento de boda";
            }
        } catch (NumberFormatException e) {
            LOGG.error("ID no es un número válido: {}", id);
            return null; 
        }
        Evento evento = new Evento(id, descripcion);
        return EventoMapper.toDTO(evento);
    }
}
