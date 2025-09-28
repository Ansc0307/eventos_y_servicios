package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    // Ejemplo: Listar notificaciones por usuario
    List<Notificacion> findByUserId(Long userId);

    // Ejemplo: Listar notificaciones no leídas por usuario
    List<Notificacion> findByUserIdAndLeidoFalse(Long userId);
}
