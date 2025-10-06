package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.service.NotificacionService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/notificaciones")
@Tag(name = "3. Notificaciones", description = "API REST para la gestión de notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(
        summary = "Crear nueva notificación", 
        description = "Crea una nueva notificación en el sistema. Valida que el userId, prioridadId y tipoNotificacionId existan."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
                    content = @Content(schema = @Schema(implementation = NotificacionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Prioridad o tipo de notificación no encontrado")
    })
    @PostMapping
    public ResponseEntity<NotificacionDTO> crearNotificacion(
            @Parameter(description = "DTO con los datos de la notificación a crear", required = true)
            @Valid @RequestBody NotificacionDTO dto) {
        NotificacionDTO nuevaNotificacion = notificacionService.crearNotificacion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNotificacion);
    }

    @Operation(
        summary = "Listar todas las notificaciones", 
        description = "Obtiene una lista de todas las notificaciones en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listarNotificaciones() {
        List<NotificacionDTO> notificaciones = notificacionService.listarNotificaciones();
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Listar notificaciones paginadas", 
        description = "Obtiene una lista paginada de todas las notificaciones en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de notificaciones obtenida exitosamente")
    })
    @GetMapping("/paginated")
    public ResponseEntity<Page<NotificacionDTO>> listarNotificacionesPaginated(
            @Parameter(description = "Número de página (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "20") 
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenar", example = "fechaCreacion") 
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @Parameter(description = "Dirección del orden", example = "DESC") 
            @RequestParam(defaultValue = "DESC") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("DESC") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<NotificacionDTO> notificaciones = notificacionService.listarNotificaciones(pageable);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Obtener notificación por ID", 
        description = "Obtiene una notificación específica por su ID único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> obtenerPorId(
            @Parameter(description = "ID de la notificación a buscar", example = "1", required = true)
            @PathVariable Long id) {
        NotificacionDTO notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(notificacion);
    }

    @Operation(
        summary = "Actualizar notificación", 
        description = "Actualiza los datos de una notificación existente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionDTO> actualizarNotificacion(
            @Parameter(description = "ID de la notificación a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "DTO con los nuevos datos de la notificación", required = true)
            @Valid @RequestBody NotificacionDTO dto) {
        NotificacionDTO notificacionActualizada = notificacionService.actualizarNotificacion(id, dto);
        return ResponseEntity.ok(notificacionActualizada);
    }

    @Operation(
        summary = "Eliminar notificación", 
        description = "Elimina una notificación del sistema por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(
            @Parameter(description = "ID de la notificación a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    // ============ ENDPOINTS POR USUARIO ============

    @Operation(
        summary = "Listar notificaciones por usuario", 
        description = "Obtiene todas las notificaciones de un usuario específico, ordenadas por fecha descendente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "User ID inválido")
    })
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(
            @Parameter(description = "ID del usuario", example = "123", required = true)
            @PathVariable Long userId) {
        List<NotificacionDTO> notificaciones = notificacionService.listarPorUsuario(userId);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Listar notificaciones no leídas por usuario", 
        description = "Obtiene las notificaciones no leídas de un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificaciones no leídas obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "User ID inválido")
    })
    @GetMapping("/usuario/{userId}/no-leidas")
    public ResponseEntity<List<NotificacionDTO>> listarNoLeidasPorUsuario(
            @Parameter(description = "ID del usuario", example = "123", required = true)
            @PathVariable Long userId) {
        List<NotificacionDTO> notificaciones = notificacionService.listarNoLeidasPorUsuario(userId);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Contar notificaciones no leídas por usuario", 
        description = "Obtiene el número de notificaciones no leídas de un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente"),
        @ApiResponse(responseCode = "400", description = "User ID inválido")
    })
    @GetMapping("/usuario/{userId}/contar-no-leidas")
    public ResponseEntity<Long> contarNoLeidasPorUsuario(
            @Parameter(description = "ID del usuario", example = "123", required = true)
            @PathVariable Long userId) {
        Long count = notificacionService.contarNoLeidasPorUsuario(userId);
        return ResponseEntity.ok(count);
    }

    @Operation(
        summary = "Marcar notificación como leída", 
        description = "Marca una notificación específica como leída."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PatchMapping("/{id}/leer")
    public ResponseEntity<NotificacionDTO> marcarComoLeida(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
            @PathVariable Long id) {
        NotificacionDTO notificacion = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(notificacion);
    }

    @Operation(
        summary = "Marcar notificación como no leída", 
        description = "Marca una notificación específica como no leída."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como no leída exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PatchMapping("/{id}/no-leer")
    public ResponseEntity<NotificacionDTO> marcarComoNoLeida(
            @Parameter(description = "ID de la notificación", example = "1", required = true)
            @PathVariable Long id) {
        NotificacionDTO notificacion = notificacionService.marcarComoNoLeida(id);
        return ResponseEntity.ok(notificacion);
    }

    @Operation(
        summary = "Marcar todas como leídas", 
        description = "Marca todas las notificaciones no leídas de un usuario como leídas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Todas las notificaciones marcadas como leídas"),
        @ApiResponse(responseCode = "400", description = "User ID inválido")
    })
    @PatchMapping("/usuario/{userId}/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasComoLeidas(
            @Parameter(description = "ID del usuario", example = "123", required = true)
            @PathVariable Long userId) {
        notificacionService.marcarTodasComoLeidas(userId);
        return ResponseEntity.noContent().build();
    }

    // ============ ENDPOINTS ADICIONALES ============

    @Operation(
        summary = "Listar por prioridad", 
        description = "Obtiene todas las notificaciones de una prioridad específica."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Prioridad no encontrada")
    })
    @GetMapping("/prioridad/{prioridadId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorPrioridad(
            @Parameter(description = "ID de la prioridad", example = "1", required = true)
            @PathVariable Long prioridadId) {
        List<NotificacionDTO> notificaciones = notificacionService.listarPorPrioridad(prioridadId);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Listar por tipo de notificación", 
        description = "Obtiene todas las notificaciones de un tipo específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrada")
    })
    @GetMapping("/tipo/{tipoNotificacionId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorTipoNotificacion(
            @Parameter(description = "ID del tipo de notificación", example = "2", required = true)
            @PathVariable Long tipoNotificacionId) {
        List<NotificacionDTO> notificaciones = notificacionService.listarPorTipoNotificacion(tipoNotificacionId);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Listar notificaciones recientes", 
        description = "Obtiene las notificaciones creadas después de una fecha específica."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/recientes")
    public ResponseEntity<List<NotificacionDTO>> listarRecientes(
            @Parameter(description = "Fecha desde la cual buscar (formato: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde) {
        List<NotificacionDTO> notificaciones = notificacionService.listarRecientes(desde);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(
        summary = "Listar notificaciones pendientes de acknowledgment", 
        description = "Obtiene las notificaciones no leídas que requieren confirmación de lectura."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/pendientes-ack")
    public ResponseEntity<List<NotificacionDTO>> listarPendientesDeAck() {
        List<NotificacionDTO> notificaciones = notificacionService.listarPendientesDeAck();
        return ResponseEntity.ok(notificaciones);
    }
}