package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.exception.ReservaNotFoundException;
import com.eventos.ms_reservas.mapper.NoDisponibilidadMapper;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.mapper.SolicitudMapper;
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;

@Tag(name = "Reservas", description = "Operaciones relacionadas con las reservas de eventos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // ============================================================
    // 🔹 GET por ID
    // ============================================================
    @Operation(summary = "Obtener una reserva por ID", description = "Devuelve la información de una reserva específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaDTO> obtenerPorId(
            @Parameter(description = "ID de la reserva", example = "1") @PathVariable Integer id) {
        Reserva reserva = reservaService.getById(id);
        if (reserva == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);

        return ResponseEntity.ok(ReservaMapper.toDTO(reserva));
    }

    // ============================================================
    // 🔹 POST Crear Reserva
    // ============================================================
    @Operation(summary = "Crear una nueva reserva", description = "Crea y almacena una nueva reserva en la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de horario"),
            @ApiResponse(responseCode = "404", description = "La solicitud asociada no existe")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR') or hasAuthority('SCOPE_reservas.write')")
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        Reserva saved = reservaService.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaMapper.toDTO(saved));
    }

    // ============================================================
    // 🔹 PUT Actualizar Reserva
    // ============================================================
    @Operation(summary = "Actualizar una reserva existente", description = "Actualiza los datos de una reserva existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR') or hasAuthority('SCOPE_reservas.write')")
    public ResponseEntity<ReservaDTO> actualizarReserva(
            @PathVariable Integer id,
            @Valid @RequestBody ReservaDTO reservaDTO) {

        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        Reserva updated = reservaService.update(id, reserva);

        if (updated == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);

        return ResponseEntity.ok(ReservaMapper.toDTO(updated));
    }

    // ============================================================
    // 🔹 DELETE Eliminar Reserva
    // ============================================================
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        boolean deleted = reservaService.delete(id);
        if (!deleted)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // 🔹 GET Todas las reservas
    // ============================================================
    @Operation(summary = "Listar todas las reservas", description = "Devuelve la lista completa de reservas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> listarTodas() {
        List<ReservaDTO> lista = reservaService.getAll().stream()
                .map(ReservaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ============================================================
    // 🔹 GET Reservas por estado
    // ============================================================
    @Operation(summary = "Buscar reservas por estado", description = "Devuelve todas las reservas con un estado específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas por estado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/estado/{estado}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> listarPorEstado(@PathVariable String estado) {
        List<ReservaDTO> lista = reservaService.getByEstado(estado).stream()
                .map(ReservaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ============================================================
    // 🔹 GET Reservas por ID de solicitud
    // ============================================================
    @Operation(summary = "Buscar reservas por ID de solicitud", description = "Obtiene las reservas asociadas a una solicitud específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas por solicitud"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/solicitud/{idSolicitud}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> listarPorSolicitud(@PathVariable Integer idSolicitud) {
        List<ReservaDTO> lista = reservaService.getByIdSolicitud(idSolicitud).stream()
                .map(ReservaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ============================================================
    // 🔹 GET Reservas en rango
    // ============================================================
    @Operation(summary = "Buscar reservas por rango de fechas", description = "Obtiene las reservas dentro de un rango de tiempo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas en rango"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/rango")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> listarPorRango(
            @RequestParam("inicio") String inicioStr,
            @RequestParam("fin") String finStr) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(inicioStr);
            LocalDateTime fin = LocalDateTime.parse(finStr);
            List<ReservaDTO> lista = reservaService.getReservasEnRango(inicio, fin).stream()
                    .map(ReservaMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(lista);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    // ============================================================
    // 🔹 GET Reservas conflictivas (JPQL)
    // ============================================================
    @Operation(summary = "Buscar reservas conflictivas", description = "Devuelve las reservas que se solapan con un rango dado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas conflictivas"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/conflictivas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> buscarConflictivas(
            @RequestParam("inicio") String inicioStr,
            @RequestParam("fin") String finStr) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(inicioStr);
            LocalDateTime fin = LocalDateTime.parse(finStr);
            List<ReservaDTO> lista = reservaService.getReservasConflictivas(inicio, fin).stream()
                    .map(ReservaMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(lista);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    // ============================================================
    // 🔹 GET Reservas conflictivas (Native)
    // ============================================================
    @Operation(summary = "Buscar reservas conflictivas (consulta nativa)", description = "Devuelve las reservas conflictivas usando SQL nativo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de reservas conflictivas (nativa)"),
        @ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/conflictivas/native")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReservaDTO>> buscarConflictivasNative(
            @RequestParam("inicio") String inicioStr,
            @RequestParam("fin") String finStr) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(inicioStr);
            LocalDateTime fin = LocalDateTime.parse(finStr);
            List<ReservaDTO> lista = reservaService.getReservasConflictivasNative(inicio, fin).stream()
                    .map(ReservaMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(lista);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use ISO_LOCAL_DATE_TIME, por ejemplo: 2025-10-17T14:30:00");
        }
    }

    // ============================================================
    // 🔹 GET No Disponibilidad asociada
    // ============================================================
    @Operation(summary = "Obtener no disponibilidad asociada a una reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "No disponibilidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada o sin no disponibilidad")
    })
    @GetMapping("/{id}/no-disponibilidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NoDisponibilidadDTO> obtenerNoDisponibilidad(@PathVariable Integer id) {
        var noDisp = reservaService.getNoDisponibilidadByReserva(id);
        if (noDisp == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada o sin no disponibilidad asociada");
        return ResponseEntity.ok(NoDisponibilidadMapper.toDTO(noDisp));
    }

    @GetMapping("/{id}/has-no-disponibilidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> tieneNoDisponibilidad(@PathVariable Integer id) {
        if (reservaService.getById(id) == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        return ResponseEntity.ok(reservaService.hasNoDisponibilidad(id));
    }

    // ============================================================
    // 🔹 GET Solicitud asociada
    // ============================================================
    @Operation(summary = "Obtener solicitud asociada a una reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada o sin solicitud asociada")
    })
    @GetMapping("/{id}/solicitud")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SolicitudDTO> obtenerSolicitud(@PathVariable Integer id) {
        var solicitud = reservaService.getSolicitudByReserva(id);
        if (solicitud == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada o sin solicitud asociada");
        return ResponseEntity.ok(SolicitudMapper.toDTO(solicitud));
    }

    @GetMapping("/{id}/has-solicitud")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> tieneSolicitud(@PathVariable Integer id) {
        if (reservaService.getById(id) == null)
            throw new ReservaNotFoundException(String.valueOf(id), "Reserva no encontrada: " + id);
        return ResponseEntity.ok(reservaService.hasSolicitud(id));
    }
}
