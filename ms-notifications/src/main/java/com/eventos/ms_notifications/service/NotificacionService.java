package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificacionCreateDTO;
import com.eventos.ms_notifications.dto.NotificacionResponseDTO;
import com.eventos.ms_notifications.mapper.NotificacionMapper;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.NotificacionRepository;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PrioridadRepository prioridadRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               PrioridadRepository prioridadRepository,
                               TipoNotificacionRepository tipoNotificacionRepository) {
        this.notificacionRepository = notificacionRepository;
        this.prioridadRepository = prioridadRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
    }

    // Crear una notificación
    public NotificacionResponseDTO crearNotificacion(NotificacionCreateDTO dto) {
        Prioridad prioridad = prioridadRepository.findById(dto.getPrioridadId())
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada"));

        TipoNotificacion tipo = tipoNotificacionRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new RuntimeException("Tipo de notificación no encontrado"));

        Notificacion entity = NotificacionMapper.toEntity(dto, prioridad, tipo);
        Notificacion saved = notificacionRepository.save(entity);
        return NotificacionMapper.toResponseDTO(saved);
    }

    // Obtener todas las notificaciones 
    public List<NotificacionResponseDTO> listarTodas() {
        return notificacionRepository.findAll()
                .stream()
                .map(NotificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


}
