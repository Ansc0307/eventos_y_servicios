package com.eventos.notifications.controller;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.notifications.dto.NotificationDTO;
import com.eventos.notifications.exception.InvalidInputException;
import com.eventos.notifications.exception.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    private static final Logger LOGGER = Logger.getLogger(NotificationController.class.getName());

    @Operation(
        summary = "Obtiene una notificación por su ID",
        description = "Devuelve la notificación correspondiente al ID dado. Lanza error si el ID no es válido o no existe."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "400", description = "El ID de la notificación es inválido"),
        @ApiResponse(responseCode = "404", description = "No se encontró una notificación con el ID proporcionado")
    })
    @GetMapping("/{notificationId}")
    public NotificationDTO getNotificationById(
        @Parameter(description = "ID de la notificación", required = true)
        @PathVariable String notificationId
    ) {
        LOGGER.info("Obteniendo notificación con id: " + notificationId);

        if (notificationId == null || notificationId.trim().isEmpty()) {
            throw new InvalidInputException("El id de la notificación no puede estar vacío");
        }

        if (notificationId.equals("999")) {
            throw new NotFoundException("No existe notificación con id: " + notificationId);
        }

        if (Integer.parseInt(notificationId) <= 0) {
            throw new InvalidInputException("El id de la notificación debe ser mayor a cero");  
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationId);
        notificationDTO.setTitulo("Nueva notificación");
        notificationDTO.setMensaje("Nueva notificación, id: " + notificationId);
        notificationDTO.setLeido(false);

        return notificationDTO;
    }
}
