package com.eventos.ms_notifications.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_notifications.dto.NotificactionDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/notificaciones")
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    @GetMapping(value = "/{id}", produces = "application/json")
    public NotificactionDTO getNotificacionById(@PathVariable("id") Long id) {
        LOGGER.info("Buscando notificación con ID: {}", id);

        if(id <= 0) {
            throw new InvalidInputException("El ID de la notificación debe ser un número positivo: " + id);
        }

        if(id==100){
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

    @GetMapping
    public NotificactionDTO getNotificationIdParam(@RequestParam Long id) {
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
    
    @GetMapping("/")
    public List<NotificactionDTO> getAllNotifications() {
        LOGGER.info("Obteniendo todas las notificaciones");

        List<NotificactionDTO> lista = new ArrayList<>();

        for (long i = 1; i <= 5; i++) { // luego hay que cambiar y que sólo devuelva lo de la BD!!!
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

        @PostMapping
        public NotificactionDTO createNotification(@RequestBody NotificactionDTO newNotification) {
            LOGGER.info("Creando nueva notificación: {}", newNotification);
            // por ahora algo random
            newNotification.setId(System.currentTimeMillis()); // solo para simular BORRAR!!!
            newNotification.setFechaCreacion(java.time.LocalDateTime.now());

            return newNotification;
        }
   
}
