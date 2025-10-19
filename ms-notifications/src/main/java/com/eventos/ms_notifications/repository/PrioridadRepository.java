package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    //no se si agregar algo más...
}
