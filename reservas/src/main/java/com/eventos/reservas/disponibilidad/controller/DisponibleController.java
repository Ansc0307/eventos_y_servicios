package com.eventos.reservas.disponibilidad.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.reservas.disponibilidad.model.Disponible;
import com.eventos.reservas.dto.DisponibleDTO;
import com.eventos.reservas.mapper.DisponibleMapper;

@RestController
@RequestMapping("/v1/disponible")
public class DisponibleController {

    @GetMapping("/{id}")
public DisponibleDTO obtenerDisponible(@PathVariable String id) {
    Disponible disponible = new Disponible();
    disponible.setId(id);
    disponible.setDescripcion("Disponible de prueba");
    disponible.setFechaInicio(LocalDateTime.now());
    disponible.setFechaFin(LocalDateTime.now().plusHours(1));
    disponible.setDisponible(true);

    return DisponibleMapper.toDTO(disponible);
}

}
