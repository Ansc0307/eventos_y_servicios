package com.eventos.ms_reservas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.exception.DisponibleNotFoundException;

@Service
public class DisponibleService {

    private final List<DisponibleDTO> disponibles = new ArrayList<>();

    // Crear nueva disponibilidad
    public DisponibleDTO crearDisponible(DisponibleDTO dto) {
        // Asignar un id simple incremental (para ejemplo)
        dto.setId(String.valueOf(disponibles.size() + 1));
        disponibles.add(dto);
        return dto;
    }

    // Obtener disponibilidad por id
    public DisponibleDTO obtenerPorId(int id) {
        return disponibles.stream()
                .filter(d -> d.getId().equals(String.valueOf(id)))
                .findFirst()
                .orElseThrow(() -> new DisponibleNotFoundException("No se encontró disponibilidad con id: " + id));
    }

    // Listar todas las disponibilidades
    public List<DisponibleDTO> listar() {
        return new ArrayList<>(disponibles);
    }

    // Eliminar disponibilidad por id
    public void eliminarDisponible(int id) {
        DisponibleDTO dto = obtenerPorId(id);
        disponibles.remove(dto);
    }
}
