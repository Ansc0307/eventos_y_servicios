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

    private final List<DisponibleDTO> disponibles = new ArrayList<>();

    public DisponibleDTO obtenerPorId(Long id) {
        return disponibles.stream()
                .filter(d -> d.getId().equals(id.toString()))
                .findFirst()
                .map(d -> {
                    // Validar fechas
                    if (d.getFechaFin() != null && d.getFechaInicio() != null
                            && d.getFechaFin().isBefore(d.getFechaInicio())) {
                        throw new FechaInvalidaException(id, "La fecha de fin no puede ser anterior a la fecha de inicio");
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
        // Validar fechas
        if (dto.getFechaFin() != null && dto.getFechaInicio() != null
                && dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            Long id = dto.getId() != null ? Long.valueOf(dto.getId()) : -1L;
            throw new FechaInvalidaException(id, "La fecha de fin no puede ser anterior a la fecha de inicio");
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
