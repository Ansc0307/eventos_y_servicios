package com.eventos.ms_notifications.service;

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

    public Flux<NotificacionDTO> obtenerTodas() {
        return notificacionRepository.findAll()
                .map(notificacionMapper::toDto);
    }

    public Mono<NotificacionDTO> obtenerPorId(Long id) {
        return notificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Notificación no encontrada con ID: " + id)))
                .map(notificacionMapper::toDto);
    }

    public Mono<NotificacionDTO> crear(NotificacionDTO dto) {
        // Validaciones básicas
        if (dto.getAsunto() == null || dto.getAsunto().isBlank()) {
            return Mono.error(new InvalidInputException("El asunto de la notificación no puede estar vacío."));
        }
        if (dto.getMensaje() == null || dto.getMensaje().isBlank()) {
            return Mono.error(new InvalidInputException("El mensaje de la notificación no puede estar vacío."));
        }
        if (dto.getPrioridad() == null || dto.getPrioridad().getId() == null) {
            return Mono.error(new InvalidInputException("La prioridad de la notificación no puede ser nula."));
        }
        if (dto.getTipoNotificacion() == null || dto.getTipoNotificacion().getId() == null) {
            return Mono.error(new InvalidInputException("El tipo de notificación no puede ser nulo."));
        }
        if (dto.getUserId() == null) {
            return Mono.error(new InvalidInputException("El ID del usuario no puede ser nulo."));
        }
        if (dto.getLeido() == null) {
            dto.setLeido(false); // Por defecto no leído
        }

        // Crear entidad desde DTO usando solo IDs
        Notificacion notificacion = notificacionMapper.toEntity(dto);

        // Guardar en R2DBC
        return notificacionRepository.save(notificacion)
                .map(notificacionMapper::toDto);
    }

    public Mono<NotificacionDTO> actualizar(Long id, NotificacionDTO dto) {
        return notificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la notificación con ID: " + id)))
                .flatMap(existente -> {
                    // Validaciones básicas
                    if (dto.getAsunto() == null || dto.getAsunto().isBlank()) {
                        return Mono.error(new InvalidInputException("El asunto de la notificación no puede estar vacío."));
                    }
                    if (dto.getMensaje() == null || dto.getMensaje().isBlank()) {
                        return Mono.error(new InvalidInputException("El mensaje de la notificación no puede estar vacío."));
                    }
                    if (dto.getPrioridad() == null || dto.getPrioridad().getId() == null) {
                        return Mono.error(new InvalidInputException("La prioridad de la notificación no puede ser nula."));
                    }
                    if (dto.getTipoNotificacion() == null || dto.getTipoNotificacion().getId() == null) {
                        return Mono.error(new InvalidInputException("El tipo de notificación no puede ser nulo."));
                    }
                    if (dto.getUserId() == null) {
                        return Mono.error(new InvalidInputException("El ID del usuario no puede ser nulo."));
                    }

                    // Actualizar campos
                    existente.setAsunto(dto.getAsunto());
                    existente.setMensaje(dto.getMensaje());
                    existente.setLeido(dto.getLeido());
                    existente.setUserId(dto.getUserId());
                    existente.setPrioridadId(dto.getPrioridad().getId());
                    existente.setTipoNotificacionId(dto.getTipoNotificacion().getId());

                    return notificacionRepository.save(existente)
                            .map(notificacionMapper::toDto);
                });
    }

    public Mono<Void> eliminar(Long id) {
        return notificacionRepository.existsById(id)
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new NotFoundException("No existe la notificación con ID: " + id));
                    }
                    return notificacionRepository.deleteById(id);
                });
    }

    public Flux<NotificacionDTO> obtenerPorUsuario(Long userId) {
        return notificacionRepository.findByUserId(userId)
                .map(notificacionMapper::toDto);
    }

    public Mono<NotificacionDTO> marcarComoLeida(Long id) {
        return notificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la notificación con ID: " + id)))
                .flatMap(notificacion -> {
                    notificacion.setLeido(true);
                    return notificacionRepository.save(notificacion)
                            .map(notificacionMapper::toDto);
                });
    }
}
