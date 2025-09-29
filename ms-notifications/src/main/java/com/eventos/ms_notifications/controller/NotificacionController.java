package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.NotificacionCreateDTO;
import com.eventos.ms_notifications.dto.NotificacionResponseDTO;
import com.eventos.ms_notifications.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notificaciones")
@Tag(name = "Notificaciones", description = "REST API para gestión de notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Crear nueva notificación")
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(
            @Valid @RequestBody NotificacionCreateDTO dto) {
        return ResponseEntity.ok(notificacionService.crearNotificacion(dto));
    }

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {
        return ResponseEntity.ok(notificacionService.listarTodas());
    }
    
}
