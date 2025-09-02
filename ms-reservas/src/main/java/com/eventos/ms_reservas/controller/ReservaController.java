package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.exception.ReservaNotFoundException;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.model.Reserva;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reservas", description = "Operaciones relacionadas con reservas")
@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {

    @Operation(
        summary = "Obtener una reserva por ID",
        description = "Devuelve la información de una reserva según su ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada o ID inválido")
        }
    )
    @GetMapping("/{id}")
    public ReservaDTO obtenerReserva(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable String id
    ) {
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