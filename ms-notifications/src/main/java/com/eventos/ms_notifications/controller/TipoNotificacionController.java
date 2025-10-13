package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.service.TipoNotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipos-notificacion")
@Tag(name = "TipoNotificacion", description = "CRUD de tipos de notificación")
public class TipoNotificacionController {

    private final TipoNotificacionService tipoNotificacionService;

    public TipoNotificacionController(TipoNotificacionService tipoNotificacionService) {
        this.tipoNotificacionService = tipoNotificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los tipos de notificación")
    public ResponseEntity<List<TipoNotificacionDTO>> listarTodos() {
        return ResponseEntity.ok(tipoNotificacionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un tipo de notificación por ID")
    public ResponseEntity<TipoNotificacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoNotificacionService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de notificación")
    public ResponseEntity<TipoNotificacionDTO> crear(@Valid @RequestBody TipoNotificacionDTO tipoDTO) {
        TipoNotificacionDTO creado = tipoNotificacionService.crear(tipoDTO);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un tipo de notificación existente")
    public ResponseEntity<TipoNotificacionDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoNotificacionDTO tipoDTO) {
        return ResponseEntity.ok(tipoNotificacionService.actualizar(id, tipoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un tipo de notificación por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoNotificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
