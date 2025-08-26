package com.eventos.ms_reservas.disponibilidad.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.disponibilidad.model.Disponible;
import com.eventos.ms_reservas.dto.DisponibleDTO;
import com.eventos.ms_reservas.exception.DisponibleNotFoundException;
import com.eventos.ms_reservas.exception.DisponibleOcupadoException;
import com.eventos.ms_reservas.exception.FechaInvalidaException;
import com.eventos.ms_reservas.mapper.DisponibleMapper;

@RestController
@RequestMapping("/v1/disponible")
public class DisponibleController {

    @GetMapping("/{id}")
    public DisponibleDTO obtenerDisponible(@PathVariable String id) {

        // Caso: No existe disponibilidad
        if (id.equals("999")) {
            throw new DisponibleNotFoundException("No se encontró disponibilidad para el id " + id);
        }

        // Caso: Fechas inválidas solo si el id es "fechaInvalida"
        if (id.equals("fechaInvalida")) {
            LocalDateTime inicio = LocalDateTime.now();
            LocalDateTime fin = inicio.minusHours(1);
            if (fin.isBefore(inicio)) {
                throw new FechaInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
            }
        }

        // Caso: Ocupado
        if (id.equals("ocupado")) {
            throw new DisponibleOcupadoException("El evento ya está reservado");
        }

        // Caso normal: devolver objeto válido
        Disponible disponible = new Disponible();
        disponible.setId(id);
        disponible.setDescripcion("Eventos disponibles entre " 
                + LocalDateTime.now() + " a " + LocalDateTime.now().plusDays(1) + ":");
        disponible.setFechaInicio(LocalDateTime.now());
        disponible.setFechaFin(LocalDateTime.now().plusHours(1));
        disponible.setDisponible(true);

        return DisponibleMapper.toDTO(disponible);
    }
}
