package com.eventos.ms_reservas.service;

import com.eventos.ms_reservas.model.Reserva;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {
	private final Map<String, Reserva> reservas = new HashMap<>();

	public Reserva getById(String id) {
		return reservas.get(id);
	}

	public Reserva save(Reserva reserva) {
		reservas.put(reserva.getId(), reserva);
		return reserva;
	}

	public Reserva update(String id, Reserva reserva) {
		if (!reservas.containsKey(id)) return null;
		reserva.setId(id);
		reservas.put(id, reserva);
		return reserva;
	}

	public boolean delete(String id) {
		return reservas.remove(id) != null;
	}
}
