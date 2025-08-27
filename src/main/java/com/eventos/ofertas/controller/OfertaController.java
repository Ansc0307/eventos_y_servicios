package com.eventos.ofertas.controller;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.exception.OfertaNotFoundException;
import com.eventos.ofertas.mapper.OfertaMapper;
import com.eventos.ofertas.model.Oferta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.logging.Logger;

@Tag(name = "Ofertas", description = "Operaciones relacionadas con ofertas")
@RestController
@RequestMapping("/v1/oferta")
public class OfertaController {

    private static final Logger LOGGER = Logger.getLogger(OfertaController.class.getName());

    @Operation(
        summary = "Obtener una oferta por ID",
        description = "Devuelve la información de una oferta según su ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Oferta encontrada"),
            @ApiResponse(responseCode = "404", description = "Oferta no encontrada o ID inválido")
        }
    )
    @GetMapping("/{id}")
    public OfertaDTO obtenerOferta(
        @Parameter(description = "ID de la oferta", example = "1")
        @PathVariable String id
    ) {
        LOGGER.info("Obteniendo oferta con ID: " + id);
        int idInt;

        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new OfertaNotFoundException("El ID de la oferta no es un número válido: " + id);
        }

        if (idInt < 1) {
            throw new OfertaNotFoundException("El ID de la oferta debe ser mayor a cero: " + id);
        }

        // Simulación de una búsqueda: ID par = espacio, impar = servicio
        String descripcion = (idInt % 2 == 0) ? "Oferta de espacio para eventos" : "Servicio de catering disponible";
        String categoria = (idInt % 2 == 0) ? "Espacio" : "Servicio";
        double precio = (idInt % 2 == 0) ? 3500.0 : 1500.0;

        Oferta oferta = new Oferta(id, "Oferta #" + id, descripcion, precio, categoria);
        return OfertaMapper.toDTO(oferta);
    }
}