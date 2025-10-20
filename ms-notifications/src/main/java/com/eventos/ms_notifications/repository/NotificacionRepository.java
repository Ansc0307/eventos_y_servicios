package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    // Derived Query - Generada automáticamente por Spring
    List<Notificacion> findByUserId(Long userId);

    // Native Query - usando SQL puro (referencia, no se usa actualmente)
    @Query(value = "SELECT * FROM notificacion WHERE user_id = :userId", nativeQuery = true)
    List<Notificacion> buscarPorUserId(@Param("userId") Long userId);
    
    // JPQL - Similar a SQL pero orientado a entidades (referencia, no se usa actualmente)
    @Query("SELECT n FROM Notificacion n WHERE n.userId = :userId")
    List<Notificacion> buscarPorUserIdJPQL(@Param("userId") Long userId);
    
}
