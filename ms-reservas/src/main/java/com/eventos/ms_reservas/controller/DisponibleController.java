package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.exception.DisponibleNotFoundException;
import com.eventos.ms_reservas.exception.DisponibleOcupadoException;
import com.eventos.ms_reservas.exception.FechaInvalidaException;
import com.eventos.ms_reservas.mapper.DisponibleMapper;
import com.eventos.ms_reservas.model.Disponible;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Disponibilidad", description = "Operaciones relacionadas con la disponibilidad de eventos")
@RestController
@RequestMapping("/v1/disponible")
public class DisponibleController {

    @Operation(
        summary = "Obtener disponibilidad por ID",
        
        description = "Devuelve la información de disponibilidad para un evento en base a su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
        @ApiResponse(responseCode = "400", description = "ID o fechas inválidas"),
        @ApiResponse(responseCode = "404", description = "No se encontró disponibilidad"),
        @ApiResponse(responseCode = "409", description = "Disponibilidad ocupada")
    })
    @GetMapping("/{id}")
    public DisponibleDTO obtenerDisponible(
        @Parameter(
            description = "ID de la disponibilidad a consultar",
            example = "1",
            required = true
        )
        @PathVariable String id
    ) {

        // Caso: No existe disponibilidad
        if (id.equals("999")) {
            throw new DisponibleNotFoundException("No se encontró disponibilidad para el id " + id);
        }

        // Caso: Fechas inválidas
        if (id.equals("fechaInvalida")) {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fin = inicio.minusHours(1);
            if (fin.isBefore(inicio)) {
                throw new FechaInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
            }
        }

        // Caso: Ocupado
        if (id.equals("ocupado")) {
            throw new DisponibleOcupadoException("El evento ya está reservado");
        }

        // Caso normal: devolver objeto válido
        Disponible disponible = new Disponible();
        disponible.setId(id);
        disponible.setDescripcion("Eventos disponibles entre "
                + LocalDateTime.now() + " a " + LocalDateTime.now().plusDays(1) + ":");
        disponible.setFechaInicio(LocalDateTime.now());
        disponible.setFechaFin(LocalDateTime.now().plusHours(1));
        disponible.setDisponible(true);

        return DisponibleMapper.toDTO(disponible);
    }
}