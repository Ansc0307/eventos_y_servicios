package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.service.TipoNotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tipos-notificacion")
@Tag(name = "Tipos de Notificación", description = "REST API para gestión de tipos de notificación")
public class TipoNotificacionController {

    private final TipoNotificacionService tipoNotificacionService;

    public TipoNotificacionController(TipoNotificacionService tipoNotificacionService) {
        this.tipoNotificacionService = tipoNotificacionService;
    }

    @Operation(summary = "Crear nuevo tipo de notificación")
    @PostMapping
    public ResponseEntity<TipoNotificacionDTO> crearTipo(@Valid @RequestBody TipoNotificacionDTO dto) {
        return ResponseEntity.ok(tipoNotificacionService.crearTipo(dto));
    }

    @Operation(summary = "Listar tipos de notificación")
    @GetMapping
    public ResponseEntity<List<TipoNotificacionDTO>> listarTipos() {
        return ResponseEntity.ok(tipoNotificacionService.listarTipos());
    }
}
