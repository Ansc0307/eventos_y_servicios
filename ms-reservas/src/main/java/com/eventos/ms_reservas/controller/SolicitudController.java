package com.eventos.ms_reservas.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/solicitudes")
@Tag(name = "Solicitudes", description = "REST API para solicitudes de reservas")
public class SolicitudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudController.class);
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    // --- CRUD BÁSICO ---

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID")
    public SolicitudDTO getSolicitud(@PathVariable Integer id) {
        LOGGER.info("Obteniendo solicitud con id: {}", id);
        return solicitudService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva solicitud")
    public SolicitudDTO createSolicitud(@Valid @RequestBody SolicitudDTO body) {
        LOGGER.debug("Creando solicitud: {}", body);
        return solicitudService.crearSolicitud(body);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar solicitud por ID")
    public void deleteSolicitud(@PathVariable Integer id) {
        LOGGER.debug("Eliminando solicitud con id: {}", id);
        solicitudService.eliminarSolicitud(id);
    }

    // --- NUEVOS ENDPOINTS ---

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener solicitudes por estado")
    public List<SolicitudDTO> getByEstado(@PathVariable String estado) {
        LOGGER.info("Buscando solicitudes con estado: {}", estado);
        return solicitudService.obtenerPorEstado(estado);
    }

    @GetMapping("/organizador/{idOrganizador}")
    @Operation(summary = "Obtener solicitudes por organizador")
    public List<SolicitudDTO> getByOrganizador(@PathVariable Integer idOrganizador) {
        LOGGER.info("Buscando solicitudes del organizador: {}", idOrganizador);
        return solicitudService.obtenerPorOrganizador(idOrganizador);
    }

    @GetMapping("/proveedor/{idProveedor}")
    @Operation(summary = "Obtener solicitudes por proveedor")
    public List<SolicitudDTO> getByProveedor(@PathVariable Integer idProveedor) {
        LOGGER.info("Buscando solicitudes del proveedor: {}", idProveedor);
        return solicitudService.obtenerPorProveedor(idProveedor);
    }

    @GetMapping("/rango-fechas")
    @Operation(summary = "Obtener solicitudes entre fechas")
    public List<SolicitudDTO> getByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        LOGGER.info("Buscando solicitudes entre {} y {}", inicio, fin);
        return solicitudService.obtenerPorRangoFechas(inicio, fin);
    }

    @GetMapping("/estado-fechas")
    @Operation(summary = "Obtener solicitudes por estado y rango de fechas")
    public List<SolicitudDTO> getByEstadoYRangoFechas(
            @RequestParam String estado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        LOGGER.info("Buscando solicitudes con estado {} entre {} y {}", estado, inicio, fin);
        return solicitudService.obtenerPorEstadoYRangoFechas(estado, inicio, fin);
    }

    @GetMapping("/oferta/{idOferta}")
    @Operation(summary = "Obtener solicitud por ID de oferta")
    public SolicitudDTO getByIdOferta(@PathVariable Integer idOferta) {
        LOGGER.info("Buscando solicitud por idOferta: {}", idOferta);
        return solicitudService.obtenerPorIdOferta(idOferta)
                .orElseThrow(() -> new RuntimeException("No se encontró solicitud con idOferta: " + idOferta));
    }

    @GetMapping("/existe")
    @Operation(summary = "Verificar si existe una solicitud por organizador y proveedor")
    public boolean existePorOrganizadorYProveedor(
            @RequestParam Integer idOrganizador,
            @RequestParam Integer idProveedor) {
        LOGGER.info("Verificando existencia de solicitud entre organizador {} y proveedor {}", idOrganizador, idProveedor);
        return solicitudService.existePorOrganizadorYProveedor(idOrganizador, idProveedor);
    }

    // ✅ Endpoints para navegación de relaciones JPA

    @Operation(summary = "Obtener reserva de una solicitud", description = "Obtiene la reserva asociada a una solicitud específica")
    @GetMapping("/{id}/reserva")
    public ResponseEntity<com.eventos.ms_reservas.dto.ReservaDTO> obtenerReservaDeSolicitud(@PathVariable Integer id) {
        LOGGER.info("Obteniendo reserva de solicitud con id: {}", id);
        com.eventos.ms_reservas.model.Reserva reserva = solicitudService.getReservaBySolicitud(id);
        if (reserva == null) {
            throw new RuntimeException("Solicitud no encontrada o sin reserva asociada: " + id);
        }
        return ResponseEntity.ok(com.eventos.ms_reservas.mapper.ReservaMapper.toDTO(reserva));
    }

    @Operation(summary = "Verificar si solicitud tiene reserva", description = "Verifica si una solicitud tiene una reserva asociada")
    @GetMapping("/{id}/has-reserva")
    public ResponseEntity<Boolean> verificarReserva(@PathVariable Integer id) {
        LOGGER.info("Verificando si solicitud {} tiene reserva asociada", id);
        // Verificar que la solicitud existe
        if (solicitudService.obtenerPorId(id).isEmpty()) {
            throw new RuntimeException("Solicitud no encontrada: " + id);
        }
        boolean hasReserva = solicitudService.hasReserva(id);
        return ResponseEntity.ok(hasReserva);
    }

    @Operation(summary = "Listar solicitudes con reserva", description = "Obtiene todas las solicitudes que tienen reserva asociada")
    @GetMapping("/con-reserva")
    public ResponseEntity<List<SolicitudDTO>> listarConReserva() {
        LOGGER.info("Listando solicitudes con reserva asociada");
        return ResponseEntity.ok(solicitudService.obtenerConReserva());
    }

    @Operation(summary = "Listar solicitudes sin reserva", description = "Obtiene todas las solicitudes que NO tienen reserva asociada")
    @GetMapping("/sin-reserva")
    public ResponseEntity<List<SolicitudDTO>> listarSinReserva() {
        LOGGER.info("Listando solicitudes sin reserva asociada");
        return ResponseEntity.ok(solicitudService.obtenerSinReserva());
    }
}
