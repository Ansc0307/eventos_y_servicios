package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.exception.ConflictException;
import com.eventos.ms_notifications.exception.InvalidInputException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.PrioridadMapper;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;
    private final PrioridadMapper prioridadMapper;

    public PrioridadService(PrioridadRepository prioridadRepository, PrioridadMapper prioridadMapper) {
        this.prioridadRepository = prioridadRepository;
        this.prioridadMapper = prioridadMapper;
    }

    public List<PrioridadDTO> obtenerTodas() {
        return prioridadRepository.findAll()
                .stream()
                .map(prioridadMapper::toDto)
                .collect(Collectors.toList());
    }

    public PrioridadDTO obtenerPorId(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prioridad no encontrada con ID: " + id));
        return prioridadMapper.toDto(prioridad);
    }

    public PrioridadDTO crear(PrioridadDTO prioridadDTO) {
        if (prioridadDTO.getNombre() == null || prioridadDTO.getNombre().isBlank()) {
            throw new InvalidInputException("El nombre de la prioridad no puede estar vacío.");
        }

        if (prioridadRepository.existsByNombreIgnoreCase(prioridadDTO.getNombre())) {
            throw new ConflictException("Ya existe una prioridad con el nombre '" + prioridadDTO.getNombre() + "'.");
        }

        Prioridad prioridad = prioridadMapper.toEntity(prioridadDTO);
        return prioridadMapper.toDto(prioridadRepository.save(prioridad));
    }

    public PrioridadDTO actualizar(Long id, PrioridadDTO prioridadDTO) {
        Prioridad existente = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la prioridad con ID: " + id));

        if (prioridadDTO.getNombre() == null || prioridadDTO.getNombre().isBlank()) {
            throw new InvalidInputException("El nombre no puede estar vacío.");
        }

        if (prioridadRepository.existsByNombreIgnoreCase(prioridadDTO.getNombre())) {
            throw new ConflictException("Ya existe una prioridad con el nombre '" + prioridadDTO.getNombre() + "'.");
        }

        existente.setNombre(prioridadDTO.getNombre());
        existente.setDescripcion(prioridadDTO.getDescripcion());

        return prioridadMapper.toDto(prioridadRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!prioridadRepository.existsById(id)) {
            throw new NotFoundException("No existe la prioridad con ID: " + id);
        }
        prioridadRepository.deleteById(id);
    }
}
