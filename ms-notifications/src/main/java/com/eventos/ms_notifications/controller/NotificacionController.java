package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.service.NotificacionService;
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
@RequestMapping("/v1/notificaciones")
@Tag(name = "Gestión de Notificaciones", description = "CRUD para administrar las notificaciones del sistema")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene una lista completa de todas las notificaciones registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<NotificacionDTO> listarTodas() {
        return notificacionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Obtener una notificación por ID", description = "Devuelve los datos de una notificación específica según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la notificación con el ID especificado")
    })
    public Mono<ResponseEntity<NotificacionDTO>> obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{userId}")
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Listar notificaciones por usuario", description = "Obtiene todas las notificaciones asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones para el usuario indicado")
    })
    public Flux<NotificacionDTO> obtenerPorUsuario(@PathVariable Long userId) {
        return notificacionService.obtenerPorUsuario(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Crear una nueva notificación", description = "Registra una nueva notificación en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada correctamente"),
            @ApiResponse(responseCode = "422", description = "Entrada inválida o datos incompletos")
    })
    public Mono<ResponseEntity<NotificacionDTO>> crear(@Valid @RequestBody NotificacionDTO notificacionDTO) {
        return notificacionService.crear(notificacionDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Actualizar una notificación", description = "Modifica los datos de una notificación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "422", description = "Datos inválidos enviados")
    })
    public Mono<ResponseEntity<NotificacionDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionDTO notificacionDTO) {
        return notificacionService.actualizar(id, notificacionDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/leida")
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Marcar una notificación como leída", description = "Actualiza el estado de lectura de una notificación a 'leída'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación marcada como leída correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public Mono<ResponseEntity<NotificacionDTO>> marcarComoLeida(@PathVariable Long id) {
        return notificacionService.marcarComoLeida(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN', 'PROVEEDOR', 'ORGANIZADOR')")
    @Operation(summary = "Eliminar una notificación", description = "Elimina una notificación existente por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return notificacionService.eliminar(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
