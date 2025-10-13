package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.mapper.TipoNotificacionMapper;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoNotificacionService {

    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final TipoNotificacionMapper tipoNotificacionMapper;

    public TipoNotificacionService(TipoNotificacionRepository tipoNotificacionRepository,
                                   TipoNotificacionMapper tipoNotificacionMapper) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.tipoNotificacionMapper = tipoNotificacionMapper;
    }

    public List<TipoNotificacionDTO> obtenerTodas() {
        return tipoNotificacionRepository.findAll()
                .stream()
                .map(tipoNotificacionMapper::toDto)
                .collect(Collectors.toList());
    }

    public TipoNotificacionDTO obtenerPorId(Long id) {
        TipoNotificacion tipo = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de notificación no encontrada con ID: " + id));
        return tipoNotificacionMapper.toDto(tipo);
    }

    public TipoNotificacionDTO crear(TipoNotificacionDTO tipoDTO) {
        if (tipoNotificacionRepository.existsByNombreIgnoreCase(tipoDTO.getNombre())) {
            throw new RuntimeException("Ya existe un tipo de notificación con ese nombre");
        }
        TipoNotificacion tipo = tipoNotificacionMapper.toEntity(tipoDTO);
        return tipoNotificacionMapper.toDto(tipoNotificacionRepository.save(tipo));
    }

    public TipoNotificacionDTO actualizar(Long id, TipoNotificacionDTO tipoDTO) {
        TipoNotificacion existente = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de notificación no encontrada con ID: " + id));

        existente.setNombre(tipoDTO.getNombre());
        existente.setDescripcion(tipoDTO.getDescripcion());

        return tipoNotificacionMapper.toDto(tipoNotificacionRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!tipoNotificacionRepository.existsById(id)) {
            throw new RuntimeException("Tipo de notificación no encontrada con ID: " + id);
        }
        tipoNotificacionRepository.deleteById(id);
    }
}
