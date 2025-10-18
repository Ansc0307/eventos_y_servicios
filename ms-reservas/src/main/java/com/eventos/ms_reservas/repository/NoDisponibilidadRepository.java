package com.eventos.ms_reservas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventos.ms_reservas.model.NoDisponibilidad;

@Repository
public interface NoDisponibilidadRepository extends JpaRepository<NoDisponibilidad, Integer> {

    // Buscar todas las no disponibilidades por idOferta
    List<NoDisponibilidad> findByIdOferta(Integer idOferta);

    // Buscar por idReserva
    List<NoDisponibilidad> findByIdReserva(Integer idReserva);

    // Buscar por motivo que contenga una palabra clave
    List<NoDisponibilidad> findByMotivoContainingIgnoreCase(String motivo);
    @Query("SELECT n FROM NoDisponibilidad n WHERE n.idOferta = :idOferta AND n.fechaFin >= CURRENT_TIMESTAMP")
List<NoDisponibilidad> findActivasByOferta(@Param("idOferta") Integer idOferta);


    // Buscar registros entre dos fechas
    @Query("SELECT n FROM NoDisponibilidad n WHERE n.fechaInicio BETWEEN :inicio AND :fin")
    List<NoDisponibilidad> findByFechaInicioBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Buscar conflictos de fechas
    @Query("SELECT n FROM NoDisponibilidad n WHERE " +
            "(n.fechaInicio <= :fin AND n.fechaFin >= :inicio)")
    List<NoDisponibilidad> findConflictosDeFecha(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

 // 🔹 MÉTODOS NUEVOS (Native Query)
    // ------------------------------------------------------------------

    // Buscar por motivo (ignora mayúsculas/minúsculas)
    @Query(value = "SELECT * FROM no_disponibilidad n WHERE LOWER(n.motivo) LIKE LOWER(CONCAT('%', :motivo, '%'))",
           nativeQuery = true)
    List<NoDisponibilidad> findByMotivoNative(@Param("motivo") String motivo);
}