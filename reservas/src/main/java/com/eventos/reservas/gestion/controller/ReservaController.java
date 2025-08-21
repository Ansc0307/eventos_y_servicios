package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.dto.ReservaDTO;
import com.eventos.reservas.gestion.model.Reserva;
import com.eventos.reservas.mapper.ReservaMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {
    private static final Logger LOGG = LoggerFactory.getLogger(ReservaController.class);
    @GetMapping("/{id}")
    public ReservaDTO obtenerReserva(@PathVariable String id) {
        String estado = "PENDIENTE";
        try {
            int idInt = Integer.parseInt(id);
            if (idInt < 1) {
                LOGG.error("ID inválido: {}", id);
                return null; 
            }
            if(idInt % 2 == 0){
                estado = "APROBADA";
            }
        } catch (NumberFormatException e) {
            LOGG.error("ID no es un número válido: {}", id);
            return null; 
        }
        Reserva reserva = new Reserva(id, estado);
        return ReservaMapper.toDTO(reserva);
    }
}
