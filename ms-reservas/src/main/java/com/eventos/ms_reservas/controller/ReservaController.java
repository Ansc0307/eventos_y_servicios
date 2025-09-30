package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.exception.ReservaNotFoundException;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        description = "Devuelve la información de una reserva según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReserva(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable Integer id
    ) {
        Reserva reserva = reservaService.getById(id);
        if (reserva == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(reserva));
    }

    @Operation(summary = "Crear una nueva reserva", description = "Crea una reserva y la almacena en memoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - La reserva ya existe o hay conflicto de horarios"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        reservaService.save(reserva);
        return new ResponseEntity<>(ReservaMapper.toDTO(reserva), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una reserva", description = "Actualiza una reserva existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - Conflicto de horarios en la actualización"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(@PathVariable Integer id, @Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        Reserva updated = reservaService.update(id, reserva);
        if (updated == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(updated));
    }

    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        boolean deleted = reservaService.delete(id);
        if (!deleted) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}