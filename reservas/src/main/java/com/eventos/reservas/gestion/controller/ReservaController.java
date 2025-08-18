package com.eventos.reservas.gestion.controller;

import com.eventos.reservas.gestion.model.Reserva;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {

    @GetMapping("/{id}")
    public Reserva getReserva(@PathVariable String id) {
        // Retorno estático para prueba
        return new Reserva(id, "Pendiente");
    }
}
