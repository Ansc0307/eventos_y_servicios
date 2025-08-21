package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.ReservaDTO;
import com.eventos.reservas.gestion.model.Reserva;
import com.eventos.reservas.mapper.ReservaMapper;
import com.eventos.reservas.exception.ReservaNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {

    @GetMapping("/{id}")
    public ReservaDTO obtenerReserva(@PathVariable String id) {
        int idInt;

        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new ReservaNotFoundException("ID no es un número válido: " + id);
        }

        if (idInt < 1) {
            throw new ReservaNotFoundException("ID inválido: " + id);
        }

        String estado = (idInt % 2 == 0) ? "APROBADA" : "PENDIENTE";
        Reserva reserva = new Reserva(id, estado);
        return ReservaMapper.toDTO(reserva);
    }
}
