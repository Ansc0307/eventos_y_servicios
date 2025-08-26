package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.SolicitudNotFoundException;
import com.eventos.ms_reservas.exception.SolicitudPendienteException;
import com.eventos.ms_reservas.mapper.SolicitudMapper;
import com.eventos.ms_reservas.model.Solicitud;

@RestController
@RequestMapping("/v1/solicitud")
public class SolicitudController {

    @GetMapping("/{id}")
    public SolicitudDTO obtenerSolicitud(@PathVariable String id) {

        // No existe
        if (id.equals("999")) {
            throw new SolicitudNotFoundException("No se encontró la solicitud con id " + id);
        }

        //  Aún pendiente - por el momento
        if (id.equals("5")) {
            throw new SolicitudPendienteException("La solicitud " + id + " aún está pendiente de respuesta");
        }

        // Caso 4: Aceptada
        Solicitud solicitud = new Solicitud();
        solicitud.setId(id);
        solicitud.setNombreRecurso("Reserva de salón de eventos");
        solicitud.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitud.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(3));
        solicitud.setEstado("aceptada");

        return SolicitudMapper.toDTO(solicitud);
    }
}
