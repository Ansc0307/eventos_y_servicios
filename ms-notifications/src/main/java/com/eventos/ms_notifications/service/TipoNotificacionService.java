package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.exception.ConflictException;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.TipoNotificacionMapper;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TipoNotificacionService {

    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final TipoNotificacionMapper tipoNotificacionMapper;

    public TipoNotificacionService(TipoNotificacionRepository tipoNotificacionRepository,
                                   TipoNotificacionMapper tipoNotificacionMapper) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.tipoNotificacionMapper = tipoNotificacionMapper;
    }

    // Obtener todas las notificaciones
    public Flux<TipoNotificacionDTO> obtenerTodas() {
        return tipoNotificacionRepository.findAll()
                .map(tipoNotificacionMapper::toDto);
    }

    // Obtener por ID
    public Mono<TipoNotificacionDTO> obtenerPorId(Long id) {
        return tipoNotificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Tipo de notificación no encontrado con ID: " + id)))
                .map(tipoNotificacionMapper::toDto);
    }

    // Crear
    public Mono<TipoNotificacionDTO> crear(TipoNotificacionDTO tipoDTO) {
        if (tipoDTO.getNombre() == null || tipoDTO.getNombre().isBlank()) {
            return Mono.error(new InvalidInputException("El nombre del tipo de notificación no puede estar vacío."));
        }

        return tipoNotificacionRepository.existsByNombreIgnoreCase(tipoDTO.getNombre())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ConflictException(
                                "Ya existe un tipo de notificación con el nombre '" + tipoDTO.getNombre() + "'."));
                    }
                    TipoNotificacion tipo = tipoNotificacionMapper.toEntity(tipoDTO);
                    return tipoNotificacionRepository.save(tipo)
                            .map(tipoNotificacionMapper::toDto);
                });
    }

    // Actualizar
    public Mono<TipoNotificacionDTO> actualizar(Long id, TipoNotificacionDTO tipoDTO) {
        if (tipoDTO.getNombre() == null || tipoDTO.getNombre().isBlank()) {
            return Mono.error(new InvalidInputException("El nombre no puede estar vacío."));
        }

        return tipoNotificacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró el tipo de notificación con ID: " + id)))
                .flatMap(existente -> {
                    existente.setNombre(tipoDTO.getNombre());
                    existente.setDescripcion(tipoDTO.getDescripcion());
                    return tipoNotificacionRepository.save(existente)
                            .map(tipoNotificacionMapper::toDto);
                });
    }

    // Eliminar
    public Mono<Void> eliminar(Long id) {
        return tipoNotificacionRepository.existsById(id)
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new NotFoundException("No existe el tipo de notificación con ID: " + id));
                    }
                    return tipoNotificacionRepository.deleteById(id);
                });
    }
}
