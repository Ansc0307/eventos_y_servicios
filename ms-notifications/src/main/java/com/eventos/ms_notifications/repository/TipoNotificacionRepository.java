package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {
    
    // Derived Query - Generada automáticamente por Spring
    boolean existsByNombreIgnoreCase(String nombre);

    // Native Query - usando SQL puro (referencia, no se usa actualmente)
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tipo_notificacion WHERE LOWER(nombre) = LOWER(:nombre)", nativeQuery = true)
    boolean existePorNombre(@Param("nombre") String nombre);
    
    // JPQL - Similar a SQL pero orientado a entidades (referencia, no se usa actualmente)
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TipoNotificacion t WHERE LOWER(t.nombre) = LOWER(:nombre)")
    boolean existePorNombreJPQL(@Param("nombre") String nombre);
}
