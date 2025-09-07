package com.eventos.ms_reservas.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.exception.DisponibleNotFoundException;
import com.eventos.ms_reservas.exception.DisponibleOcupadoException;
import com.eventos.ms_reservas.exception.FechaInvalidaException;

@Service
public class DisponibleService {

    //private final List<DisponibleDTO> disponibles = new ArrayList<>();
    private static final List<DisponibleDTO> disponibles = new ArrayList<>();

public DisponibleService() {
    if (disponibles.isEmpty()) {
        disponibles.add(new DisponibleDTO("1", "Disponible normal",
                LocalDateTime.of(2025, 9, 20, 10, 0),
                LocalDateTime.of(2025, 9, 20, 12, 0),
                true));
        disponibles.add(new DisponibleDTO("2", "Sala ocupada",
                LocalDateTime.of(2025, 9, 21, 10, 0),
                LocalDateTime.of(2025, 9, 21, 12, 0),
                false));
        disponibles.add(new DisponibleDTO("3", "Fechas inválidas",
                LocalDateTime.of(2025, 9, 22, 12, 0),
                LocalDateTime.of(2025, 9, 22, 10, 0),
                true));
    }
}


/* 
    public DisponibleService() {
        DisponibleDTO d1 = new DisponibleDTO("1", "Disponible normal",
                LocalDateTime.of(2025, 9, 20, 10, 0),
                LocalDateTime.of(2025, 9, 20, 12, 0),
                true);

        DisponibleDTO d2 = new DisponibleDTO("2", "Sala ocupada",
                LocalDateTime.of(2025, 9, 21, 10, 0),
                LocalDateTime.of(2025, 9, 21, 12, 0),
                false);

        DisponibleDTO d3 = new DisponibleDTO("3", "Fechas inválidas",
                LocalDateTime.of(2025, 9, 22, 12, 0),
                LocalDateTime.of(2025, 9, 22, 10, 0),
                true);

        disponibles.add(d1);
        disponibles.add(d2);
        disponibles.add(d3);
    }*/

    public DisponibleDTO obtenerPorId(Long id) {
        return disponibles.stream()
                .filter(d -> d.getId().equals(id.toString()))
                .findFirst()
                .map(d -> {
                    // Validar fechas
                    if (d.getFechaFin() != null && d.getFechaInicio() != null
                            && d.getFechaFin().isBefore(d.getFechaInicio())) {
                        throw new FechaInvalidaException(id,
                                "La fecha de fin no puede ser anterior a la fecha de inicio");
                    }
                    // Validar disponibilidad
                    if (!d.isDisponible()) {
                        throw new DisponibleOcupadoException(id, "El recurso ya está ocupado");
                    }
                    return d;
                })
                .orElseThrow(() -> new DisponibleNotFoundException(id, "Disponibilidad no encontrada"));
    }

    public List<DisponibleDTO> listar() {
        return new ArrayList<>(disponibles);
    }

    public DisponibleDTO crearDisponible(DisponibleDTO dto) {
        // Validar fechas al crear
        if (dto.getFechaFin() != null && dto.getFechaInicio() != null
                && dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            Long id = dto.getId() != null ? Long.valueOf(dto.getId()) : -1L;
            throw new FechaInvalidaException(id,
                    "La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // Generar un id si no existe
        if (dto.getId() == null) {
            dto.setId(String.valueOf(disponibles.size() + 1));
        }

        disponibles.add(dto);
        return dto;
    }

    public void eliminarDisponible(Long id) {
        DisponibleDTO dto = disponibles.stream()
                .filter(d -> d.getId().equals(id.toString()))
                .findFirst()
                .orElseThrow(() -> new DisponibleNotFoundException(id, "Disponibilidad no encontrada"));
        disponibles.remove(dto);
    }
}