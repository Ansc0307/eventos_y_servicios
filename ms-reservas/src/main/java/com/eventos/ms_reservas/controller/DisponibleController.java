package com.eventos.ms_reservas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.service.DisponibleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/disponibles")
@Tag(name = "Disponibilidad", description = "REST API para consultar y registrar disponibilidad de eventos")
public class DisponibleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibleController.class);

    private final DisponibleService disponibleService;

    @Autowired
    public DisponibleController(DisponibleService disponibleService) {
        this.disponibleService = disponibleService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener disponibilidad por ID", description = "Devuelve la información de una disponibilidad por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada"),
            @ApiResponse(responseCode = "422", description = "Fechas inválidas o recurso ocupado")
    })
    public DisponibleDTO getDisponible(
            @Parameter(description = "ID de la disponibilidad", required = true, example = "1")
            @PathVariable Long id) {
        LOGGER.info("Consultando disponibilidad con id: {}", id);
        return disponibleService.obtenerPorId(id);
    }

    @GetMapping
    @Operation(summary = "Listar disponibilidades", description = "Lista todas las disponibilidades registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de disponibilidades")
    })
    public List<DisponibleDTO> listarDisponibles() {
        LOGGER.info("Listando todas las disponibilidades");
        return disponibleService.listar();
    }

    @PostMapping
    @Operation(summary = "Crear nueva disponibilidad", description = "Registra una nueva disponibilidad de evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad creada correctamente"),
            @ApiResponse(responseCode = "422", description = "Fechas inválidas")
    })
    public DisponibleDTO createDisponible(@Valid @RequestBody DisponibleDTO body) {
        LOGGER.debug("Creando disponibilidad: {}", body);
        return disponibleService.crearDisponible(body);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar disponibilidad", description = "Elimina una disponibilidad existente según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    public void deleteDisponible(@PathVariable Long id) {
        LOGGER.debug("Eliminando disponibilidad con id: {}", id);
        disponibleService.eliminarDisponible(id);
    }
}
