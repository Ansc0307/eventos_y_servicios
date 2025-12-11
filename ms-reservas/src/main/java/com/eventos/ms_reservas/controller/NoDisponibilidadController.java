package com.eventos.ms_reservas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.dto.ReservaDTO;
import com.eventos.ms_reservas.exception.NoDisponibleNotFoundException;
import com.eventos.ms_reservas.mapper.ReservaMapper;
import com.eventos.ms_reservas.service.NoDisponibilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/no-disponibilidades")
@Tag(name = "No Disponibilidad", description = "API REST para gestionar los periodos de no disponibilidad de las ofertas o servicios")
public class NoDisponibilidadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoDisponibilidadController.class);

    @Autowired
    private NoDisponibilidadService service;

    // -------------------------------------------------------------
    // 🔹 CREAR
    // -------------------------------------------------------------
    @Operation(summary = "Registrar no disponibilidad", description = "Crea un nuevo registro de no disponibilidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "No disponibilidad registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
        @ApiResponse(responseCode = "401", description = "No autenticado. Requiere token Bearer"),
        @ApiResponse(responseCode = "403", description = "Prohibido. Solo ORGANIZADOR o ADMIN pueden crear registros")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROVEEDOR','ORGANIZADOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NoDisponibilidadDTO> crear(
            @Valid @RequestBody NoDisponibilidadDTO dto) {
        LOGGER.info("Creando no disponibilidad: {}", dto);
        NoDisponibilidadDTO creado = service.crearNoDisponible(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // -------------------------------------------------------------
    // 🔹 OBTENER UNO
    // -------------------------------------------------------------
    @Operation(summary = "Obtener no disponibilidad", description = "Obtiene una no disponibilidad específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "No disponibilidad encontrada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NoDisponibilidadDTO> obtenerUno(
            @Parameter(description = "ID de la no disponibilidad", example = "1", required = true)
            @PathVariable Integer id) {
        LOGGER.info("Buscando no disponibilidad con id: {}", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id, "No disponibilidad no encontrada"));
    }

    // -------------------------------------------------------------
    // 🔹 LISTAR TODAS
    // -------------------------------------------------------------
    @Operation(summary = "Listar no disponibilidades", description = "Obtiene todas las no disponibilidades registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> listar() {
        LOGGER.info("Listando todas las no disponibilidades");
        List<NoDisponibilidadDTO> lista = service.obtenerTodas();
        if (lista.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Info", "No existen registros de no disponibilidad");
            return new ResponseEntity<>(lista, headers, HttpStatus.OK);
        }
        return ResponseEntity.ok(lista);
    }

    // -------------------------------------------------------------
    // 🔹 LISTAR POR OFERTA
    // -------------------------------------------------------------
    @Operation(summary = "Listar por oferta", description = "Obtiene las no disponibilidades de una oferta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/oferta/{idOferta}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> listarPorOferta(
            @Parameter(description = "ID de la oferta", example = "101", required = true)
            @PathVariable Integer idOferta) {
        LOGGER.info("Listando no disponibilidades para oferta: {}", idOferta);
        return ResponseEntity.ok(service.obtenerPorIdOferta(idOferta));
    }

    // -------------------------------------------------------------
    // 🔹 ACTUALIZAR
    // -------------------------------------------------------------
    @Operation(summary = "Actualizar no disponibilidad", description = "Modifica una no disponibilidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "No disponibilidad actualizada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROVEEDOR','ORGANIZADOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NoDisponibilidadDTO> actualizar(
            @Parameter(description = "ID de la no disponibilidad", example = "1") @PathVariable Integer id,
            @Valid @RequestBody NoDisponibilidadDTO dto) {
        LOGGER.info("Actualizando no disponibilidad con id {}: {}", id, dto);
        NoDisponibilidadDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // -------------------------------------------------------------
    // 🔹 ELIMINAR
    // -------------------------------------------------------------
    @Operation(summary = "Eliminar no disponibilidad", description = "Elimina un registro de no disponibilidad por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROVEEDOR','ORGANIZADOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la no disponibilidad", example = "1", required = true)
            @PathVariable Integer id) {
        LOGGER.info("Eliminando no disponibilidad con id: {}", id);
        service.eliminarNoDisponible(id);
        return ResponseEntity.ok().build();
    }

    // -------------------------------------------------------------
    // 🔹 RELACIONES CON RESERVAS
    // -------------------------------------------------------------
    @Operation(summary = "Obtener reserva asociada", description = "Obtiene la reserva asociada a una no disponibilidad específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "No disponibilidad sin reserva o inexistente")
    })
    @GetMapping("/{id}/reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservaDTO> obtenerReservaAsociada(@PathVariable Integer id) {
        LOGGER.info("Buscando reserva asociada a la no disponibilidad {}", id);
        var reserva = service.getReservaByNoDisponibilidad(id);
        if (reserva == null) {
            throw new NoDisponibleNotFoundException(id, "No disponibilidad sin reserva asociada");
        }
        return ResponseEntity.ok(ReservaMapper.toDTO(reserva));
    }

    // -------------------------------------------------------------
    // 🔹 VERIFICAR SI TIENE RESERVA
    // -------------------------------------------------------------
    @Operation(summary = "Verificar si tiene reserva", description = "Indica si una no disponibilidad tiene una reserva asociada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operación exitosa"),
        @ApiResponse(responseCode = "404", description = "No disponibilidad no encontrada")
    })
    @GetMapping("/{id}/has-reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Boolean> tieneReserva(@PathVariable Integer id) {
        LOGGER.info("Verificando si la no disponibilidad {} tiene reserva", id);
        boolean hasReserva = service.hasReserva(id);
        return ResponseEntity.ok(hasReserva);
    }

    // -------------------------------------------------------------
    // 🔹 LISTAR POR RESERVA
    // -------------------------------------------------------------
    @Operation(summary = "Listar por reserva", description = "Obtiene todas las no disponibilidades asociadas a una reserva")
    @GetMapping("/reserva/{idReserva}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> listarPorReserva(
            @Parameter(description = "ID de la reserva", example = "10", required = true)
            @PathVariable Integer idReserva) {
        LOGGER.info("Listando no disponibilidades para reserva: {}", idReserva);
        return ResponseEntity.ok(service.obtenerPorIdReserva(idReserva));
    }

    // -------------------------------------------------------------
    // 🔹 LISTAR CON / SIN RESERVA
    // -------------------------------------------------------------
    @Operation(summary = "Listar con reserva", description = "Obtiene todas las no disponibilidades que tienen reserva asociada")
    @GetMapping("/con-reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> listarConReserva() {
        LOGGER.info("Listando no disponibilidades con reserva asociada");
        return ResponseEntity.ok(service.obtenerConReserva());
    }

    @Operation(summary = "Listar sin reserva", description = "Obtiene todas las no disponibilidades sin reserva asociada")
    @GetMapping("/sin-reserva")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> listarSinReserva() {
        LOGGER.info("Listando no disponibilidades sin reserva");
        return ResponseEntity.ok(service.obtenerSinReserva());
    }

    // -------------------------------------------------------------
    // 🔹 BUSCAR POR MOTIVO (Native Query)
    // -------------------------------------------------------------
    @Operation(summary = "Buscar por motivo", description = "Busca no disponibilidades cuyo motivo contiene una palabra clave (consulta nativa)")
    @GetMapping("/buscar/motivo-native/{motivo}")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<NoDisponibilidadDTO>> buscarPorMotivoNative(
            @Parameter(description = "Motivo o palabra clave", example = "mantenimiento") @PathVariable String motivo) {
        LOGGER.info("Buscando no disponibilidades por motivo: {}", motivo);
        return ResponseEntity.ok(service.buscarPorMotivoNative(motivo));
    }
}
