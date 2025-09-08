package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificationDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    public NotificationDTO getNotificacionById(Long id) {
        if (id <= 0) {
            throw new InvalidInputException("El ID de la notificación debe ser positivo: " + id);
        }

        if (id == 100) {
            throw new NotFoundException("No se encontró la notificación con ID: " + id);
        }

        return new NotificationDTO(
                id,
                1L,
                "Asunto de prueba",
                "Este es un mensaje de prueba.",
                "ALTA",
                LocalDateTime.now(),
                false,
                "INFORMATIVA"
        );
    }

    public List<NotificationDTO> getAllNotifications() {
        List<NotificationDTO> lista = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            lista.add(new NotificationDTO(
                    i,
                    i * 10,
                    "Asunto de prueba " + i,
                    "Mensaje de prueba " + i,
                    i % 2 == 0 ? "ALTA" : "BAJA",
                    LocalDateTime.now(),
                    false,
                    "INFORMATIVA"
            ));
        }
        return lista;
    }

    public NotificationDTO createNotification(NotificationDTO newNotification) {
        newNotification.setFechaCreacion(LocalDateTime.now());
        return newNotification;
    }

    public NotificationDTO updateNotification(Long id, NotificationDTO updatedNotification) {
        if (id <= 0) {
            throw new InvalidInputException("El ID de la notificación debe ser positivo: " + id);
        }
        if (id == 100) {
            throw new NotFoundException("No se encontró la notificación con ID: " + id);
        }
        updatedNotification.setId(id);
        updatedNotification.setFechaCreacion(LocalDateTime.now());
        return updatedNotification;
    }

    public void deleteNotification(Long id) {
        if (id <= 0) {
            throw new InvalidInputException("El ID de la notificación debe ser positivo: " + id);
        }
        if (id == 100) {
            throw new NotFoundException("No se encontró la notificación con ID: " + id);
        }
        
    }
}

