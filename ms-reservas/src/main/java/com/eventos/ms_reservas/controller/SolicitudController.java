package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.SolicitudNotFoundException;
import com.eventos.ms_reservas.exception.SolicitudPendienteException;
import com.eventos.ms_reservas.mapper.SolicitudMapper;
import com.eventos.ms_reservas.model.Solicitud;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Solicitudes", description = "Operaciones relacionadas con solicitudes")
@RestController
@RequestMapping("/v1/solicitud")
public class SolicitudController {

    @Operation(
        summary = "Obtener una solicitud por ID",
        description = "Devuelve la información de una solicitud según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "409", description = "Solicitud aún pendiente")
    })
    @GetMapping("/{id}")
    public SolicitudDTO obtenerSolicitud(
        @Parameter(
            description = "ID de la solicitud",
            example = "1",
            required = true
        )
        @PathVariable String id
    ) {

        // Caso: no existe
        if ("999".equals(id)) {
            throw new SolicitudNotFoundException("No se encontró la solicitud con id " + id);
        }

        
        // Caso: aún pendiente
        if ("5".equals(id)) {
            throw new SolicitudPendienteException("La solicitud " + id + " aún está pendiente de respuesta");
        }

        // Caso 4: Aceptada
        // Caso: aceptada
        Solicitud solicitud = new Solicitud();
        solicitud.setId(id);
        solicitud.setNombreRecurso("Reserva de salón de eventos");
        solicitud.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitud.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(3));
        solicitud.setEstado("aceptada");

        return SolicitudMapper.toDTO(solicitud);
    }
}