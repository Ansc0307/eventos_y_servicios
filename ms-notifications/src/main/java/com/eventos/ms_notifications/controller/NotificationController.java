package com.eventos.ms_notifications.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.eventos.ms_notifications.dto.NotificactionDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/notificaciones")
@Tag(name = "Notificaciones", description = "REST API para gestión de notificaciones")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @Operation(
        summary = "${api.notification.get-by-id.description}",
        description = "${api.notification.get-by-id.notes}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public NotificactionDTO getNotificacionById(
            @Parameter(description = "${api.notification.get-by-id.parameters.notificationId}", required = true)
            @PathVariable("id") Long id) {

        LOGGER.info("Buscando notificación con ID: {}", id);

        if (id <= 0) {
            throw new InvalidInputException("El ID de la notificación debe ser positivo: " + id);
        }

        if (id == 100) {
            throw new NotFoundException("No se encontró la notificación con ID: " + id);
        }

        // Simulación de datos
        NotificactionDTO notificacion = new NotificactionDTO();
        notificacion.setId(id);
        notificacion.setUserId(1L);
        notificacion.setAsunto("Asunto de prueba");
        notificacion.setMensaje("Este es un mensaje de prueba.");
        notificacion.setPrioridad("ALTA");
        notificacion.setFechaCreacion(java.time.LocalDateTime.now());
        notificacion.setLeido(false);
        notificacion.setTipoNotificacion("INFORMATIVA");

        return notificacion;
    }

    @Operation(summary = "Buscar notificación por ID (query param)")
    @GetMapping
    public NotificactionDTO getNotificationIdParam(@RequestParam Long id) {
        LOGGER.info("Buscando notificación con ID: {}", id);

        NotificactionDTO notificacion = new NotificactionDTO();
        notificacion.setId(id);
        notificacion.setUserId(1L);
        notificacion.setAsunto("Asunto de prueba");
        notificacion.setMensaje("Este es un mensaje de prueba.");
        notificacion.setPrioridad("ALTA");
        notificacion.setFechaCreacion(java.time.LocalDateTime.now());
        notificacion.setLeido(false);
        notificacion.setTipoNotificacion("INFORMATIVA");

        return notificacion;
    }

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping("/")
    public List<NotificactionDTO> getAllNotifications() {
        LOGGER.info("Obteniendo todas las notificaciones");

        List<NotificactionDTO> lista = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            NotificactionDTO notificacion = new NotificactionDTO();
            notificacion.setId(i);
            notificacion.setUserId(i * 10);
            notificacion.setAsunto("Asunto de prueba " + i);
            notificacion.setMensaje("Mensaje de prueba " + i);
            notificacion.setPrioridad(i % 2 == 0 ? "ALTA" : "BAJA");
            notificacion.setFechaCreacion(java.time.LocalDateTime.now());
            notificacion.setLeido(false);
            notificacion.setTipoNotificacion("INFORMATIVA");
            lista.add(notificacion);
        }
        return lista;
    }

    @Operation(summary = "Crear una nueva notificación")
    @PostMapping
    public NotificactionDTO createNotification(@RequestBody NotificactionDTO newNotification) {
        LOGGER.info("Creando nueva notificación: {}", newNotification);

        newNotification.setId(System.currentTimeMillis()); // simulado
        newNotification.setFechaCreacion(java.time.LocalDateTime.now());

        return newNotification;
    }
}
