package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import com.eventos.ms_notifications.mapper.TipoNotificacionMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoNotificacionService {

    private final TipoNotificacionRepository tipoNotificacionRepository;

    public TipoNotificacionService(TipoNotificacionRepository tipoNotificacionRepository) {
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    // Crear tipo de notificación
    public TipoNotificacionDTO crearTipoNotificacion(TipoNotificacionDTO dto) {
        TipoNotificacion tipoNotificacion = TipoNotificacionMapper.toEntity(dto);
        TipoNotificacion saved = tipoNotificacionRepository.save(tipoNotificacion);
        return TipoNotificacionMapper.toDTO(saved);
    }

    // Listar todos los tipos de notificación
    public List<TipoNotificacionDTO> listarTiposNotificacion() {
        return tipoNotificacionRepository.findAll()
                .stream()
                .map(TipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener tipo de notificación por ID
    public TipoNotificacionDTO obtenerPorId(Long id) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de notificación no encontrado"));
        return TipoNotificacionMapper.toDTO(tipoNotificacion);
    }

    // Actualizar tipo de notificación
    public TipoNotificacionDTO actualizarTipoNotificacion(Long id, TipoNotificacionDTO dto) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de notificación no encontrado"));

        // Actualizamos todos los campos
        tipoNotificacion.setNombre(dto.getNombre());
        tipoNotificacion.setDescripcion(dto.getDescripcion());
        tipoNotificacion.setRequiereAck(dto.getRequiereAck());
        tipoNotificacion.setIcono(dto.getIcono());
        tipoNotificacion.setActivo(dto.getActivo());

        TipoNotificacion updated = tipoNotificacionRepository.save(tipoNotificacion);
        return TipoNotificacionMapper.toDTO(updated);
    }

    // Eliminar tipo de notificación
    public void eliminarTipoNotificacion(Long id) {
        if (!tipoNotificacionRepository.existsById(id)) {
            throw new RuntimeException("Tipo de notificación no encontrado");
        }
        tipoNotificacionRepository.deleteById(id);
    }
}
