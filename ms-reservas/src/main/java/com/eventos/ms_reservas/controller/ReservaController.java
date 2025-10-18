package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.model.Reserva;
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

    @Operation(summary = "Buscar reservas conflictivas", description = "Devuelve reservas que se solapan con el rango proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservas conflictivas"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @GetMapping("/conflictivas")
    public ResponseEntity<List<ReservaDTO>> buscarConflictivas(
        @RequestParam("inicio") String inicioStr,
        @RequestParam("fin") String finStr
    ) {
        try {
            java.time.LocalDateTime inicio = java.time.LocalDateTime.parse(inicioStr);
            java.time.LocalDateTime fin = java.time.LocalDateTime.parse(finStr);
            List<Reserva> conflictivas = reservaService.getReservasEnRango(inicio, fin);
            List<ReservaDTO> dtoList = conflictivas.stream().map(ReservaMapper::toDTO).toList();
            return ResponseEntity.ok(dtoList);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    @Operation(summary = "Listar todas las reservas", description = "Devuelve todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<Reserva> all = reservaService.getAll();
        List<ReservaDTO> dtoList = all.stream().map(ReservaMapper::toDTO).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> listarPorEstado(@PathVariable String estado) {
        List<Reserva> list = reservaService.getByEstado(estado);
        return ResponseEntity.ok(list.stream().map(ReservaMapper::toDTO).toList());
    }

    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<List<ReservaDTO>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        List<Reserva> list = reservaService.getByIdSolicitud(idSolicitud);
        return ResponseEntity.ok(list.stream().map(ReservaMapper::toDTO).toList());
    }

    @GetMapping("/rango")
    public ResponseEntity<List<ReservaDTO>> listarPorRango(@RequestParam("inicio") String inicioStr, @RequestParam("fin") String finStr) {
        try {
            java.time.LocalDateTime inicio = java.time.LocalDateTime.parse(inicioStr);
            java.time.LocalDateTime fin = java.time.LocalDateTime.parse(finStr);
            List<Reserva> list = reservaService.getReservasEnRango(inicio, fin);
            return ResponseEntity.ok(list.stream().map(ReservaMapper::toDTO).toList());
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    @GetMapping("/conflictivas/native")
    public ResponseEntity<List<ReservaDTO>> buscarConflictivasNative(@RequestParam("inicio") String inicioStr, @RequestParam("fin") String finStr) {
        try {
            java.time.LocalDateTime inicio = java.time.LocalDateTime.parse(inicioStr);
            java.time.LocalDateTime fin = java.time.LocalDateTime.parse(finStr);
            List<Reserva> list = reservaService.getReservasConflictivasNative(inicio, fin);
            return ResponseEntity.ok(list.stream().map(ReservaMapper::toDTO).toList());
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    @Operation(summary = "Obtener no disponibilidad de una reserva", description = "Obtiene la no disponibilidad asociada a una reserva específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "No disponibilidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada o sin no disponibilidad asociada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/no-disponibilidad")
    public ResponseEntity<com.eventos.ms_reservas.dto.NoDisponibilidadDTO> obtenerNoDisponibilidadDeReserva(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable Integer id
    ) {
        com.eventos.ms_reservas.model.NoDisponibilidad noDisp = reservaService.getNoDisponibilidadByReserva(id);
        if (noDisp == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada o sin no disponibilidad asociada: " + id);
        }
        return ResponseEntity.ok(com.eventos.ms_reservas.mapper.NoDisponibilidadMapper.toDTO(noDisp));
    }

    @Operation(summary = "Verificar si reserva tiene no disponibilidad", description = "Verifica si una reserva tiene una no disponibilidad asociada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/has-no-disponibilidad")
    public ResponseEntity<Boolean> verificarNoDisponibilidad(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable Integer id
    ) {
        // Verificar que la reserva existe
        if (reservaService.getById(id) == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        }
        boolean hasNoDisp = reservaService.hasNoDisponibilidad(id);
        return ResponseEntity.ok(hasNoDisp);
    }

    @Operation(summary = "Obtener solicitud de una reserva", description = "Obtiene la solicitud asociada a una reserva específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada o sin solicitud asociada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/solicitud")
    public ResponseEntity<com.eventos.ms_reservas.dto.SolicitudDTO> obtenerSolicitudDeReserva(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable Integer id
    ) {
        com.eventos.ms_reservas.model.Solicitud solicitud = reservaService.getSolicitudByReserva(id);
        if (solicitud == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada o sin solicitud asociada: " + id);
        }
        return ResponseEntity.ok(com.eventos.ms_reservas.mapper.SolicitudMapper.toDTO(solicitud));
    }

    @Operation(summary = "Verificar si reserva tiene solicitud", description = "Verifica si una reserva tiene una solicitud asociada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/has-solicitud")
    public ResponseEntity<Boolean> verificarSolicitud(
        @Parameter(description = "ID de la reserva", example = "1")
        @PathVariable Integer id
    ) {
        // Verificar que la reserva existe
        if (reservaService.getById(id) == null) {
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        }
        boolean hasSolicitud = reservaService.hasSolicitud(id);
        return ResponseEntity.ok(hasSolicitud);
    }
}