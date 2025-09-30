package com.eventos.ms_reservas.service;

import com.eventos.ms_reservas.model.Reserva;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {
	private final Map<Long, Reserva> reservas = new HashMap<>();
	private Long nextId = 1L;

	public Reserva getById(Long id) {
		return reservas.get(id);
	}

	public Reserva save(Reserva reserva) {
		if (reserva.getIdReserva() == null) {
			reserva.setIdReserva(nextId++);
		}
		
		// Establecer fechas de auditoría
		if (reserva.getFechaCreacion() == null) {
			reserva.setFechaCreacion(LocalDateTime.now());
		}
		reserva.setFechaActualizacion(LocalDateTime.now());
		
		reservas.put(reserva.getIdReserva(), reserva);
		return reserva;
	}

	public Reserva update(Long id, Reserva reserva) {
		if (!reservas.containsKey(id)) return null;
		
		// Mantener la fecha de creación original
		Reserva existingReserva = reservas.get(id);
		reserva.setIdReserva(id);
		reserva.setFechaCreacion(existingReserva.getFechaCreacion());
		reserva.setFechaActualizacion(LocalDateTime.now());
		
		reservas.put(id, reserva);
		return reserva;
	}

	public boolean delete(Long id) {
		return reservas.remove(id) != null;
	}
}
