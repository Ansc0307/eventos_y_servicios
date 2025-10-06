package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.exception.ConflictException;
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

    // ============ MÉTODOS CRUD BÁSICOS ============
    
    public PrioridadDTO crearPrioridad(PrioridadDTO dto) {
        if (prioridadRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new ConflictException("Prioridad", "nombre", dto.getNombre());
        }
        
        Prioridad prioridad = prioridadMapper.toEntity(dto);
        Prioridad saved = prioridadRepository.save(prioridad);
        return prioridadMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PrioridadDTO> listarPrioridades() {
        return prioridadRepository.findAllByOrderByNivelAsc()
                .stream()
                .map(prioridadMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrioridadDTO obtenerPorId(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prioridad", id));
        return prioridadMapper.toDTO(prioridad);
    }

    public PrioridadDTO actualizarPrioridad(Long id, PrioridadDTO dto) {
        Prioridad existingPrioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prioridad", id));
        
        prioridadRepository.findByNombre(dto.getNombre())
                .ifPresent(prioridadConMismoNombre -> {
                    if (!prioridadConMismoNombre.getId().equals(id)) {
                        throw new ConflictException("Prioridad", "nombre", dto.getNombre());
                    }
                });
        
        prioridadMapper.updateEntityFromDTO(dto, existingPrioridad);
        Prioridad updated = prioridadRepository.save(existingPrioridad);
        return prioridadMapper.toDTO(updated);
    }

    public void eliminarPrioridad(Long id) {
        if (!prioridadRepository.existsById(id)) {
            throw new NotFoundException("Prioridad", id);
        }
        prioridadRepository.deleteById(id);
    }

    // ============ MÉTODOS ADICIONALES ============
    
    @Transactional(readOnly = true)
    public List<PrioridadDTO> listarPrioridadesActivas() {
        return prioridadRepository.findByActivoTrue()
                .stream()
                .map(prioridadMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return prioridadRepository.findByNombre(nombre).isPresent();
    }

    @Transactional(readOnly = true)
    public List<PrioridadDTO> buscarPorNivel(Integer nivel) {
        List<Prioridad> prioridades = prioridadRepository.findByNivel(nivel);
        if (prioridades.isEmpty()) {
            throw new NotFoundException("No se encontraron prioridades con nivel: " + nivel);
        }
        return prioridades.stream()
                .map(prioridadMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PrioridadDTO desactivarPrioridad(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prioridad", id));
        
        prioridad.setActivo(false);
        Prioridad updated = prioridadRepository.save(prioridad);
        return prioridadMapper.toDTO(updated);
    }

    public PrioridadDTO activarPrioridad(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prioridad", id));
        
        prioridad.setActivo(true);
        Prioridad updated = prioridadRepository.save(prioridad);
        return prioridadMapper.toDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<PrioridadDTO> buscarPorNombreContaining(String nombre) {
        return prioridadRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(prioridadMapper::toDTO)
                .collect(Collectors.toList());
    }
}