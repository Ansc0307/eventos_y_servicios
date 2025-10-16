package com.eventos.ms_reservas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/solicitudes")
@Tag(name = "Solicitudes", description = "REST API para solicitudes de reservas")
public class SolicitudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudController.class);
    private final SolicitudService solicitudService;

    @Autowired
    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID", description = "Devuelve la información de una solicitud por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public SolicitudDTO getSolicitud(
            @Parameter(description = "ID de la solicitud", required = true, example = "1")
            @PathVariable Long id) {
        LOGGER.info("Obteniendo solicitud con id: {}", id);
        return solicitudService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva solicitud", description = "Registra una nueva solicitud de reserva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public SolicitudDTO createSolicitud(@Valid @RequestBody SolicitudDTO body) {
        LOGGER.debug("Creando solicitud: {}", body);
        return solicitudService.crearSolicitud(body);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public void deleteSolicitud(@PathVariable Long id) {
        LOGGER.debug("Eliminando solicitud con id: {}", id);
        solicitudService.eliminarSolicitud(id);
    }
}