package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.service.TipoNotificacionService;
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
@RequestMapping("/v1/tipos-notificacion")
@Tag(name = "Gestión de Tipos de Notificación", description = "CRUD para los tipos de notificación del sistema")
public class TipoNotificacionController {

    private final TipoNotificacionService tipoNotificacionService;

    public TipoNotificacionController(TipoNotificacionService tipoNotificacionService) {
        this.tipoNotificacionService = tipoNotificacionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Listar todos los tipos de notificación", description = "Obtiene la lista completa de tipos de notificación registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<TipoNotificacionDTO> listarTodos() {
        return tipoNotificacionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Obtener un tipo de notificación por ID", description = "Devuelve un tipo de notificación según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de notificación encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el tipo de notificación con el ID especificado")
    })
    public Mono<ResponseEntity<TipoNotificacionDTO>> obtenerPorId(@PathVariable Long id) {
        return tipoNotificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Crear un nuevo tipo de notificación", description = "Registra un nuevo tipo de notificación en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de notificación creado correctamente"),
            @ApiResponse(responseCode = "409", description = "Ya existe un tipo con el mismo nombre"),
            @ApiResponse(responseCode = "422", description = "Entrada inválida o datos incompletos")
    })
    public Mono<ResponseEntity<TipoNotificacionDTO>> crear(@Valid @RequestBody TipoNotificacionDTO tipoDTO) {
        return tipoNotificacionService.crear(tipoDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Actualizar un tipo de notificación existente", description = "Modifica los datos de un tipo de notificación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de notificación actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado"),
            @ApiResponse(responseCode = "422", description = "Datos inválidos enviados")
    })
    public Mono<ResponseEntity<TipoNotificacionDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody TipoNotificacionDTO tipoDTO) {
        return tipoNotificacionService.actualizar(id, tipoDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar un tipo de notificación por ID", description = "Elimina un tipo de notificación existente por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de notificación eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return tipoNotificacionService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
