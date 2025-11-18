package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/solicitudes")
@Tag(name = "Solicitud", description = "REST API para solicitudes de reserva")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // --- Crear solicitud ---
    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud de reserva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Debe enviar Bearer token"),
        @ApiResponse(responseCode = "403", description = "Prohibido. Solo ORGANIZADOR o ADMIN pueden crear solicitudes")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SolicitudDTO> crear(@Valid @RequestBody SolicitudDTO dto) {
        SolicitudDTO creada = solicitudService.crearSolicitud(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // --- Obtener solicitud por ID ---
    @Operation(summary = "Obtener solicitud", description = "Obtiene una solicitud por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SolicitudDTO> obtenerUno(
        @Parameter(description = "ID de la solicitud", example = "1", required = true)
        @PathVariable Integer id
    ) {
        SolicitudDTO dto = solicitudService.obtenerPorId(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        return ResponseEntity.ok(dto);
    }

    // --- Listar todas ---

    /* 
    @Operation(summary = "Listar solicitudes", description = "Lista todas las solicitudes de reserva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> listar() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }
    */

    // --- Buscar por estado ---
    @Operation(summary = "Obtener solicitudes por estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/estado/{estado}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorEstado(
        @Parameter(description = "Estado de la solicitud", example = "pendiente") 
        @PathVariable String estado
    ) {
        return ResponseEntity.ok(solicitudService.obtenerPorEstado(estado));
    }

    // --- Buscar por organizador ---
    @Operation(summary = "Obtener solicitudes por organizador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/organizador/{idOrganizador}")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZADOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorOrganizador(
        @Parameter(description = "ID del organizador", example = "101") 
        @PathVariable Integer idOrganizador
    ) {
        return ResponseEntity.ok(solicitudService.obtenerPorOrganizador(idOrganizador));
    }

    // --- Buscar por proveedor ---
    @Operation(summary = "Obtener solicitudes por proveedor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/proveedor/{idProveedor}")
    @PreAuthorize("hasAnyRole('ADMIN','PROVEEDOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorProveedor(
        @Parameter(description = "ID del proveedor", example = "202") 
        @PathVariable Integer idProveedor
    ) {
        return ResponseEntity.ok(solicitudService.obtenerPorProveedor(idProveedor));
    }

    // --- Buscar por rango de fechas ---
    @Operation(summary = "Obtener solicitudes entre fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/rango-fechas")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorRangoFechas(
        @Parameter(description = "Fecha de inicio", example = "2025-10-01T00:00:00")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @Parameter(description = "Fecha de fin", example = "2025-10-31T23:59:59")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(solicitudService.obtenerPorRangoFechas(inicio, fin));
    }

    // --- Buscar por estado y rango de fechas ---
    @Operation(summary = "Obtener solicitudes por estado y rango de fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/estado-fechas")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> obtenerPorEstadoYRango(
        @RequestParam String estado,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(solicitudService.obtenerPorEstadoYRangoFechas(estado, inicio, fin));
    }

    // --- Buscar por ID de oferta ---
    @Operation(summary = "Obtener solicitud por ID de oferta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "No encontrada")
    })
    @GetMapping("/oferta/{idOferta}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SolicitudDTO> obtenerPorOferta(
        @Parameter(description = "ID de la oferta asociada", example = "303")
        @PathVariable Integer idOferta
    ) {
        SolicitudDTO dto = solicitudService.obtenerPorIdOferta(idOferta)
            .orElseThrow(() -> new RuntimeException("No se encontró solicitud con idOferta: " + idOferta));
        return ResponseEntity.ok(dto);
    }

    // --- Verificar existencia por organizador y proveedor ---
    @Operation(summary = "Verificar si existe una solicitud entre organizador y proveedor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    @GetMapping("/existe")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Boolean> existePorOrganizadorYProveedor(
        @RequestParam Integer idOrganizador,
        @RequestParam Integer idProveedor
    ) {
        boolean existe = solicitudService.existePorOrganizadorYProveedor(idOrganizador, idProveedor);
        return ResponseEntity.ok(existe);
    }

    // --- Obtener reserva asociada ---
    @Operation(summary = "Obtener reserva de una solicitud", description = "Obtiene la reserva asociada a una solicitud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró reserva asociada")
    })
    @GetMapping("/{id}/reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservaDTO> obtenerReservaDeSolicitud(
        @Parameter(description = "ID de la solicitud", example = "1")
        @PathVariable Integer id
    ) {
        Reserva reserva = solicitudService.getReservaBySolicitud(id);
        if (reserva == null) {
            throw new RuntimeException("Solicitud sin reserva asociada: " + id);
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(reserva));
    }

    // --- Listar solicitudes con/sin reserva ---
    @Operation(summary = "Listar solicitudes con reserva asociada")
    @GetMapping("/con-reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> listarConReserva() {
        return ResponseEntity.ok(solicitudService.obtenerConReserva());
    }

    @Operation(summary = "Listar solicitudes sin reserva asociada")
    @GetMapping("/sin-reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitudDTO>> listarSinReserva() {
        return ResponseEntity.ok(solicitudService.obtenerSinReserva());
    }

    // --- Eliminar solicitud ---
    @Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Solicitud eliminada"),
        @ApiResponse(responseCode = "403", description = "Solo ADMIN puede eliminar solicitudes")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la solicitud", example = "1") 
        @PathVariable Integer id
    ) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
