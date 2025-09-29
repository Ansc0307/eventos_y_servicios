package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
import com.eventos.ms_notifications.mapper.PrioridadMapper;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;

    public PrioridadService(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    // Crear prioridad
    public PrioridadDTO crearPrioridad(PrioridadDTO dto) {
        Prioridad prioridad = PrioridadMapper.toEntity(dto);
        Prioridad saved = prioridadRepository.save(prioridad);
        return PrioridadMapper.toDTO(saved);
    }

    // Listar todas las prioridades
    public List<PrioridadDTO> listarPrioridades() {
        return prioridadRepository.findAll()
                .stream()
                .map(PrioridadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener prioridad por ID
    public PrioridadDTO obtenerPorId(Long id) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada"));
        return PrioridadMapper.toDTO(prioridad);
    }

    // Actualizar prioridad
    public PrioridadDTO actualizarPrioridad(Long id, PrioridadDTO dto) {
        Prioridad prioridad = prioridadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada"));

        // Actualizamos los campos
        prioridad.setNombre(dto.getNombre());
        prioridad.setDescripcion(dto.getDescripcion());
        prioridad.setNivel(dto.getNivel());
        prioridad.setColorHex(dto.getColorHex());
        prioridad.setActivo(dto.getActivo());

        Prioridad updated = prioridadRepository.save(prioridad);
        return PrioridadMapper.toDTO(updated);
    }

    // Eliminar prioridad
    public void eliminarPrioridad(Long id) {
        if (!prioridadRepository.existsById(id)) {
            throw new RuntimeException("Prioridad no encontrada");
        }
        prioridadRepository.deleteById(id);
    }
}
