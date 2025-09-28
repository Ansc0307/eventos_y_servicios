package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUserId(Long userId);
}
