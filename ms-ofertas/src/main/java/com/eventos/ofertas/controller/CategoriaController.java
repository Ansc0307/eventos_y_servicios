package com.eventos.ofertas.controller;

import com.eventos.ofertas.dto.CategoriaDTO;
import com.eventos.ofertas.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "API para gestionar categorías de ofertas")
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    /**
     * POST /categorias - Crear una nueva categoría
     */
    @Operation(
        summary = "Crear una nueva categoría",
        description = "Crea una nueva categoría en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Categoría creada exitosamente",
            content = @Content(schema = @Schema(implementation = CategoriaDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<CategoriaDTO> crearCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la categoría a crear",
                required = true
            )
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO categoria = categoriaService.crearCategoria(categoriaDTO);
        return new ResponseEntity<>(categoria, HttpStatus.CREATED);
    }
    
    /**
     * GET /categorias - Obtener todas las categorías
     */
    @Operation(
        summary = "Obtener todas las categorías",
        description = "Retorna una lista con todas las categorías disponibles"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de categorías obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerTodasLasCategorias() {
        List<CategoriaDTO> categorias = categoriaService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    /**
     * GET /categorias/{id} - Obtener una categoría por ID
     */
    @Operation(
        summary = "Obtener categoría por ID",
        description = "Retorna los detalles de una categoría específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada",
            content = @Content(schema = @Schema(implementation = CategoriaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }
    
    /**
     * PUT /categorias/{id} - Actualizar una categoría
     */
    @Operation(
        summary = "Actualizar una categoría",
        description = "Actualiza los datos de una categoría existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = CategoriaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos actualizados de la categoría",
                required = true
            )
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO categoria = categoriaService.actualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(categoria);
    }
    
    /**
     * DELETE /categorias/{id} - Eliminar una categoría
     */
    @Operation(
        summary = "Eliminar una categoría",
        description = "Elimina una categoría del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Categoría eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}