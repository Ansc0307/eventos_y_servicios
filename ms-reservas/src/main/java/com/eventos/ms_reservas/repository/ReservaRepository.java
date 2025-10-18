package com.eventos.ms_reservas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventos.ms_reservas.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    //derived
    List<Reserva> findByEstado(String estado);
    
    List<Reserva> findByIdSolicitud(Integer idSolicitud);
    //JPQL
    @Query("SELECT r FROM Reserva r WHERE r.fechaReservaInicio BETWEEN :inicio AND :fin")
    List<Reserva> findByFechaReservaInicioBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

     @Query("SELECT r FROM Reserva r WHERE " +
           "(r.fechaReservaInicio <= :fin AND r.fechaReservaFin >= :inicio)")
    List<Reserva> findReservasConflictivas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    // Native SQL equivalent (uses table/column names as in the Reserva entity)
    @Query(value = "SELECT * FROM reservas r WHERE (r.fecha_reserva_inicio <= :fin AND r.fecha_reserva_fin >= :inicio)", nativeQuery = true)
    List<Reserva> findReservasConflictivasNative(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
