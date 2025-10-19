package com.eventos.ofertas.controller;

import com.eventos.ofertas.dto.OfertaRequestDTO;
import com.eventos.ofertas.dto.OfertaResponseDTO;
import com.eventos.ofertas.dto.PrecioDescuentoDTO;
import com.eventos.ofertas.dto.PrecioDescuentoRequestDTO;
import com.eventos.ofertas.service.OfertaService;
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
@RequestMapping("/ofertas")
@RequiredArgsConstructor
@Tag(name = "Ofertas", description = "API para gestionar ofertas y sus descuentos")
public class OfertaController {
    
    private final OfertaService ofertaService;
    
    /**
     * POST /ofertas - Crear una nueva oferta
     */
    @Operation(
        summary = "Crear una nueva oferta",
        description = "Crea una nueva oferta con sus medias asociadas"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Oferta creada exitosamente",
            content = @Content(schema = @Schema(implementation = OfertaResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o categoría no encontrada"
        )
    })
    @PostMapping
    public ResponseEntity<OfertaResponseDTO> crearOferta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la oferta a crear",
                required = true
            )
            @Valid @RequestBody OfertaRequestDTO request) {
        OfertaResponseDTO oferta = ofertaService.crearOferta(request);
        return new ResponseEntity<>(oferta, HttpStatus.CREATED);
    }
    
    /**
     * GET /ofertas - Obtener todas las ofertas activas
     */
    @Operation(
        summary = "Obtener todas las ofertas activas",
        description = "Retorna una lista con todas las ofertas que están activas (activo=true)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de ofertas obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<OfertaResponseDTO>> obtenerTodasLasOfertas() {
        List<OfertaResponseDTO> ofertas = ofertaService.obtenerTodasLasOfertasActivas();
        return ResponseEntity.ok(ofertas);
    }
    
    /**
     * GET /ofertas/categoria/{idCategoria} - Obtener ofertas filtradas por categoría
     */
    @Operation(
        summary = "Obtener ofertas por categoría",
        description = "Retorna todas las ofertas activas de una categoría específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ofertas encontradas"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<OfertaResponseDTO>> obtenerOfertasPorCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long idCategoria) {
        List<OfertaResponseDTO> ofertas = ofertaService.obtenerOfertasPorCategoria(idCategoria);
        return ResponseEntity.ok(ofertas);
    }
    
    /**
     * PUT /ofertas/{id} - Editar una oferta (total o parcialmente)
     */
    @Operation(
        summary = "Actualizar una oferta",
        description = "Actualiza una oferta existente (parcial o totalmente)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Oferta actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = OfertaResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Oferta no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> actualizarOferta(
            @Parameter(description = "ID de la oferta", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos actualizados de la oferta",
                required = true
            )
            @Valid @RequestBody OfertaRequestDTO request) {
        OfertaResponseDTO oferta = ofertaService.actualizarOferta(id, request);
        return ResponseEntity.ok(oferta);
    }
    
    /**
     * DELETE /ofertas/{id} - Borrado lógico (activo=false, estado='archivado')
     */
    @Operation(
        summary = "Eliminar una oferta (borrado lógico)",
        description = "Realiza un borrado lógico estableciendo activo=false y estado='archivado'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Oferta eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Oferta no encontrada"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOferta(
            @Parameter(description = "ID de la oferta", required = true)
            @PathVariable Long id) {
        ofertaService.eliminarOferta(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * POST /ofertas/{id}/descuentos - Añadir descuento a una oferta
     */
    @Operation(
        summary = "Añadir descuento a una oferta",
        description = "Crea un nuevo descuento asociado a una oferta"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Descuento creado exitosamente",
            content = @Content(schema = @Schema(implementation = PrecioDescuentoDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Oferta no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o fechas incorrectas"
        )
    })
    @PostMapping("/{id}/descuentos")
    public ResponseEntity<PrecioDescuentoDTO> agregarDescuento(
            @Parameter(description = "ID de la oferta", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del descuento a crear",
                required = true
            )
            @Valid @RequestBody PrecioDescuentoRequestDTO request) {
        PrecioDescuentoDTO descuento = ofertaService.agregarDescuento(id, request);
        return new ResponseEntity<>(descuento, HttpStatus.CREATED);
    }
    
    /**
     * DELETE /ofertas/{id}/descuentos/{idDescuento} - Eliminar descuento de una oferta
     */
    @Operation(
        summary = "Eliminar descuento de una oferta",
        description = "Elimina un descuento específico de una oferta"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Descuento eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Oferta o descuento no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "El descuento no pertenece a la oferta especificada"
        )
    })
    @DeleteMapping("/{id}/descuentos/{idDescuento}")
    public ResponseEntity<Void> eliminarDescuento(
            @Parameter(description = "ID de la oferta", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID del descuento", required = true)
            @PathVariable Long idDescuento) {
        ofertaService.eliminarDescuento(id, idDescuento);
        return ResponseEntity.noContent().build();
    }
}