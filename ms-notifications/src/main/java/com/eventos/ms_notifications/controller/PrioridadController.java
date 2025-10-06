package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.service.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/prioridades")
@Tag(name = "1. Prioridades", description = "API REST para la gestión de prioridades de notificaciones")
@CrossOrigin(origins = "*")
public class PrioridadController {

    private final PrioridadService prioridadService;

    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    // ============ CREAR ============
    @Operation(
        summary = "Crear nueva prioridad", 
        description = "Crea una nueva prioridad en el sistema. El nombre debe ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prioridad creada exitosamente",
                    content = @Content(schema = @Schema(implementation = PrioridadDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una prioridad con el mismo nombre")
    })
    @PostMapping
    public ResponseEntity<PrioridadDTO> crearPrioridad(
            @Parameter(description = "DTO con los datos de la prioridad a crear", required = true)
            @Valid @RequestBody PrioridadDTO dto) {
        PrioridadDTO nuevaPrioridad = prioridadService.crearPrioridad(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPrioridad);
    }

    // ============ LISTAR TODOS ============
    @Operation(
        summary = "Listar todas las prioridades", 
        description = "Obtiene una lista de todas las prioridades registradas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de prioridades obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PrioridadDTO>> listarPrioridades() {
        List<PrioridadDTO> prioridades = prioridadService.listarPrioridades();
        return ResponseEntity.ok(prioridades);
    }

    // ============ OBTENER POR ID ============
    @Operation(
        summary = "Obtener prioridad por ID", 
        description = "Obtiene una prioridad específica por su ID único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrioridadDTO> obtenerPorId(
            @Parameter(description = "ID de la prioridad a buscar", example = "1", required = true)
            @PathVariable Long id) {
        PrioridadDTO prioridad = prioridadService.obtenerPorId(id);
        return ResponseEntity.ok(prioridad);
    }

    // ============ ACTUALIZAR ============
    @Operation(
        summary = "Actualizar prioridad", 
        description = "Actualiza los datos de una prioridad existente. Verifica que el nombre sea único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada"),
        @ApiResponse(responseCode = "409", description = "Conflicto con nombre de prioridad existente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PrioridadDTO> actualizarPrioridad(
            @Parameter(description = "ID de la prioridad a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "DTO con los nuevos datos de la prioridad", required = true)
            @Valid @RequestBody PrioridadDTO dto) {
        PrioridadDTO prioridadActualizada = prioridadService.actualizarPrioridad(id, dto);
        return ResponseEntity.ok(prioridadActualizada);
    }

    // ============ ELIMINAR ============
    @Operation(
        summary = "Eliminar prioridad", 
        description = "Elimina una prioridad del sistema por su ID. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Prioridad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrioridad(
            @Parameter(description = "ID de la prioridad a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        prioridadService.eliminarPrioridad(id);
        return ResponseEntity.noContent().build();
    }

    // ============ ENDPOINTS ADICIONALES ============

    @Operation(
        summary = "Listar prioridades activas", 
        description = "Obtiene una lista de todas las prioridades que están activas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de prioridades activas obtenida exitosamente")
    })
    @GetMapping("/activas")
    public ResponseEntity<List<PrioridadDTO>> listarPrioridadesActivas() {
        List<PrioridadDTO> prioridadesActivas = prioridadService.listarPrioridadesActivas();
        return ResponseEntity.ok(prioridadesActivas);
    }

    @Operation(
        summary = "Verificar existencia por nombre", 
        description = "Verifica si existe una prioridad con el nombre especificado en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(
            @Parameter(description = "Nombre de la prioridad a verificar", example = "ALTA", required = true)
            @PathVariable String nombre) {
        boolean existe = prioridadService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    @Operation(
        summary = "Buscar por nivel", 
        description = "Obtiene las prioridades que coinciden con un nivel específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridades encontradas exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron prioridades con ese nivel")
    })
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<PrioridadDTO>> buscarPorNivel(
            @Parameter(description = "Nivel de prioridad a buscar (1=alta, 2=media, 3=baja)", example = "1", required = true)
            @PathVariable Integer nivel) {
        List<PrioridadDTO> prioridades = prioridadService.buscarPorNivel(nivel);
        return ResponseEntity.ok(prioridades);
    }

    @Operation(
        summary = "Desactivar prioridad", 
        description = "Desactiva una prioridad (soft delete) en lugar de eliminarla físicamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad desactivada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<PrioridadDTO> desactivarPrioridad(
            @Parameter(description = "ID de la prioridad a desactivar", example = "1", required = true)
            @PathVariable Long id) {
        PrioridadDTO prioridadDesactivada = prioridadService.desactivarPrioridad(id);
        return ResponseEntity.ok(prioridadDesactivada);
    }

    @Operation(
        summary = "Activar prioridad", 
        description = "Reactiva una prioridad que estaba desactivada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad activada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<PrioridadDTO> activarPrioridad(
            @Parameter(description = "ID de la prioridad a activar", example = "1", required = true)
            @PathVariable Long id) {
        PrioridadDTO prioridadActivada = prioridadService.activarPrioridad(id);
        return ResponseEntity.ok(prioridadActivada);
    }
}