package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
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
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con ID: " + id));
        return prioridadMapper.toDto(prioridad);
    }

    public PrioridadDTO crear(PrioridadDTO prioridadDTO) {
        if (prioridadRepository.existsByNombreIgnoreCase(prioridadDTO.getNombre())) {
            throw new RuntimeException("Ya existe una prioridad con ese nombre");
        }
        Prioridad prioridad = prioridadMapper.toEntity(prioridadDTO);
        return prioridadMapper.toDto(prioridadRepository.save(prioridad));
    }

    public PrioridadDTO actualizar(Long id, PrioridadDTO prioridadDTO) {
        Prioridad existente = prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con ID: " + id));

        existente.setNombre(prioridadDTO.getNombre());
        existente.setDescripcion(prioridadDTO.getDescripcion());

        return prioridadMapper.toDto(prioridadRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!prioridadRepository.existsById(id)) {
            throw new RuntimeException("No existe la prioridad con ID: " + id);
        }
        prioridadRepository.deleteById(id);
    }
}
