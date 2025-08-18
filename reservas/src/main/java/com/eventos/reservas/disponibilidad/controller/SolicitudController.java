package com.eventos.reservas.disponibilidad.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.reservas.disponibilidad.model.Solicitud;
import com.eventos.reservas.dto.SolicitudDTO;
import com.eventos.reservas.mapper.SolicitudMapper;

@RestController
@RequestMapping("/v1/solicitud")
public class SolicitudController {

    @GetMapping("/{id}")
    public SolicitudDTO obtenerSolicitud(@PathVariable String id) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(id);
        solicitud.setNombreRecurso("Solicitud de prueba");
        solicitud.setFechaInicio(LocalDateTime.now());
        solicitud.setFechaFin(LocalDateTime.now().plusHours(1));
        solicitud.setEstado("pendiente");

        return SolicitudMapper.toDTO(solicitud);
    }
}
