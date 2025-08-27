package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Disponibilidad", description = "REST API para consultar y registrar disponibilidad de eventos")
@RestController
@RequestMapping("/v1/disponible")
public class DisponibleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibleController.class);

    // -------- GET --------
    @Operation(
        summary = "${api.disponible.get-disponible.description}",
        description = "${api.disponible.get-disponible.notes}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
        @ApiResponse(responseCode = "409", description = "${api.responseCodes.conflict.description}")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public DisponibleDTO obtenerDisponible(
        @Parameter(description = "${api.disponible.get-disponible.parameters.id}", required = true)
        @PathVariable String id
    ) {
        LOGGER.info("Consultando disponibilidad con id: {}", id);

        // Caso: No existe disponibilidad
        if (id.equals("999")) {
            throw new DisponibleNotFoundException("No se encontró disponibilidad para el id " + id);
        }

        // Caso: Fechas inválidas
        if (id.equals("fechaInvalida")) {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fin = inicio.minusHours(1);
            LOGGER.debug("Validando fechas -> inicio: {}, fin: {}", inicio, fin);

            if (fin.isBefore(inicio)) {
                throw new FechaInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
            }
        }

        // Caso: Ocupado
        if (id.equals("ocupado")) {
            throw new DisponibleOcupadoException("El evento ya está reservado");
        }

        // Caso normal
        Disponible disponible = new Disponible();
        disponible.setId(id);
        disponible.setDescripcion("Eventos disponibles entre "
                + LocalDateTime.now() + " a " + LocalDateTime.now().plusDays(1) + ":");
        disponible.setFechaInicio(LocalDateTime.now());
        disponible.setFechaFin(LocalDateTime.now().plusHours(1));
        disponible.setDisponible(true);

        LOGGER.debug("Disponibilidad encontrada para id: {}", id);
        return DisponibleMapper.toDTO(disponible);
    }

    // -------- POST --------
    @Operation(
        summary = "${api.disponible.create-disponible.description}",
        description = "${api.disponible.create-disponible.notes}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public DisponibleDTO crearDisponible(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la disponibilidad a registrar",
            required = true,
            content = @Content(mediaType = "application/json",
                               schema = @Schema(implementation = DisponibleDTO.class))
        )
        @Valid @RequestBody DisponibleDTO body
    ) {
        LOGGER.info("Creando nueva disponibilidad: {}", body);

        // Validación de fechas
        if (body.getFechaFin() != null && body.getFechaInicio() != null 
                && body.getFechaFin().isBefore(body.getFechaInicio())) {
            throw new FechaInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
        }

        // Aquí iría la lógica de persistencia (ej: guardar en DB)
        LOGGER.debug("Disponibilidad creada para id: {}", body.getId());

        return body;
    }
}
