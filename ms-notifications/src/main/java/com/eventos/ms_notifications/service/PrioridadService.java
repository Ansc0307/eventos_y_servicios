package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.PrioridadDTO;
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

    public PrioridadDTO crearPrioridad(PrioridadDTO dto) {
        Prioridad prioridad = new Prioridad(
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getNivel(),
                dto.getColorHex(),
                dto.getActivo()
        );
        Prioridad saved = prioridadRepository.save(prioridad);
        dto.setId(saved.getId());
        return dto;
    }

    public List<PrioridadDTO> listarPrioridades() {
        return prioridadRepository.findAll()
                .stream()
                .map(p -> {
                    PrioridadDTO dto = new PrioridadDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setDescripcion(p.getDescripcion());
                    dto.setNivel(p.getNivel());
                    dto.setColorHex(p.getColorHex());
                    dto.setActivo(p.getActivo());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
