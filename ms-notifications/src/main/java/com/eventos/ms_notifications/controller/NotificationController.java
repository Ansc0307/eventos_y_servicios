package com.eventos.ms_notifications.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_notifications.dto.NotificactionDTO;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/notificaciones")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @GetMapping(value = "/{id}", produces = "application/json")
    public NotificactionDTO getNotificacionById(@PathVariable("id") Long id) {
        LOGGER.info("Buscando notificación con ID: {}", id);
        
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
    
}
