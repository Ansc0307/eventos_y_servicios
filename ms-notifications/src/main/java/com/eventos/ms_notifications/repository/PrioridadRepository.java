package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Prioridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
    // Derived Query - Generada automáticamente por Spring
    boolean existsByNombreIgnoreCase(String nombre);

    // Native Query - usando SQL puro
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM prioridad WHERE LOWER(nombre) = LOWER(:nombre)", nativeQuery = true)
    boolean existePorNombre(@Param("nombre") String nombre);

    // JPQL - Similar a SQL pero orientado a entidades
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Prioridad p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    boolean existePorNombreJPQL(@Param("nombre") String nombre);

}
