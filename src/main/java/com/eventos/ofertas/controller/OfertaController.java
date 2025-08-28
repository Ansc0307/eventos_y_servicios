package com.eventos.ofertas.controller;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.exception.OfertaNotFoundException;
import com.eventos.ofertas.mapper.OfertaMapper;
import com.eventos.ofertas.entity.Oferta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Tag(name = "Ofertas", description = "Operaciones relacionadas con ofertas")
@RestController
@RequestMapping("/v1/oferta")
public class OfertaController {

    private static final Logger LOGGER = Logger.getLogger(OfertaController.class.getName());

    // 🔹 Simulación de almacenamiento en memoria
    private static final Map<String, Oferta> ofertasDB = new HashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    @Operation(
        summary = "Obtener una oferta por ID",
        description = "Devuelve la información de una oferta según su ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Oferta encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                value = "{\n" +
                        "  \"error\": \"Datos inválidos\",\n" +
                        "  \"detalle\": \"Id inválido\"\n" +
                        "}")
                )
            )
        }
    )
    @GetMapping("/{id}")
    public OfertaDTO obtenerOferta(
        @Parameter(description = "ID de la oferta", example = "1")
        @PathVariable String id
    ) {
        LOGGER.info("Obteniendo oferta con ID: " + id);

        Oferta oferta = ofertasDB.get(id);
        if (oferta == null) {
            throw new OfertaNotFoundException("No se encontró la oferta con ID: " + id);
        }

        return OfertaMapper.toDTO(oferta);
    }

    @Operation(
        summary = "Crear una nueva oferta",
        description = "Permite registrar una nueva oferta de espacio o servicio para eventos",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva oferta",
            required = true,
            content = @Content(schema = @Schema(implementation = OfertaDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Oferta creada exitosamente",
                         content = @Content(schema = @Schema(implementation = OfertaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                value = "{\n" +
                        "  \"error\": \"Datos inválidos\",\n" +
                        "  \"detalle\": \"El campo 'titulo' no puede estar vacío\"\n" +
                        "}")
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<OfertaDTO> crearOferta(@Valid @RequestBody OfertaDTO ofertaDTO) {
        String newId = String.valueOf(idGenerator.getAndIncrement());

        Oferta oferta = new Oferta(
            newId,
            ofertaDTO.getTitulo(),
            ofertaDTO.getDescripcion(),
            ofertaDTO.getPrecio(),
            ofertaDTO.getCategoria()
        );

        ofertasDB.put(newId, oferta);
        LOGGER.info("Oferta creada con ID: " + newId);

        return ResponseEntity.ok(OfertaMapper.toDTO(oferta));
    }
    @Operation(
    summary = "Listar todas las ofertas",
    description = "Devuelve un listado de todas las ofertas registradas",
    responses = {
        @ApiResponse(responseCode = "200", description = "Listado de ofertas",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = OfertaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Lista vacia",
                         content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                value = "{\n" +
                        "  \"error\": \"Lista vacía\",\n" +
                        "  \"detalle\": \"No se encontraron ofertas disponibles\"\n" +
                        "}")
                )
            )
    }
)
    @GetMapping
        public List<OfertaDTO> listarOfertas() {
            LOGGER.info("Listando todas las ofertas");
            return ofertasDB.values().stream()
                .map(OfertaMapper::toDTO)
                .toList();}
}