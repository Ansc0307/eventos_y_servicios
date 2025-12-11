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
import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.repository.NoDisponibilidadRepository;

@Service
public class NoDisponibilidadService {

    private final NoDisponibilidadRepository repository;

    public NoDisponibilidadService(NoDisponibilidadRepository repository) {
        this.repository = repository;
    }

    // ✅ Crear registro
    public NoDisponibilidadDTO crearNoDisponible(NoDisponibilidadDTO dto) {
        validarFechas(dto.getFechaInicio(), dto.getFechaFin());
        NoDisponibilidad entidad = NoDisponibilidadMapper.toEntity(dto);
        NoDisponibilidad guardada = repository.save(entidad);
        return NoDisponibilidadMapper.toDTO(guardada);
    }

    // ✅ Obtener por ID
    public Optional<NoDisponibilidadDTO> obtenerPorId(Integer id) {
        return repository.findById(id)
                .map(NoDisponibilidadMapper::toDTO);
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

    // ✅ Buscar por motivo
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

    // ✅ Buscar conflictos
    public List<NoDisponibilidadDTO> buscarConflictosDeFecha(LocalDateTime inicio, LocalDateTime fin) {
        validarFechas(inicio, fin);
        return repository.findConflictosDeFecha(inicio, fin).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Activas por oferta
    public List<NoDisponibilidadDTO> obtenerActivasPorOferta(Integer idOferta) {
        return repository.findActivasByOferta(idOferta).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Actualizar
    public NoDisponibilidadDTO actualizar(Integer id, NoDisponibilidadDTO dto) {
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

    // ✅ Eliminar (compatible con tests)
    public void eliminarNoDisponible(Integer id) {
        NoDisponibilidad existente = repository.findById(id)
                .orElseThrow(() -> new NoDisponibleNotFoundException(id,
                        "No se encontró la no disponibilidad con ID: " + id));
        repository.delete(existente);
    }

    // ✅ Validar fechas
    private void validarFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio != null && fin != null && inicio.isAfter(fin)) {
            throw new FechaInvalidaException(null, "La fecha de inicio es posterior a la fecha fin");
        }
    }

    // 🔁 Métodos legacy para compatibilidad con pruebas antiguas
    public NoDisponibilidad save(NoDisponibilidad nd) {
        return repository.save(nd);
    }

    public NoDisponibilidad getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public List<NoDisponibilidad> getAll() {
        return repository.findAll();
    }

    public NoDisponibilidad update(Integer id, NoDisponibilidad update) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setMotivo(update.getMotivo());
                    existing.setFechaInicio(update.getFechaInicio());
                    existing.setFechaFin(update.getFechaFin());
                    existing.setIdOferta(update.getIdOferta());
                    existing.setIdReserva(update.getIdReserva());
                    return repository.save(existing);
                })
                .orElse(null);
    }

    public boolean eliminar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Métodos de navegación de relaciones JPA
    
    /**
     * Obtiene la reserva asociada a una no disponibilidad
     * @param noDisponibilidadId ID de la no disponibilidad
     * @return Reserva asociada o null si no existe
     */
    public Reserva getReservaByNoDisponibilidad(Integer noDisponibilidadId) {
        NoDisponibilidad noDisp = repository.findById(noDisponibilidadId).orElse(null);
        return noDisp != null ? noDisp.getReserva() : null;
    }

    /**
     * Verifica si una no disponibilidad tiene una reserva asociada
     * @param noDisponibilidadId ID de la no disponibilidad
     * @return true si tiene reserva asociada
     */
    public boolean hasReserva(Integer noDisponibilidadId) {
        return getReservaByNoDisponibilidad(noDisponibilidadId) != null;
    }

    /**
     * Obtiene todas las no disponibilidades asociadas a una reserva
     * @param reservaId ID de la reserva
     * @return Lista de no disponibilidades de la reserva
     */
    public List<NoDisponibilidadDTO> obtenerPorIdReserva(Integer reservaId) {
        return repository.findByIdReserva(reservaId).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las no disponibilidades que tienen reserva asociada
     * @return Lista de no disponibilidades con reserva
     */
    public List<NoDisponibilidadDTO> obtenerConReserva() {
        return repository.findAll().stream()
                .filter(nd -> nd.getIdReserva() != null)
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las no disponibilidades que NO tienen reserva asociada
     * @return Lista de no disponibilidades sin reserva
     */
    public List<NoDisponibilidadDTO> obtenerSinReserva() {
        return repository.findAll().stream()
                .filter(nd -> nd.getIdReserva() == null)
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }
    // 🔹 Buscar por motivo (Native Query)
    public List<NoDisponibilidadDTO> buscarPorMotivoNative(String motivo) {
        return repository.findByMotivoNative(motivo).stream()
                .map(NoDisponibilidadMapper::toDTO)
                .collect(Collectors.toList());
    }
}
