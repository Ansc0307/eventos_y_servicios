package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {

    // Buscar por nombre (por si quieres validar duplicados)
    Optional<Prioridad> findByNombre(String nombre);
}