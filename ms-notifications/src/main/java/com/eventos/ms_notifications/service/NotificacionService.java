package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.NotificacionMapper;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.NotificacionRepository;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PrioridadRepository prioridadRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final NotificacionMapper notificacionMapper;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               PrioridadRepository prioridadRepository,
                               TipoNotificacionRepository tipoNotificacionRepository,
                               NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.prioridadRepository = prioridadRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    public List<NotificacionDTO> obtenerTodas() {
        return notificacionRepository.findAll()
                .stream()
                .map(notificacionMapper::toDto)
                .collect(Collectors.toList());
    }

    public NotificacionDTO obtenerPorId(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación no encontrada con ID: " + id));
        return notificacionMapper.toDto(notificacion);
    }

    public NotificacionDTO crear(NotificacionDTO dto) {
        // Validaciones básicas
        if (dto.getAsunto() == null || dto.getAsunto().isBlank()) {
            throw new InvalidInputException("El asunto de la notificación no puede estar vacío.");
        }
        if (dto.getMensaje() == null || dto.getMensaje().isBlank()) {
            throw new InvalidInputException("El mensaje de la notificación no puede estar vacío.");
        }
        if (dto.getPrioridad() == null || dto.getPrioridad().getId() == null) {
            throw new InvalidInputException("La prioridad de la notificación no puede ser nula.");
        }
        if (dto.getTipoNotificacion() == null || dto.getTipoNotificacion().getId() == null) {
            throw new InvalidInputException("El tipo de notificación no puede ser nulo.");
        }

        if (dto.getUserId() == null) {
            throw new InvalidInputException("El ID del usuario no puede ser nulo.");
        }

        if (dto.getLeido() == null) {
            dto.setLeido(false); // Por defecto no leido
        }

        // Validar prioridad existente
        Prioridad prioridad = null;
        if (dto.getPrioridad() != null && dto.getPrioridad().getId() != null) {
            prioridad = prioridadRepository.findById(dto.getPrioridad().getId())
                    .orElseThrow(() -> new NotFoundException("Prioridad no encontrada con ID: " + dto.getPrioridad().getId()));
        }

        // Validar tipo de notificación existente
        TipoNotificacion tipo = null;
        if (dto.getTipoNotificacion() != null && dto.getTipoNotificacion().getId() != null) {
            tipo = tipoNotificacionRepository.findById(dto.getTipoNotificacion().getId())
                    .orElseThrow(() -> new NotFoundException("Tipo de notificación no encontrado con ID: " + dto.getTipoNotificacion().getId()));
        }

        // Crear entidad desde DTO
        Notificacion notificacion = notificacionMapper.toEntity(dto);
        notificacion.setPrioridad(prioridad);
        notificacion.setTipoNotificacion(tipo);

        // Temporal: hasta que haya microservicio de usuarios
        if (notificacion.getUserId() == null) {
            notificacion.setUserId(999L); // valor estático temporal
        }

        return notificacionMapper.toDto(notificacionRepository.save(notificacion));
    }

    // Actualizar una notificación existente
    public NotificacionDTO actualizar(Long id, NotificacionDTO dto) {
        Notificacion existente = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la notificación con ID: " + id));

        // Validaciones básicas
        if (dto.getAsunto() == null || dto.getAsunto().isBlank()) {
            throw new InvalidInputException("El asunto de la notificación no puede estar vacío.");
        }
        if (dto.getMensaje() == null || dto.getMensaje().isBlank()) {
            throw new InvalidInputException("El mensaje de la notificación no puede estar vacío.");
        }
        if (dto.getPrioridad() == null || dto.getPrioridad().getId() == null) {
            throw new InvalidInputException("La prioridad de la notificación no puede ser nula.");
        }
        if (dto.getTipoNotificacion() == null || dto.getTipoNotificacion().getId() == null) {
            throw new InvalidInputException("El tipo de notificación no puede ser nulo.");
        }

        if (dto.getUserId() == null) {
            throw new InvalidInputException("El ID del usuario no puede ser nulo.");
        }

        existente.setAsunto(dto.getAsunto());
        existente.setMensaje(dto.getMensaje());
        existente.setLeido(dto.getLeido());

        // Actualizar prioridad
        if (dto.getPrioridad() != null && dto.getPrioridad().getId() != null) {
            Prioridad prioridad = prioridadRepository.findById(dto.getPrioridad().getId())
                    .orElseThrow(() -> new NotFoundException("Prioridad no encontrada con ID: " + dto.getPrioridad().getId()));
            existente.setPrioridad(prioridad);
        }

        // Actualizar tipo
        if (dto.getTipoNotificacion() != null && dto.getTipoNotificacion().getId() != null) {
            TipoNotificacion tipo = tipoNotificacionRepository.findById(dto.getTipoNotificacion().getId())
                    .orElseThrow(() -> new NotFoundException("Tipo de notificación no encontrado con ID: " + dto.getTipoNotificacion().getId()));
            existente.setTipoNotificacion(tipo);
        }

        return notificacionMapper.toDto(notificacionRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new NotFoundException("No existe la notificación con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }

    public List<NotificacionDTO> obtenerPorUsuario(Long userId) {
        return notificacionRepository.findByUserId(userId)
                .stream()
                .map(notificacionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Marcar una notificación como leída
    public NotificacionDTO marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la notificación con ID: " + id));
        notificacion.setLeido(true);
        return notificacionMapper.toDto(notificacionRepository.save(notificacion));
    }
}
