package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.gestion.model.Evento;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/evento")
public class EventoController {

    @GetMapping("/{id}")
    public Evento getEvento(@PathVariable String id) {
        // Retorno estático para prueba
        return new Evento(id, "Evento de prueba");
    }
}
