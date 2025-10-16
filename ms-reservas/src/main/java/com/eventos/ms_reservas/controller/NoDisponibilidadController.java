package com.eventos.ms_reservas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.exception.NoDisponibleNotFoundException;
import com.eventos.ms_reservas.service.NoDisponibilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/no-disponibilidades")
@Tag(name = "No Disponibilidad", description = "REST API para registrar periodos de no disponibilidad de ofertas o servicios")
public class NoDisponibilidadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoDisponibilidadController.class);
    private final NoDisponibilidadService service;

    public NoDisponibilidadController(NoDisponibilidadService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro de no disponibilidad por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    public NoDisponibilidadDTO getNoDisponibilidad(
            @Parameter(description = "ID del registro de no disponibilidad", example = "1")
            @PathVariable Long id) {
        LOGGER.info("Buscando no disponibilidad con id: {}", id);
        return service.obtenerPorId(id)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id,
                        "No se encontró la no disponibilidad con ID: " + id));
    }

    @GetMapping("/oferta/{idOferta}")
    @Operation(summary = "Listar registros de no disponibilidad por ID de oferta")
    public List<NoDisponibilidadDTO> getByIdOferta(
            @Parameter(description = "ID de la oferta", example = "303")
            @PathVariable Integer idOferta) {
        LOGGER.info("Listando no disponibilidades de la oferta: {}", idOferta);
        return service.obtenerPorIdOferta(idOferta);
    }

    @PostMapping
    @Operation(summary = "Registrar nueva no disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No disponibilidad registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "422", description = "Fecha de inicio posterior a fecha fin")
    })
    public NoDisponibilidadDTO createNoDisponibilidad(@Valid @RequestBody NoDisponibilidadDTO dto) {
        LOGGER.debug("Creando registro de no disponibilidad: {}", dto);
        return service.crearNoDisponible(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de no disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro eliminado"),
            @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    public void deleteNoDisponibilidad(@PathVariable Long id) {
        LOGGER.debug("Eliminando registro de no disponibilidad con id: {}", id);
        service.eliminarNoDisponible(id);
    }
}