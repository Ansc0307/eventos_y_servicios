package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.service.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/prioridades")
@Tag(name = "Gestión de Prioridades", description = "CRUD para administrar los niveles de prioridad de las notificaciones")
public class PrioridadController {

    private final PrioridadService prioridadService;

    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las prioridades", description = "Obtiene una lista completa de las prioridades registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<PrioridadDTO>> listar() {
        return ResponseEntity.ok(prioridadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener prioridad por ID", description = "Devuelve una prioridad según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la prioridad con el ID especificado")
    })
    public ResponseEntity<PrioridadDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prioridadService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear una nueva prioridad", description = "Registra una nueva prioridad en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad creada correctamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe una prioridad con ese nombre"),
        @ApiResponse(responseCode = "422", description = "Entrada inválida (nombre vacío o inválido)")
    })
    public ResponseEntity<PrioridadDTO> crear(@Valid @RequestBody PrioridadDTO prioridadDTO) {
        return ResponseEntity.ok(prioridadService.crear(prioridadDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar una prioridad", description = "Modifica los datos de una prioridad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada"),
        @ApiResponse(responseCode = "422", description = "Datos inválidos enviados")
    })
    public ResponseEntity<PrioridadDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PrioridadDTO prioridadDTO) {
        return ResponseEntity.ok(prioridadService.actualizar(id, prioridadDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una prioridad", description = "Elimina una prioridad existente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Prioridad eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        prioridadService.eliminar(id);
        return ResponseEntity.ok("Prioridad con ID " + id + " eliminada correctamente");
    }
}
