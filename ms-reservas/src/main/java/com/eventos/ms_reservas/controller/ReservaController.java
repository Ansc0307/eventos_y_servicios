package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.exception.ReservaNotFoundException;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reservas", description = "Operaciones relacionadas con reservas")
@RestController
@RequestMapping("/v1/reserva")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(
        summary = "Obtener una reserva por ID",
        description = "Devuelve la información de una reserva según su ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada o ID inválido")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReserva(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable String id
    ) {
        Reserva reserva = reservaService.getById(id);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(reserva));
    }

    @Operation(summary = "Crear una nueva reserva", description = "Crea una reserva y la almacena en memoria")
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        reservaService.save(reserva);
        return new ResponseEntity<>(ReservaMapper.toDTO(reserva), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una reserva", description = "Actualiza una reserva existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(@PathVariable String id, @RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        Reserva updated = reservaService.update(id, reserva);
        if (updated == null) {
            throw new ReservaNotFoundException("Reserva no encontrada: " + id);
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(updated));
    }

    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable String id) {
        boolean deleted = reservaService.delete(id);
        if (!deleted) {
            throw new ReservaNotFoundException("Reserva no encontrada: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}