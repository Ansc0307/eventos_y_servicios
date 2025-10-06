package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {

    /**
     * Buscar tipo de notificación por nombre exacto
     */
    Optional<TipoNotificacion> findByNombre(String nombre);

    /**
     * Buscar tipos de notificación por estado activo
     */
    List<TipoNotificacion> findByActivoTrue();

    /**
     * Buscar tipos de notificación que requieren acknowledgment
     */
    List<TipoNotificacion> findByRequiereAckTrue();

    /**
     * Buscar tipos de notificación activos que requieren acknowledgment
     */
    List<TipoNotificacion> findByActivoTrueAndRequiereAckTrue();

    /**
     * Verificar si existe un tipo de notificación con nombre ignorando mayúsculas/minúsculas
     */
    @Query("SELECT t FROM TipoNotificacion t WHERE LOWER(t.nombre) = LOWER(:nombre)")
    Optional<TipoNotificacion> findByNombreIgnoreCase(@Param("nombre") String nombre);

    /**
     * Buscar tipos de notificación por nombre que contenga el texto (case insensitive)
     */
    @Query("SELECT t FROM TipoNotificacion t WHERE LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<TipoNotificacion> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Obtener todos los tipos de notificación ordenados por nombre
     */
    List<TipoNotificacion> findAllByOrderByNombreAsc();
}