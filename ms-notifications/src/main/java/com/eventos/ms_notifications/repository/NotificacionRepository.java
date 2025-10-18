package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    //notificaciones por userId
    List<Notificacion> findByUserId(Long userId);
}
