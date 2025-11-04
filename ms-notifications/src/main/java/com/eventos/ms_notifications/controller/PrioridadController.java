package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.service.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/prioridades")
@Tag(name = "Gestión de Prioridades", description = "CRUD para administrar los niveles de prioridad de las notificaciones")
public class PrioridadController {

    private final PrioridadService prioridadService;

    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Listar todas las prioridades", description = "Obtiene una lista completa de las prioridades registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<PrioridadDTO> listar() {
        return prioridadService.obtenerTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Obtener prioridad por ID", description = "Devuelve una prioridad según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prioridad encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la prioridad con el ID especificado")
    })
    public Mono<ResponseEntity<PrioridadDTO>> obtenerPorId(@PathVariable Long id) {
        return prioridadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear una nueva prioridad", description = "Registra una nueva prioridad en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prioridad creada correctamente"),
            @ApiResponse(responseCode = "409", description = "Ya existe una prioridad con ese nombre"),
            @ApiResponse(responseCode = "422", description = "Entrada inválida (nombre vacío o inválido)")
    })
    public Mono<ResponseEntity<PrioridadDTO>> crear(@Valid @RequestBody PrioridadDTO prioridadDTO) {
        return prioridadService.crear(prioridadDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar una prioridad", description = "Modifica los datos de una prioridad existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prioridad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Prioridad no encontrada"),
            @ApiResponse(responseCode = "422", description = "Datos inválidos enviados")
    })
    public Mono<ResponseEntity<PrioridadDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody PrioridadDTO prioridadDTO) {
        return prioridadService.actualizar(id, prioridadDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar una prioridad", description = "Elimina una prioridad existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prioridad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return prioridadService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
