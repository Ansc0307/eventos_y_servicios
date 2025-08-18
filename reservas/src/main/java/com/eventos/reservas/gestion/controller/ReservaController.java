package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.ReservaDTO;
import com.eventos.reservas.gestion.model.Reserva;
import com.eventos.reservas.mapper.ReservaMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {

    @GetMapping("/{id}")
    public ReservaDTO obtenerReserva(@PathVariable String id) {
        Reserva reserva = new Reserva(id, "PENDIENTE");
        return ReservaMapper.toDTO(reserva);
    }

}
