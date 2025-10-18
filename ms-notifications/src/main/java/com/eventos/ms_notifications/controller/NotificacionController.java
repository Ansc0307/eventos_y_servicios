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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notificaciones")
@Tag(name = "Gestión de Notificaciones", description = "CRUD para administrar las notificaciones del sistema")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las notificaciones", description = "Obtiene una lista completa de todas las notificaciones registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> listarTodas() {
        return ResponseEntity.ok(notificacionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una notificación por ID", description = "Devuelve los datos de una notificación específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la notificación con el ID especificado")
    })
    public ResponseEntity<NotificacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{userId}")
    @Operation(summary = "Listar notificaciones por usuario", description = "Obtiene todas las notificaciones asociadas a un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones para el usuario indicado")
    })
    public ResponseEntity<List<NotificacionDTO>> obtenerPorUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(userId));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva notificación", description = "Registra una nueva notificación en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada correctamente"),
        @ApiResponse(responseCode = "422", description = "Entrada inválida o datos incompletos")
    })
    public ResponseEntity<NotificacionDTO> crear(@Valid @RequestBody NotificacionDTO notificacionDTO) {
        NotificacionDTO creada = notificacionService.crear(notificacionDTO);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una notificación", description = "Modifica los datos de una notificación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
        @ApiResponse(responseCode = "422", description = "Datos inválidos enviados")
    })
    public ResponseEntity<NotificacionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionDTO notificacionDTO) {
        return ResponseEntity.ok(notificacionService.actualizar(id, notificacionDTO));
    }

    @PatchMapping("/{id}/leida")
    @Operation(summary = "Marcar una notificación como leída", description = "Actualiza el estado de lectura de una notificación a 'leída'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<NotificacionDTO> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una notificación", description = "Elimina una notificación existente por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.ok("Notificación con ID " + id + " eliminada correctamente");
    }
}
