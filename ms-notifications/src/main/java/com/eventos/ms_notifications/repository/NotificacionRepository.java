package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Buscar notificaciones por usuario
     */
    List<Notificacion> findByUserId(Long userId);

    /**
     * Buscar notificaciones por usuario con paginación
     */
    Page<Notificacion> findByUserId(Long userId, Pageable pageable);

    /**
     * Buscar notificaciones por usuario y estado de lectura
     */
    List<Notificacion> findByUserIdAndLeido(Long userId, Boolean leido);

    /**
     * Buscar notificaciones no leídas por usuario
     */
    List<Notificacion> findByUserIdAndLeidoFalse(Long userId);

    /**
     * Buscar notificaciones por prioridad
     */
    List<Notificacion> findByPrioridadId(Long prioridadId);

    /**
     * Buscar notificaciones por tipo de notificación
     */
    List<Notificacion> findByTipoNotificacionId(Long tipoNotificacionId);

    /**
     * Buscar notificaciones creadas después de una fecha
     */
    List<Notificacion> findByFechaCreacionAfter(LocalDateTime fecha);

    /**
     * Buscar notificaciones por usuario y rango de fechas
     */
    @Query("SELECT n FROM Notificacion n WHERE n.userId = :userId AND n.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Notificacion> findByUserIdAndFechaCreacionBetween(
            @Param("userId") Long userId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Contar notificaciones no leídas por usuario
     */
    Long countByUserIdAndLeidoFalse(Long userId);

    /**
     * Buscar notificaciones por usuario ordenadas por fecha descendente
     */
    List<Notificacion> findByUserIdOrderByFechaCreacionDesc(Long userId);

    /**
     * Eliminar notificaciones antiguas (para limpieza)
     */
    void deleteByFechaCreacionBefore(LocalDateTime fecha);

    /**
     * Buscar notificaciones que requieren acknowledgment y no han sido leídas
     */
    @Query("SELECT n FROM Notificacion n WHERE n.leido = false AND n.tipoNotificacion.requiereAck = true")
    List<Notificacion> findNotificacionesPendientesDeAck();
}