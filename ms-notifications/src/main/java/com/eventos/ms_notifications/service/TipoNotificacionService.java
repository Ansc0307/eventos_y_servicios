package com.eventos.ms_notifications.service;

import com.eventos.ms_notifications.dto.TipoNotificacionDTO;
import com.eventos.ms_notifications.exception.ConflictException;
import com.eventos.ms_notifications.exception.NotFoundException;
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

    // ============ MÉTODOS CRUD BÁSICOS ============
    
    @Transactional
    public TipoNotificacionDTO crearTipoNotificacion(TipoNotificacionDTO dto) {
        // Validar que no exista un tipo con el mismo nombre
        if (tipoNotificacionRepository.findByNombre(dto.getNombre()).isPresent()) {
            throw new ConflictException("Tipo de notificación", "nombre", dto.getNombre());
        }
        
        TipoNotificacion tipoNotificacion = tipoNotificacionMapper.toEntity(dto);
        TipoNotificacion saved = tipoNotificacionRepository.save(tipoNotificacion);
        return tipoNotificacionMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<TipoNotificacionDTO> listarTiposNotificacion() {
        return tipoNotificacionRepository.findAllByOrderByNombreAsc()
                .stream()
                .map(tipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoNotificacionDTO obtenerPorId(Long id) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", id));
        return tipoNotificacionMapper.toDTO(tipoNotificacion);
    }

    @Transactional
    public TipoNotificacionDTO actualizarTipoNotificacion(Long id, TipoNotificacionDTO dto) {
        TipoNotificacion existingTipo = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", id));
        
        // Verificar que el nombre no esté en uso por otro tipo
        tipoNotificacionRepository.findByNombre(dto.getNombre())
                .ifPresent(tipoConMismoNombre -> {
                    if (!tipoConMismoNombre.getId().equals(id)) {
                        throw new ConflictException("Tipo de notificación", "nombre", dto.getNombre());
                    }
                });
        
        tipoNotificacionMapper.updateEntityFromDTO(dto, existingTipo);
        TipoNotificacion updated = tipoNotificacionRepository.save(existingTipo);
        return tipoNotificacionMapper.toDTO(updated);
    }

    @Transactional
    public void eliminarTipoNotificacion(Long id) {
        if (!tipoNotificacionRepository.existsById(id)) {
            throw new NotFoundException("Tipo de notificación", id);
        }
        tipoNotificacionRepository.deleteById(id);
    }

    // ============ MÉTODOS ADICIONALES ============
    
    @Transactional(readOnly = true)
    public List<TipoNotificacionDTO> listarTiposActivos() {
        return tipoNotificacionRepository.findByActivoTrue()
                .stream()
                .map(tipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoNotificacionDTO> listarTiposQueRequierenAck() {
        return tipoNotificacionRepository.findByRequiereAckTrue()
                .stream()
                .map(tipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoNotificacionDTO> listarTiposActivosQueRequierenAck() {
        return tipoNotificacionRepository.findByActivoTrueAndRequiereAckTrue()
                .stream()
                .map(tipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return tipoNotificacionRepository.findByNombre(nombre).isPresent();
    }

    @Transactional(readOnly = true)
    public List<TipoNotificacionDTO> buscarPorNombreContaining(String nombre) {
        return tipoNotificacionRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(tipoNotificacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TipoNotificacionDTO desactivarTipoNotificacion(Long id) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", id));
        
        tipoNotificacion.setActivo(false);
        TipoNotificacion updated = tipoNotificacionRepository.save(tipoNotificacion);
        return tipoNotificacionMapper.toDTO(updated);
    }

    @Transactional
    public TipoNotificacionDTO activarTipoNotificacion(Long id) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", id));
        
        tipoNotificacion.setActivo(true);
        TipoNotificacion updated = tipoNotificacionRepository.save(tipoNotificacion);
        return tipoNotificacionMapper.toDTO(updated);
    }

    @Transactional
    public TipoNotificacionDTO cambiarEstadoAck(Long id, Boolean requiereAck) {
        TipoNotificacion tipoNotificacion = tipoNotificacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipo de notificación", id));
        
        tipoNotificacion.setRequiereAck(requiereAck);
        TipoNotificacion updated = tipoNotificacionRepository.save(tipoNotificacion);
        return tipoNotificacionMapper.toDTO(updated);
    }
}