package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificationDTO;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public NotificationDTO getNotificacionById(Long id) {
        Notificacion entity = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la notificación con ID: " + id));
        return mapToDTO(entity);
    }

    public List<NotificationDTO> getAllNotifications() {
        return repo.findAll()
                   .stream()
                   .map(this::mapToDTO)
                   .collect(Collectors.toList());
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        Notificacion entity = mapToEntity(dto);
        entity.setFechaCreacion(LocalDateTime.now());
        Notificacion saved = repo.save(entity);
        return mapToDTO(saved);
    }

    public NotificationDTO updateNotification(Long id, NotificationDTO dto) {
        Notificacion existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la notificación con ID: " + id));

        // actualizar campos
        existing.setAsunto(dto.getAsunto());
        existing.setMensaje(dto.getMensaje());
        existing.setPrioridad(com.eventos.ms_notifications.model.Prioridad.valueOf(dto.getPrioridad()));
        existing.setLeido(dto.getLeido());
        existing.setTipoNotificacion(com.eventos.ms_notifications.model.TipoNotificacion.valueOf(dto.getTipoNotificacion()));

        Notificacion updated = repo.save(existing);
        return mapToDTO(updated);
    }

    public void deleteNotification(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("No se encontró la notificación con ID: " + id);
        }
        repo.deleteById(id);
    }

    // 🔹 Helpers de mapeo
    private NotificationDTO mapToDTO(Notification n) {
        return new NotificationDTO(
                n.getId(),
                n.getUserId(),
                n.getAsunto(),
                n.getMensaje(),
                n.getPrioridad().name(),
                n.getFechaCreacion(),
                n.getLeido(),
                n.getTipoNotificacion().name()
        );
    }

    private Notification mapToEntity(NotificationDTO dto) {
        Notification n = new Notification();
        n.setId(dto.getId());
        n.setUserId(dto.getUserId());
        n.setAsunto(dto.getAsunto());
        n.setMensaje(dto.getMensaje());
        n.setPrioridad(com.eventos.ms_notifications.model.Prioridad.valueOf(dto.getPrioridad()));
        n.setFechaCreacion(dto.getFechaCreacion() != null ? dto.getFechaCreacion() : LocalDateTime.now());
        n.setLeido(dto.getLeido());
        n.setTipoNotificacion(com.eventos.ms_notifications.model.TipoNotificacion.valueOf(dto.getTipoNotificacion()));
        return n;
    }
}
