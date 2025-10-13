package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.service.PrioridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Listar todas las prioridades", description = "Obtiene una lista completa de las prioridades registradas")
    public ResponseEntity<List<PrioridadDTO>> listar() {
        return ResponseEntity.ok(prioridadService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener prioridad por ID", description = "Devuelve una prioridad según su identificador")
    public ResponseEntity<PrioridadDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prioridadService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva prioridad", description = "Registra una nueva prioridad en el sistema")
    public ResponseEntity<PrioridadDTO> crear(@Valid @RequestBody PrioridadDTO prioridadDTO) {
        return ResponseEntity.ok(prioridadService.crear(prioridadDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una prioridad", description = "Modifica los datos de una prioridad existente")
    public ResponseEntity<PrioridadDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PrioridadDTO prioridadDTO) {
        return ResponseEntity.ok(prioridadService.actualizar(id, prioridadDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una prioridad", description = "Elimina una prioridad existente por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        prioridadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
