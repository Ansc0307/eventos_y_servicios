package com.eventos.ms_reservas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.exception.NoDisponibleNotFoundException;
import com.eventos.ms_reservas.service.NoDisponibilidadService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/no-disponibilidades")
@Tag(name = "No Disponibilidad", description = "REST API para registrar periodos de no disponibilidad de ofertas o servicios")
public class NoDisponibilidadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoDisponibilidadController.class);
    private final NoDisponibilidadService service;

    public NoDisponibilidadController(NoDisponibilidadService service) {
        this.service = service;
    }

    // ✅ GET todos los registros
    @GetMapping
    public ResponseEntity<List<NoDisponibilidadDTO>> getAll() {
        LOGGER.info("Listando todas las no disponibilidades");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // ✅ GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<NoDisponibilidadDTO> getNoDisponibilidad(@PathVariable Integer id) {
        LOGGER.info("Buscando no disponibilidad con id: {}", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id,
                        "No se encontró la no disponibilidad con ID: " + id));
    }

    // ✅ GET por ID de oferta
    @GetMapping("/oferta/{idOferta}")
    public ResponseEntity<List<NoDisponibilidadDTO>> getByIdOferta(@PathVariable Integer idOferta) {
        LOGGER.info("Listando no disponibilidades de la oferta: {}", idOferta);
        return ResponseEntity.ok(service.obtenerPorIdOferta(idOferta));
    }

    // ✅ POST
    @PostMapping
    public ResponseEntity<NoDisponibilidadDTO> createNoDisponibilidad(@Valid @RequestBody NoDisponibilidadDTO dto) {
        LOGGER.debug("Creando registro de no disponibilidad: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearNoDisponible(dto));
    }

    // ✅ PUT
    @PutMapping("/{id}")
    public ResponseEntity<NoDisponibilidadDTO> updateNoDisponibilidad(@PathVariable Integer id,
                                                                     @Valid @RequestBody NoDisponibilidadDTO dto) {
        LOGGER.debug("Actualizando no disponibilidad con id {}: {}", id, dto);
        NoDisponibilidadDTO actualizado = service.actualizar(id, dto);
        if (actualizado == null) {
            throw new NoDisponibleNotFoundException(id, "No se encontró la no disponibilidad con ID: " + id);
        }
        return ResponseEntity.ok(actualizado);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoDisponibilidad(@PathVariable Integer id) {
        LOGGER.debug("Eliminando registro de no disponibilidad con id: {}", id);
        service.eliminarNoDisponible(id);
        return ResponseEntity.ok().build();
    }
}
