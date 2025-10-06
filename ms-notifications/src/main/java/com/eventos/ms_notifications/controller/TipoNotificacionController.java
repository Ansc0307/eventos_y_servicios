package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.service.TipoNotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipos-notificacion")
@Tag(name = "2. Tipos de Notificación", description = "API REST para la gestión de tipos de notificación")
@CrossOrigin(origins = "*")
public class TipoNotificacionController {

    private final TipoNotificacionService tipoNotificacionService;

    public TipoNotificacionController(TipoNotificacionService tipoNotificacionService) {
        this.tipoNotificacionService = tipoNotificacionService;
    }

    @Operation(
        summary = "Crear nuevo tipo de notificación", 
        description = "Crea un nuevo tipo de notificación en el sistema. El nombre debe ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de notificación creado exitosamente",
                    content = @Content(schema = @Schema(implementation = TipoNotificacionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un tipo de notificación con el mismo nombre")
    })
    @PostMapping
    public ResponseEntity<TipoNotificacionDTO> crearTipoNotificacion(
            @Parameter(description = "DTO con los datos del tipo de notificación a crear", required = true)
            @Valid @RequestBody TipoNotificacionDTO dto) {
        TipoNotificacionDTO nuevoTipo = tipoNotificacionService.crearTipoNotificacion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
    }

    @Operation(
        summary = "Listar todos los tipos de notificación", 
        description = "Obtiene una lista de todos los tipos de notificación registrados en el sistema, ordenados por nombre."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de notificación obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<TipoNotificacionDTO>> listarTiposNotificacion() {
        List<TipoNotificacionDTO> tipos = tipoNotificacionService.listarTiposNotificacion();
        return ResponseEntity.ok(tipos);
    }

    @Operation(
        summary = "Obtener tipo de notificación por ID", 
        description = "Obtiene un tipo de notificación específico por su ID único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de notificación encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoNotificacionDTO> obtenerPorId(
            @Parameter(description = "ID del tipo de notificación a buscar", example = "1", required = true)
            @PathVariable Long id) {
        TipoNotificacionDTO tipo = tipoNotificacionService.obtenerPorId(id);
        return ResponseEntity.ok(tipo);
    }

    @Operation(
        summary = "Actualizar tipo de notificación", 
        description = "Actualiza los datos de un tipo de notificación existente. Verifica que el nombre sea único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de notificación actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto con nombre de tipo de notificación existente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoNotificacionDTO> actualizarTipoNotificacion(
            @Parameter(description = "ID del tipo de notificación a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "DTO con los nuevos datos del tipo de notificación", required = true)
            @Valid @RequestBody TipoNotificacionDTO dto) {
        TipoNotificacionDTO tipoActualizado = tipoNotificacionService.actualizarTipoNotificacion(id, dto);
        return ResponseEntity.ok(tipoActualizado);
    }

    @Operation(
        summary = "Eliminar tipo de notificación", 
        description = "Elimina un tipo de notificación del sistema por su ID. Esta acción no se puede deshacer."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de notificación eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoNotificacion(
            @Parameter(description = "ID del tipo de notificación a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        tipoNotificacionService.eliminarTipoNotificacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar tipos de notificación activos", 
        description = "Obtiene una lista de todos los tipos de notificación que están activos en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos activos obtenida exitosamente")
    })
    @GetMapping("/activos")
    public ResponseEntity<List<TipoNotificacionDTO>> listarTiposActivos() {
        List<TipoNotificacionDTO> tiposActivos = tipoNotificacionService.listarTiposActivos();
        return ResponseEntity.ok(tiposActivos);
    }

    @Operation(
        summary = "Listar tipos que requieren confirmación", 
        description = "Obtiene una lista de todos los tipos de notificación que requieren confirmación de lectura (acknowledgment)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos que requieren ACK obtenida exitosamente")
    })
    @GetMapping("/requieren-ack")
    public ResponseEntity<List<TipoNotificacionDTO>> listarTiposQueRequierenAck() {
        List<TipoNotificacionDTO> tiposConAck = tipoNotificacionService.listarTiposQueRequierenAck();
        return ResponseEntity.ok(tiposConAck);
    }

    @Operation(
        summary = "Listar tipos activos que requieren confirmación", 
        description = "Obtiene una lista de todos los tipos de notificación activos que requieren confirmación de lectura."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping("/activos/requieren-ack")
    public ResponseEntity<List<TipoNotificacionDTO>> listarTiposActivosQueRequierenAck() {
        List<TipoNotificacionDTO> tipos = tipoNotificacionService.listarTiposActivosQueRequierenAck();
        return ResponseEntity.ok(tipos);
    }

    @Operation(
        summary = "Verificar existencia por nombre", 
        description = "Verifica si existe un tipo de notificación con el nombre especificado en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/existe/{nombre}")
    public ResponseEntity<Boolean> existePorNombre(
            @Parameter(description = "Nombre del tipo de notificación a verificar", example = "ALERTA", required = true)
            @PathVariable String nombre) {
        boolean existe = tipoNotificacionService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    @Operation(
        summary = "Buscar por nombre", 
        description = "Busca tipos de notificación cuyo nombre contenga el texto especificado (búsqueda case insensitive)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<List<TipoNotificacionDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", example = "aler", required = true)
            @PathVariable String nombre) {
        List<TipoNotificacionDTO> tipos = tipoNotificacionService.buscarPorNombreContaining(nombre);
        return ResponseEntity.ok(tipos);
    }

    @Operation(
        summary = "Desactivar tipo de notificación", 
        description = "Desactiva un tipo de notificación (soft delete) en lugar de eliminarlo físicamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de notificación desactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<TipoNotificacionDTO> desactivarTipoNotificacion(
            @Parameter(description = "ID del tipo de notificación a desactivar", example = "1", required = true)
            @PathVariable Long id) {
        TipoNotificacionDTO tipoDesactivado = tipoNotificacionService.desactivarTipoNotificacion(id);
        return ResponseEntity.ok(tipoDesactivado);
    }

    @Operation(
        summary = "Activar tipo de notificación", 
        description = "Reactiva un tipo de notificación que estaba desactivado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de notificación activado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<TipoNotificacionDTO> activarTipoNotificacion(
            @Parameter(description = "ID del tipo de notificación a activar", example = "1", required = true)
            @PathVariable Long id) {
        TipoNotificacionDTO tipoActivado = tipoNotificacionService.activarTipoNotificacion(id);
        return ResponseEntity.ok(tipoActivado);
    }

    @Operation(
        summary = "Cambiar estado de confirmación", 
        description = "Cambia el estado de requerimiento de confirmación de lectura de un tipo de notificación."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de ACK actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de notificación no encontrado")
    })
    @PatchMapping("/{id}/requiere-ack/{requiereAck}")
    public ResponseEntity<TipoNotificacionDTO> cambiarEstadoAck(
            @Parameter(description = "ID del tipo de notificación", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado de requerimiento de ACK", example = "true", required = true)
            @PathVariable Boolean requiereAck) {
        TipoNotificacionDTO tipoActualizado = tipoNotificacionService.cambiarEstadoAck(id, requiereAck);
        return ResponseEntity.ok(tipoActualizado);
    }
}