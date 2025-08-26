package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.EventoDTO;
import com.eventos.ms_reservas.exception.EventoNotFoundException;
import com.eventos.ms_reservas.mapper.EventoMapper;
import com.eventos.ms_reservas.model.Evento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Eventos", description = "Operaciones relacionadas con eventos")
@RestController
@RequestMapping("/v1/evento")
public class EventoController {

    @Operation(
        summary = "Obtener un evento por ID",
        description = "Devuelve la información de un evento según su ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado o ID inválido")
        }
    )
    @GetMapping("/{id}")
    public EventoDTO obtenerEvento(
        @Parameter(description = "ID del evento", example = "1")
        @PathVariable String id
    ) {
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