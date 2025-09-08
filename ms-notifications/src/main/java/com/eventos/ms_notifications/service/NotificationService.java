package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificationDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.exception.UnauthorizedException;
import com.eventos.ms_notifications.exception.ConflictException;
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
        if (id == 500) {
            throw new UnauthorizedException("No tienes permisos para ver la notificación con ID: " + id);
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
        if (newNotification.getUserId() != null && newNotification.getUserId() == 1111) {
            throw new NotFoundException("No se encontró el usuario con ID: " + newNotification.getUserId());
        }
        if ("SISTEMA".equalsIgnoreCase(newNotification.getTipoNotificacion())) {
            throw new ConflictException("El tipo de notificación genera un conflicto: " + newNotification.getTipoNotificacion());
        }

        newNotification.setId(999L); // ID simulado
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
        if (id == 500) {
            throw new UnauthorizedException("No tienes permisos para modificar la notificación con ID: " + id);
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
        if (id == 500) {
            throw new UnauthorizedException("No tienes permisos para eliminar la notificación con ID: " + id);
        }
    }
}
