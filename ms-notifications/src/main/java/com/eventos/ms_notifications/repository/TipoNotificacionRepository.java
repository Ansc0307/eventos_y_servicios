package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {

    // Buscar por nombre (para validar duplicados)
    Optional<TipoNotificacion> findByNombre(String nombre);
}
