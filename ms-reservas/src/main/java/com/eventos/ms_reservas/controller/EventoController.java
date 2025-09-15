package com.eventos.ms_reservas.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import com.eventos.ms_reservas.dto.EventoDTO;
import com.eventos.ms_reservas.exception.EventoNotFoundException;
import com.eventos.ms_reservas.mapper.EventoMapper;
import com.eventos.ms_reservas.model.Evento;
import com.eventos.ms_reservas.service.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Eventos", description = "Operaciones relacionadas con eventos")
@RestController
@RequestMapping("/v1/evento")
public class EventoController {
    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Operation(
        summary = "Obtener un evento por ID",
        description = "Devuelve la información de un evento según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerEvento(
        @Parameter(description = "ID del evento", example = "1")
        @PathVariable String id
    ) {
        Evento evento = eventoService.getById(id);
        if (evento == null) {
            throw new EventoNotFoundException(id, "Evento no encontrado: " + id);
        }
        return ResponseEntity.ok(EventoMapper.toDTO(evento));
    }

    @Operation(summary = "Crear un nuevo evento", description = "Crea un evento y lo almacena en memoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto - El evento ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        Evento evento = EventoMapper.toEntity(eventoDTO);
        eventoService.save(evento);
        return new ResponseEntity<>(EventoMapper.toDTO(evento), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un evento", description = "Actualiza un evento existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> actualizarEvento(@PathVariable String id, @Valid @RequestBody EventoDTO eventoDTO) {
        Evento evento = EventoMapper.toEntity(eventoDTO);
        Evento updated = eventoService.update(id, evento);
        if (updated == null) {
            throw new EventoNotFoundException(id, "Evento no encontrado: " + id);
        }
        return ResponseEntity.ok(EventoMapper.toDTO(updated));
    }

    @Operation(summary = "Eliminar un evento", description = "Elimina un evento por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable String id) {
        boolean deleted = eventoService.delete(id);
        if (!deleted) {
            throw new EventoNotFoundException(id, "Evento no encontrado: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}