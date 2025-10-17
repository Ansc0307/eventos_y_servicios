package com.eventos.ms_reservas.service;

import com.eventos.ms_reservas.model.Reserva;
import com.eventos.ms_reservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public Reserva getById(Integer id) {
        return reservaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reserva> getByEstado(String estado) {
        return reservaRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Reserva> getByIdSolicitud(Integer idSolicitud) {
        return reservaRepository.findByIdSolicitud(idSolicitud);
    }

    public Reserva save(Reserva reserva) {
        // Validar conflictos de horarios antes de guardar
        if (reserva.getIdReserva() == null) {
            validateNoConflicts(reserva.getFechaReservaInicio(), reserva.getFechaReservaFin());
        }
        return reservaRepository.save(reserva);
    }

    public Reserva update(Integer id, Reserva reserva) {
        Optional<Reserva> existingReservaOpt = reservaRepository.findById(id);
        if (existingReservaOpt.isEmpty()) {
            return null;
        }

        Reserva existingReserva = existingReservaOpt.get();
        
        // Validar conflictos de horarios excluyendo la reserva actual
        validateNoConflictsExcluding(reserva.getFechaReservaInicio(), reserva.getFechaReservaFin(), id);
        
        // Actualizar campos
        existingReserva.setIdSolicitud(reserva.getIdSolicitud());
        existingReserva.setFechaReservaInicio(reserva.getFechaReservaInicio());
        existingReserva.setFechaReservaFin(reserva.getFechaReservaFin());
        existingReserva.setEstado(reserva.getEstado());
        
        return reservaRepository.save(existingReserva);
    }

    public boolean delete(Integer id) {
        if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Reserva> getReservasEnRango(LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.findByFechaReservaInicioBetween(inicio, fin);
    }

    @Transactional(readOnly = true)
    public List<Reserva> getReservasConflictivas(LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.findReservasConflictivas(inicio, fin);
    }

    @Transactional(readOnly = true)
    public List<Reserva> getReservasConflictivasNative(LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.findReservasConflictivasNative(inicio, fin);
    }

    private void validateNoConflicts(LocalDateTime inicio, LocalDateTime fin) {
        List<Reserva> conflictivas = reservaRepository.findReservasConflictivas(inicio, fin);
        if (!conflictivas.isEmpty()) {
            throw new IllegalArgumentException("Existe conflicto de horarios con reservas existentes");
        }
    }

    private void validateNoConflictsExcluding(LocalDateTime inicio, LocalDateTime fin, Integer excludeId) {
        List<Reserva> conflictivas = reservaRepository.findReservasConflictivas(inicio, fin);
        conflictivas.removeIf(r -> r.getIdReserva().equals(excludeId));
        if (!conflictivas.isEmpty()) {
            throw new IllegalArgumentException("Existe conflicto de horarios con reservas existentes");
        }
    }
}
