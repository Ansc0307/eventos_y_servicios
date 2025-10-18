package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
