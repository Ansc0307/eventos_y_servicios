package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {

    Optional<Prioridad> findByNombre(String nombre);
    
    List<Prioridad> findByActivoTrue();
    
    List<Prioridad> findByNivel(Integer nivel);
    
    List<Prioridad> findAllByOrderByNivelAsc();

    @Query("SELECT p FROM Prioridad p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Prioridad> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    @Query("SELECT p FROM Prioridad p WHERE p.activo = true ORDER BY p.nivel ASC")
    List<Prioridad> findActivasOrdenadasPorNivel();
}