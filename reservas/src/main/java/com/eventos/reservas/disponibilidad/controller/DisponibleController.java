package com.eventos.reservas.disponibilidad.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.reservas.disponibilidad.model.Disponible;
import com.eventos.reservas.dto.DisponibleDTO;
import com.eventos.reservas.exception.DisponibleExceptiones.DisponibleNotFoundException;
import com.eventos.reservas.exception.DisponibleExceptiones.DisponibleOcupadoException;
import com.eventos.reservas.exception.DisponibleExceptiones.FechaInvalidaException;
import com.eventos.reservas.mapper.DisponibleMapper;

@RestController
@RequestMapping("/v1/disponible")
public class DisponibleController {

    @GetMapping("/{id}")
    public DisponibleDTO obtenerDisponible(@PathVariable String id) {

        //No existe disponibilidad
        if (id.equals("999")) {
            throw new DisponibleNotFoundException("No se encontró disponibilidad para el id " + id);
        }

        //Fechas inválidas
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fin = inicio.minusHours(1);
        if (fin.isBefore(inicio)) {
            throw new FechaInvalidaException("La fecha de fin no puede ser anterior a la de inicio");
        }

        //Ocupado
        if (id.equals("ocupado")) {
            throw new DisponibleOcupadoException("El evento ya está reservado");
        }

        // Si todo bien, devolver prueba
        Disponible disponible = new Disponible();
        disponible.setId(id);
        disponible.setDescripcion("Eventos disponibles entre"+LocalDateTime.now()+" a "+LocalDateTime.now().plusDays(1)+":");
        /*disponible.setFechaInicio(LocalDateTime.now());
        disponible.setFechaFin(LocalDateTime.now().plusHours(1));
        disponible.setDisponible(true);*/

        return DisponibleMapper.toDTO(disponible);
    }
}

