package com.eventos.ofertas.controller;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.dto.UpdateOfertaDTO;
import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.service.OfertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/ofertas", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ofertas", description = "Endpoints para gestionar ofertas")
@Validated
public class OfertaController {

    private final OfertaService service;

    public OfertaController(OfertaService service) { this.service = service; }
    
    @Operation(summary = "Crear oferta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Oferta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Título duplicado para el mismo proveedor")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OfertaDTO> crear(@Valid @RequestBody OfertaDTO request) {
        return service.crear(request);
    }
    @Operation(summary = "Obtener oferta por ID")
    @ApiResponses({ @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", description = "No encontrada") })
    @GetMapping("/{id}")
    public Mono<OfertaDTO> obtener(@Parameter(description = "ID de la oferta") @PathVariable Long id) {
        return service.obtener(id);
    }


    @Operation(summary = "Listar ofertas (filtrar por categoría opcional)")
    @GetMapping
    public Flux<OfertaDTO> listar(@RequestParam(required = false) Categoria categoria) {
        return service.listar(categoria);
    }

    @Operation(summary = "Actualizar una oferta")
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OfertaDTO> actualizarParcial(@PathVariable Long id, @Valid @RequestBody UpdateOfertaDTO patch) {
        return service.actualizarParcial(id, patch);
    }
    @Operation(summary = "Eliminar oferta por ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }
}