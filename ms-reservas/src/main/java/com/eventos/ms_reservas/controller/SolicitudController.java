package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.SolicitudNotFoundException;
import com.eventos.ms_reservas.exception.SolicitudPendienteException;
import com.eventos.ms_reservas.mapper.SolicitudMapper;
import com.eventos.ms_reservas.model.Solicitud;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/solicitud")
@Tag(name = "Solicitudes", description = "REST API para solicitudes de reservas")
public class SolicitudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudController.class);

    // 📌 GET: Obtener solicitud por ID
    @Operation(summary = "Obtener una solicitud por ID", description = "Devuelve la información de una solicitud específica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
            @ApiResponse(responseCode = "409", description = "Solicitud aún pendiente")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public SolicitudDTO obtenerSolicitud(
            @Parameter(description = "ID de la solicitud", example = "1", required = true)
            @PathVariable String id) {

        LOGGER.info("Buscando solicitud con id: {}", id);

        // Caso: no existe
        if ("999".equals(id)) {
            throw new SolicitudNotFoundException("No se encontró la solicitud con id " + id);
        }

        // Caso: aún pendiente
        if ("5".equals(id)) {
            throw new SolicitudPendienteException("La solicitud " + id + " aún está pendiente de respuesta");
        }

        // Caso: aceptada (mock)
        Solicitud solicitud = new Solicitud();
        solicitud.setId(id);
        solicitud.setNombreRecurso("Reserva de salón de eventos");
        solicitud.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitud.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(3));
        solicitud.setEstado("aceptada");

        LOGGER.debug("Solicitud encontrada: {}", solicitud);

        return SolicitudMapper.toDTO(solicitud);
    }

    // 📌 POST: Crear solicitud
    @Operation(summary = "Crear nueva solicitud", description = "Registra una nueva solicitud de reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public SolicitudDTO crearSolicitud(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la solicitud a crear",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudDTO.class)))
            @Valid @RequestBody SolicitudDTO body) {

        LOGGER.info("Creando nueva solicitud: {}", body.getNombreRecurso());

        // Simulamos guardado en BD
        body.setId("12345");
        body.setEstado("pendiente");

        LOGGER.debug("Solicitud creada con id: {}", body.getId());

        return body;
    }

    // 📌 DELETE: Eliminar solicitud
    @Operation(summary = "Eliminar solicitud por ID", description = "Elimina una solicitud existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud eliminada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @DeleteMapping("/{id}")
    public void eliminarSolicitud(
            @Parameter(description = "ID de la solicitud a eliminar", example = "1", required = true)
            @PathVariable String id) {

        LOGGER.warn("Eliminando solicitud con id: {}", id);

        if ("999".equals(id)) {
            throw new SolicitudNotFoundException("No se puede eliminar. No existe solicitud con id " + id);
        }

        // 🚨 Aquí deberías llamar al servicio que realmente elimine en BD
        LOGGER.debug("Solicitud {} eliminada correctamente", id);
    }
}
