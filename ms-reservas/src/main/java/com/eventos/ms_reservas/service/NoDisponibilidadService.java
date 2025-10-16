package com.eventos.ms_reservas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eventos.ms_reservas.dto.NoDisponibilidadDTO;
import com.eventos.ms_reservas.exception.FechaInvalidaException;
import com.eventos.ms_reservas.exception.NoDisponibleNotFoundException;
import com.eventos.ms_reservas.mapper.NoDisponibilidadMapper;
import com.eventos.ms_reservas.model.NoDisponibilidad;
import com.eventos.ms_reservas.repository.NoDisponibilidadRepository;

@Service
public class NoDisponibilidadService {

    private final NoDisponibilidadRepository repository;

    public NoDisponibilidadService(NoDisponibilidadRepository repository) {
        this.repository = repository;
    }

    // ✅ Crear registro de no disponibilidad
    public NoDisponibilidadDTO crearNoDisponible(NoDisponibilidadDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFin());

        NoDisponibilidad entidad = NoDisponibilidadMapper.toEntity(dto);
        NoDisponibilidad guardada = repository.save(entidad);
        return NoDisponibilidadMapper.toDTO(guardada);
    }

    // ✅ Obtener por ID (lanza excepción si no existe)
    public Optional<NoDisponibilidadDTO> obtenerPorId(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .map(NoDisponibilidadMapper::toDTO)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id,
                        "No se encontró la no disponibilidad con ID: " + id)));
    }

    // ✅ Obtener todas
    public List<NoDisponibilidadDTO> obtenerTodas() {
        return repository.findAll().stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Obtener por oferta
    public List<NoDisponibilidadDTO> obtenerPorIdOferta(Integer idOferta) {
        return repository.findByIdOferta(idOferta).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Buscar por motivo (contiene)
    public List<NoDisponibilidadDTO> buscarPorMotivo(String motivo) {
        return repository.findByMotivoContainingIgnoreCase(motivo).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Buscar entre fechas
    public List<NoDisponibilidadDTO> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        validarFechas(inicio, fin);
        return repository.findByFechaInicioBetween(inicio, fin).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Buscar conflictos de fecha
    public List<NoDisponibilidadDTO> buscarConflictosDeFecha(LocalDateTime inicio, LocalDateTime fin) {
        validarFechas(inicio, fin);
        return repository.findConflictosDeFecha(inicio, fin).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Buscar no disponibilidades activas por oferta
    public List<NoDisponibilidadDTO> obtenerActivasPorOferta(Integer idOferta) {
        return repository.findActivasByOferta(idOferta).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Actualizar un registro existente
    public NoDisponibilidadDTO actualizar(Long id, NoDisponibilidadDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFin());

        NoDisponibilidad existente = repository.findById(id)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id,
                        "No se encontró la no disponibilidad con ID: " + id));

        existente.setMotivo(dto.getMotivo());
        existente.setFechaInicio(dto.getFechaInicio());
        existente.setFechaFin(dto.getFechaFin());
        existente.setIdOferta(dto.getIdOferta());
        existente.setIdReserva(dto.getIdReserva());

        NoDisponibilidad actualizado = repository.save(existente);
        return NoDisponibilidadMapper.toDTO(actualizado);
    }

    // ✅ Eliminar registro (lanza excepción si no existe)
    public void eliminarNoDisponible(Long id) {
        if (!repository.existsById(id)) {
            throw new NoDisponibleNotFoundException(id,
                    "No se encontró la no disponibilidad con ID: " + id);
        }
        repository.deleteById(id);
    }

    // ✅ Validación de fechas
    private void validarFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio != null && fin != null && inicio.isAfter(fin)) {
            throw new FechaInvalidaException(null, "La fecha de inicio es posterior a la fecha fin");
        }
    }

    // Métodos legacy para compatibilidad con tests antiguos
public NoDisponibilidad save(NoDisponibilidad nd) {
    return repository.save(nd);
}

public NoDisponibilidad getById(Long id) {
    return repository.findById(id)
            .orElse(null); // o lanzar excepción si prefieres
}

public List<NoDisponibilidad> getAll() {
    return repository.findAll();
}

public NoDisponibilidad update(Long id, NoDisponibilidad update) {
    return repository.findById(id)
            .map(existing -> {
                existing.setMotivo(update.getMotivo());
                existing.setFechaInicio(update.getFechaInicio());
                existing.setFechaFin(update.getFechaFin());
                existing.setIdOferta(update.getIdOferta());
                existing.setIdReserva(update.getIdReserva());
                return repository.save(existing);
            }).orElse(null);
}

public boolean eliminar(Long id) {
    if (repository.existsById(id)) {
        repository.deleteById(id);
        return true;
    }
    return false;
}

}