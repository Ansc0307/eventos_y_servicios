package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.NotificacionDTO;
import com.eventos.ms_notifications.exception.BadRequestException;
import com.eventos.ms_notifications.exception.ConflictException;
import com.eventos.ms_notifications.exception.NotFoundException;
import com.eventos.ms_notifications.mapper.NotificacionMapper;
import com.eventos.ms_notifications.model.Notificacion;
import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.NotificacionRepository;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PrioridadRepository prioridadRepository;
    private final TipoNotificacionRepository tipoNotificacionRepository;
    private final NotificacionMapper notificacionMapper;

    public NotificacionService(NotificacionRepository notificacionRepository,
                              PrioridadRepository prioridadRepository,
                              TipoNotificacionRepository tipoNotificacionRepository,
                              NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.prioridadRepository = prioridadRepository;
        this.tipoNotificacionRepository = tipoNotificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    // ============ MÉTODOS CRUD BÁSICOS ============
    
    @Transactional
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        // Validar que el userId sea válido (aquí podrías integrar con el microservicio de usuarios)
        validarUserId(dto.getUserId());
        
        // Validar que existan la prioridad y el tipo de notificación
        Prioridad prioridad = prioridadRepository.findById(dto.getPrioridadId())
                .orElseThrow(() -> new NotFoundException("Prioridad", dto.getPrioridadId()));
        
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(dto.getTipoNotificacionId())
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", dto.getTipoNotificacionId()));

        // Validar que la prioridad y el tipo estén activos
        if (!prioridad.getActivo()) {
            throw new BadRequestException("La prioridad seleccionada no está activa");
        }
        if (!tipoNotificacion.getActivo()) {
            throw new BadRequestException("El tipo de notificación seleccionado no está activo");
        }

        Notificacion notificacion = notificacionMapper.toEntity(dto, prioridad, tipoNotificacion);
        Notificacion saved = notificacionRepository.save(notificacion);
        return notificacionMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarNotificaciones() {
        return notificacionRepository.findAll()
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<NotificacionDTO> listarNotificaciones(Pageable pageable) {
        return notificacionRepository.findAll(pageable)
                .map(notificacionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public NotificacionDTO obtenerPorId(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación", id));
        return notificacionMapper.toDTO(notificacion);
    }

    @Transactional
    public NotificacionDTO actualizarNotificacion(Long id, NotificacionDTO dto) {
        Notificacion existingNotificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación", id));

        // Validar userId si se proporciona
        if (dto.getUserId() != null) {
            validarUserId(dto.getUserId());
        }

        Prioridad prioridad = null;
        if (dto.getPrioridadId() != null) {
            prioridad = prioridadRepository.findById(dto.getPrioridadId())
                    .orElseThrow(() -> new NotFoundException("Prioridad", dto.getPrioridadId()));
            if (!prioridad.getActivo()) {
                throw new BadRequestException("La prioridad seleccionada no está activa");
            }
        }

        TipoNotificacion tipoNotificacion = null;
        if (dto.getTipoNotificacionId() != null) {
            tipoNotificacion = tipoNotificacionRepository.findById(dto.getTipoNotificacionId())
                    .orElseThrow(() -> new NotFoundException("Tipo de notificación", dto.getTipoNotificacionId()));
            if (!tipoNotificacion.getActivo()) {
                throw new BadRequestException("El tipo de notificación seleccionado no está activo");
            }
        }

        notificacionMapper.updateEntityFromDTO(dto, existingNotificacion, prioridad, tipoNotificacion);
        Notificacion updated = notificacionRepository.save(existingNotificacion);
        return notificacionMapper.toDTO(updated);
    }

    @Transactional
    public void eliminarNotificacion(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new NotFoundException("Notificación", id);
        }
        notificacionRepository.deleteById(id);
    }

    // ============ MÉTODOS ESPECÍFICOS POR USUARIO ============
    
    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorUsuario(Long userId) {
        validarUserId(userId);
        return notificacionRepository.findByUserIdOrderByFechaCreacionDesc(userId)
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<NotificacionDTO> listarPorUsuario(Long userId, Pageable pageable) {
        validarUserId(userId);
        return notificacionRepository.findByUserId(userId, pageable)
                .map(notificacionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarNoLeidasPorUsuario(Long userId) {
        validarUserId(userId);
        return notificacionRepository.findByUserIdAndLeidoFalse(userId)
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long contarNoLeidasPorUsuario(Long userId) {
        validarUserId(userId);
        return notificacionRepository.countByUserIdAndLeidoFalse(userId);
    }

    @Transactional
    public NotificacionDTO marcarComoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación", id));
        
        notificacion.setLeido(true);
        Notificacion updated = notificacionRepository.save(notificacion);
        return notificacionMapper.toDTO(updated);
    }

    @Transactional
    public NotificacionDTO marcarComoNoLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificación", id));
        
        notificacion.setLeido(false);
        Notificacion updated = notificacionRepository.save(notificacion);
        return notificacionMapper.toDTO(updated);
    }

    @Transactional
    public void marcarTodasComoLeidas(Long userId) {
        validarUserId(userId);
        List<Notificacion> noLeidas = notificacionRepository.findByUserIdAndLeidoFalse(userId);
        noLeidas.forEach(notificacion -> notificacion.setLeido(true));
        notificacionRepository.saveAll(noLeidas);
    }

    // ============ MÉTODOS ADICIONALES ============
    
    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorPrioridad(Long prioridadId) {
        return notificacionRepository.findByPrioridadId(prioridadId)
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPorTipoNotificacion(Long tipoNotificacionId) {
        return notificacionRepository.findByTipoNotificacionId(tipoNotificacionId)
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarRecientes(LocalDateTime desde) {
        return notificacionRepository.findByFechaCreacionAfter(desde)
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void limpiarNotificacionesAntiguas(LocalDateTime fechaLimite) {
        notificacionRepository.deleteByFechaCreacionBefore(fechaLimite);
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> listarPendientesDeAck() {
        return notificacionRepository.findNotificacionesPendientesDeAck()
                .stream()
                .map(notificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ============ VALIDACIONES ============
    
    private void validarUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BadRequestException("userId", "debe ser un número positivo");
        }
        // Aquí se integrará con el microservicio de usuarios
        // Por ahora solo validamos que sea un número positivo
    }
}