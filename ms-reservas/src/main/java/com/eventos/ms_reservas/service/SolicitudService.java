package com.eventos.ms_reservas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eventos.ms_reservas.dto.SolicitudDTO;
import com.eventos.ms_reservas.mapper.SolicitudMapper;
import com.eventos.ms_reservas.model.Solicitud;
import com.eventos.ms_reservas.repository.SolicitudRepository;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    // Crear
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO) {
        Solicitud solicitud = SolicitudMapper.toEntity(solicitudDTO);
        Solicitud guardada = solicitudRepository.save(solicitud);
        return SolicitudMapper.toDTO(guardada);
    }

    // Obtener todas
    public List<SolicitudDTO> obtenerTodas() {
        return solicitudRepository.findAll()
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public Optional<SolicitudDTO> obtenerPorId(Integer id) {
        return solicitudRepository.findById(id.intValue()) // conversión necesaria
                .map(SolicitudMapper::toDTO);
    }

    // Actualizar
    public Optional<SolicitudDTO> actualizarSolicitud(Integer id, SolicitudDTO solicitudDTO) {
        return solicitudRepository.findById(id.intValue())
                .map(solicitudExistente -> {
                    Solicitud solicitud = SolicitudMapper.toEntity(solicitudDTO);
                    solicitud.setIdSolicitud(id); // mantener ID
                    Solicitud actualizada = solicitudRepository.save(solicitud);
                    return SolicitudMapper.toDTO(actualizada);
                });
    }

    // Eliminar
    public boolean eliminarSolicitud(Integer id) {
        if (solicitudRepository.existsById(id.intValue())) {
            solicitudRepository.deleteById(id.intValue());
            return true;
        }
        return false;
    }

    // NUEVOS MÉTODOS

    public Optional<SolicitudDTO> actualizarEstado(Integer id, String nuevoEstado) {
    return solicitudRepository.findById(id)
            .map(solicitud -> {
                solicitud.setEstadoSolicitud(nuevoEstado);
                Solicitud actualizada = solicitudRepository.save(solicitud);
                return SolicitudMapper.toDTO(actualizada);
            });
}


    public List<SolicitudDTO> obtenerPorEstado(String estado) {
        return solicitudRepository.findByEstadoSolicitud(estado)
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<SolicitudDTO> obtenerPorIdOferta(Integer idOferta) {
        return solicitudRepository.findByIdOferta(idOferta.intValue())
                .map(SolicitudMapper::toDTO);
    }

    public boolean existePorOrganizadorYProveedor(Integer idOrganizador, Integer idProveedor) {
        return solicitudRepository.existsByIdOrganizadorAndIdProovedor(idOrganizador, idProveedor);
    }

    public List<SolicitudDTO> obtenerPorOrganizador(Integer idOrganizador) {
        return solicitudRepository.findByOrganizador(idOrganizador)
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> obtenerPorProveedor(Integer idProveedor) {
        return solicitudRepository.findByProovedor(idProveedor)
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> obtenerPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return solicitudRepository.findByFechaSolicitudBetween(inicio, fin)
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> obtenerPorEstadoYRangoFechas(String estado, LocalDateTime inicio, LocalDateTime fin) {
        return solicitudRepository.findByEstadoAndFechaSolicitudBetween(estado, inicio, fin)
                .stream()
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Métodos de navegación de relaciones JPA
    
    /**
     * Obtiene la reserva asociada a una solicitud
     * @param solicitudId ID de la solicitud
     * @return Reserva asociada o null si no existe
     */
    public com.eventos.ms_reservas.model.Reserva getReservaBySolicitud(Integer solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElse(null);
        return solicitud != null ? solicitud.getReserva() : null;
    }

    /**
     * Verifica si una solicitud tiene una reserva asociada
     * @param solicitudId ID de la solicitud
     * @return true si tiene reserva asociada
     */
    public boolean hasReserva(Integer solicitudId) {
        return getReservaBySolicitud(solicitudId) != null;
    }

    /**
     * Obtiene todas las solicitudes que tienen reserva asociada
     * @return Lista de solicitudes con reserva
     */
    public List<SolicitudDTO> obtenerConReserva() {
        return solicitudRepository.findAll().stream()
                .filter(s -> s.getReserva() != null)
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las solicitudes que NO tienen reserva asociada
     * @return Lista de solicitudes sin reserva
     */
    public List<SolicitudDTO> obtenerSinReserva() {
        return solicitudRepository.findAll().stream()
                .filter(s -> s.getReserva() == null)
                .map(SolicitudMapper::toDTO)
                .collect(Collectors.toList());
    }
}