package com.eventos.ms_reservas.service;

import com.eventos.ms_reservas.model.Evento;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class EventoService {
	private final Map<String, Evento> eventos = new HashMap<>();

	public Evento getById(String id) {
		return eventos.get(id);
	}

	public Evento save(Evento evento) {
		eventos.put(evento.getId(), evento);
		return evento;
	}

	public Evento update(String id, Evento evento) {
		if (!eventos.containsKey(id)) return null;
		evento.setId(id);
		eventos.put(id, evento);
		return evento;
	}

	public boolean delete(String id) {
		return eventos.remove(id) != null;
	}
}
