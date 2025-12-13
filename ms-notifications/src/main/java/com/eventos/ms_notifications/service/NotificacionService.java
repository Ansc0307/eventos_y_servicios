package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificacionConNombresDTO;
import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.NotificacionMapper;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    /**
     * Obtiene todas las notificaciones (CON NOMBRES usando JOIN)
     */
    public Flux<NotificacionDTO> obtenerTodas() {
        return notificacionRepository.findAllWithNames()
                .map(NotificacionConNombresDTO::toNotificacionDTO);
    }

    /**
     * Obtiene una notificación por ID (CON NOMBRES usando JOIN)
     */
    public Mono<NotificacionDTO> obtenerPorId(Long id) {
        return notificacionRepository.findByIdWithNames(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Notificación no encontrada con ID: " + id)))
                .map(NotificacionConNombresDTO::toNotificacionDTO);
    }

    /**
     * Obtiene notificaciones por usuario (CON NOMBRES usando JOIN)
     */
    public Flux<NotificacionDTO> obtenerPorUsuario(Long userId) {
        return notificacionRepository.findByUserIdWithNames(userId)
                .map(NotificacionConNombresDTO::toNotificacionDTO);
    }

    /**
     * Crea una nueva notificación
     * Guarda con IDs y luego obtiene con nombres
     */
    public Mono<NotificacionDTO> crear(NotificacionDTO dto) {
        validarNotificacionDTO(dto);
        
        if (dto.getLeido() == null) {
            dto.setLeido(false);
        }

        Notificacion notificacion = notificacionMapper.toEntity(dto);

        return notificacionRepository.save(notificacion)
                .flatMap(saved -> notificacionRepository.findByIdWithNames(saved.getId()))
                .map(NotificacionConNombresDTO::toNotificacionDTO);
    }

    /**
     * Actualiza una notificación existente
     */
    public Mono<NotificacionDTO> actualizar(Long id, NotificacionDTO dto) {
        return notificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la notificación con ID: " + id)))
                .flatMap(existente -> {
                    validarNotificacionDTO(dto);
                    
                    existente.setAsunto(dto.getAsunto());
                    existente.setMensaje(dto.getMensaje());
                    existente.setLeido(dto.getLeido());
                    existente.setUserId(dto.getUserId());
                    existente.setPrioridadId(dto.getPrioridad().getId());
                    existente.setTipoNotificacionId(dto.getTipoNotificacion().getId());

                    return notificacionRepository.save(existente)
                            .flatMap(saved -> notificacionRepository.findByIdWithNames(saved.getId()))
                            .map(NotificacionConNombresDTO::toNotificacionDTO);
                });
    }

    /**
     * Elimina una notificación
     */
    public Mono<Void> eliminar(Long id) {
        return notificacionRepository.existsById(id)
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new NotFoundException("No existe la notificación con ID: " + id));
                    }
                    return notificacionRepository.deleteById(id);
                });
    }

    /**
     * Marca una notificación como leída
     */
    public Mono<NotificacionDTO> marcarComoLeida(Long id) {
        return notificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la notificación con ID: " + id)))
                .flatMap(notificacion -> {
                    notificacion.setLeido(true);
                    return notificacionRepository.save(notificacion)
                            .flatMap(saved -> notificacionRepository.findByIdWithNames(saved.getId()))
                            .map(NotificacionConNombresDTO::toNotificacionDTO);
                });
    }

    /**
     * Método auxiliar de validación
     */
    private void validarNotificacionDTO(NotificacionDTO dto) {
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
    }
}