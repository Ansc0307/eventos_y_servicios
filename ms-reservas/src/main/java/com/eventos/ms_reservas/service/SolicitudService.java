package com.eventos.ms_reservas.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.SolicitudNotFoundException;
import com.eventos.ms_reservas.exception.SolicitudPendienteException;

@Service
public class SolicitudService {

    // Simulación de base de datos en memoria
    private final Map<String, SolicitudDTO> solicitudes = new HashMap<>();

    public SolicitudDTO obtenerPorId(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("Id inválido: " + id);
        }

        if (id == 5) {
            throw new SolicitudPendienteException("La solicitud " + id + " aún está pendiente");
        }

        if (id == 999) {
            throw new SolicitudNotFoundException("No existe solicitud con id: " + id);
        }

        // Si no existe, simulamos creación temporal
        return new SolicitudDTO(
                String.valueOf(id),
                "Reserva de salón de eventos",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(3),
                "aceptada"
        );
    }

    public SolicitudDTO crearSolicitud(SolicitudDTO solicitud) {
        // Aquí se podría agregar lógica de validación, ID único, persistencia, etc.
        solicitud.setId(String.valueOf(solicitudes.size() + 1));
        solicitud.setEstado("pendiente");
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    public void eliminarSolicitud(int id) {
        if (!solicitudes.containsKey(String.valueOf(id)) || id == 999) {
            throw new SolicitudNotFoundException("No existe solicitud con id: " + id);
        }
        solicitudes.remove(String.valueOf(id));
    }
}
