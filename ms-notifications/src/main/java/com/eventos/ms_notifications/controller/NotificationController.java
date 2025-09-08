package com.eventos.ms_notifications.controller;

import com.eventos.ms_notifications.dto.NotificationDTO;
import com.eventos.ms_notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notificaciones")
@Tag(name = "Notificaciones", description = "REST API para gestión de notificaciones")
public class NotificationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET por ID
    @Operation(
        summary = "${api.notification.get-by-id.summary}",
        description = "${api.notification.get-by-id.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
    })
    @GetMapping("/{id}")
    public NotificationDTO getNotificacionById(
            @Parameter(description = "${api.notification.get-by-id.parameters.notificationId}", required = true)
            @PathVariable("id") Long id) {
        LOGGER.info("Buscando notificación con ID: {}", id);
        return notificationService.getNotificacionById(id);
    }

    // GET ALL
    @Operation(
        summary = "${api.notification.get-all.summary}",
        description = "${api.notification.get-all.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}")
    })
    @GetMapping("/")
    public List<NotificationDTO> getAllNotifications() {
        LOGGER.info("Obteniendo todas las notificaciones");
        return notificationService.getAllNotifications();
    }

    // POST
    @Operation(
        summary = "${api.notification.create.summary}",
        description = "${api.notification.create.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
    })
    @PostMapping
    public NotificationDTO createNotification(@Valid @RequestBody NotificationDTO newNotification) {
        LOGGER.info("Creando nueva notificación: {}", newNotification);
        return notificationService.createNotification(newNotification);
    }

    // PUT
    @Operation(
        summary = "${api.notification.update.summary}",
        description = "${api.notification.update.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
    })
    @PutMapping("/{id}")
    public NotificationDTO updateNotification(@PathVariable Long id, @Valid @RequestBody NotificationDTO updatedNotification) {
        LOGGER.info("Actualizando notificación con ID: {}", id);
        return notificationService.updateNotification(id, updatedNotification);
    }

    // DELETE
    @Operation(
        summary = "${api.notification.delete.summary}",
        description = "${api.notification.delete.description}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
        @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
        @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
    })
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        LOGGER.info("Eliminando notificación con ID: {}", id);
        notificationService.deleteNotification(id);
    }
}
