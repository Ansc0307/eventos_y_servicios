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
@Tag(name = "Prioridades", description = "REST API para gestión de prioridades")
public class PrioridadController {

    private final PrioridadService prioridadService;

    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    @Operation(summary = "Crear nueva prioridad")
    @PostMapping
    public ResponseEntity<PrioridadDTO> crearPrioridad(@Valid @RequestBody PrioridadDTO dto) {
        return ResponseEntity.ok(prioridadService.crearPrioridad(dto));
    }

    @Operation(summary = "Listar prioridades")
    @GetMapping
    public ResponseEntity<List<PrioridadDTO>> listarPrioridades() {
        return ResponseEntity.ok(prioridadService.listarPrioridades());
    }
}
