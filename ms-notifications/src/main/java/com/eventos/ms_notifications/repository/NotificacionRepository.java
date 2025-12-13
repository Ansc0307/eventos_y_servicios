package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.dto.NotificacionConNombresDTO;
import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificacionRepository extends ReactiveCrudRepository<Notificacion, Long> {

    // =====================
    // Derived Query
    // =====================
    Flux<Notificacion> findByUserId(Long userId);

    // =====================
    // Queries con JOIN
    // =====================

    /**
     * Obtiene TODAS las notificaciones con nombres de prioridad y tipo
     */
    @Query("""
        SELECT 
            n.id,
            n.asunto,
            n.mensaje,
            n.fecha_creacion,
            n.leido,
            n.user_id,
            n.id_prioridad,
            p.nombre AS prioridad_nombre,
            n.id_tipo AS tipo_id,
            t.nombre AS tipo_nombre
        FROM notificacion n
        LEFT JOIN prioridad p ON n.id_prioridad = p.id
        LEFT JOIN tipo_notificacion t ON n.id_tipo = t.id
        ORDER BY n.fecha_creacion DESC
    """)
    Flux<NotificacionConNombresDTO> findAllWithNames();

    /**
     * Obtiene una notificación por ID con nombres de prioridad y tipo
     */
    @Query("""
        SELECT 
            n.id,
            n.asunto,
            n.mensaje,
            n.fecha_creacion,
            n.leido,
            n.user_id,
            n.id_prioridad,
            p.nombre AS prioridad_nombre,
            n.id_tipo AS tipo_id,
            t.nombre AS tipo_nombre
        FROM notificacion n
        LEFT JOIN prioridad p ON n.id_prioridad = p.id
        LEFT JOIN tipo_notificacion t ON n.id_tipo = t.id
        WHERE n.id = :id
    """)
    Mono<NotificacionConNombresDTO> findByIdWithNames(Long id);

    /**
     * Obtiene notificaciones por usuario con nombres de prioridad y tipo
     */
    @Query("""
        SELECT 
            n.id,
            n.asunto,
            n.mensaje,
            n.fecha_creacion,
            n.leido,
            n.user_id,
            n.id_prioridad,
            p.nombre AS prioridad_nombre,
            n.id_tipo AS tipo_id,
            t.nombre AS tipo_nombre
        FROM notificacion n
        LEFT JOIN prioridad p ON n.id_prioridad = p.id
        LEFT JOIN tipo_notificacion t ON n.id_tipo = t.id
        WHERE n.user_id = :userId
        ORDER BY n.fecha_creacion DESC
    """)
    Flux<NotificacionConNombresDTO> findByUserIdWithNames(Long userId);
}
