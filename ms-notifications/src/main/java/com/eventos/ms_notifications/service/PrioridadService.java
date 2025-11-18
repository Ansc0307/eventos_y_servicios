package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.exception.ConflictException;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.PrioridadMapper;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;
    private final PrioridadMapper prioridadMapper;

    public PrioridadService(PrioridadRepository prioridadRepository, PrioridadMapper prioridadMapper) {
        this.prioridadRepository = prioridadRepository;
        this.prioridadMapper = prioridadMapper;
    }

    // Obtener todas las prioridades
    public Flux<PrioridadDTO> obtenerTodas() {
        return prioridadRepository.findAll()
                .map(prioridadMapper::toDto);
    }

    // Obtener por ID
    public Mono<PrioridadDTO> obtenerPorId(Long id) {
        return prioridadRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Prioridad no encontrada con ID: " + id)))
                .map(prioridadMapper::toDto);
    }

    // Crear
    public Mono<PrioridadDTO> crear(PrioridadDTO prioridadDTO) {
        if (prioridadDTO.getNombre() == null || prioridadDTO.getNombre().isBlank()) {
            return Mono.error(new InvalidInputException("El nombre de la prioridad no puede estar vacío."));
        }

        return prioridadRepository.existsByNombreIgnoreCase(prioridadDTO.getNombre())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new ConflictException(
                                "Ya existe una prioridad con el nombre '" + prioridadDTO.getNombre() + "'."));
                    }
                    Prioridad prioridad = prioridadMapper.toEntity(prioridadDTO);
                    return prioridadRepository.save(prioridad)
                            .map(prioridadMapper::toDto);
                });
    }

    // Actualizar
    public Mono<PrioridadDTO> actualizar(Long id, PrioridadDTO prioridadDTO) {
        if (prioridadDTO.getNombre() == null || prioridadDTO.getNombre().isBlank()) {
            return Mono.error(new InvalidInputException("El nombre no puede estar vacío."));
        }

        return prioridadRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la prioridad con ID: " + id)))
                .flatMap(existente -> prioridadRepository.existsByNombreIgnoreCase(prioridadDTO.getNombre())
                        .flatMap(existe -> {
                            if (existe && !existente.getId().equals(id)) {
                                return Mono.error(new ConflictException(
                                        "Ya existe una prioridad con el nombre '" + prioridadDTO.getNombre() + "'."));
                            }
                            existente.setNombre(prioridadDTO.getNombre());
                            existente.setDescripcion(prioridadDTO.getDescripcion());
                            return prioridadRepository.save(existente)
                                    .map(prioridadMapper::toDto);
                        }));
    }

    // Eliminar
    public Mono<Void> eliminar(Long id) {
        return prioridadRepository.existsById(id)
                .flatMap(existe -> {
                    if (!existe) {
                        return Mono.error(new NotFoundException("No existe la prioridad con ID: " + id));
                    }
                    return prioridadRepository.deleteById(id);
                });
    }
}
