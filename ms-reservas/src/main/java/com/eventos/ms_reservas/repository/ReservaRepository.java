package com.eventos.ms_reservas.repository;

import com.eventos.ms_reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    
    List<Reserva> findByEstado(String estado);
    
    List<Reserva> findByIdSolicitud(Integer idSolicitud);
    
    @Query("SELECT r FROM Reserva r WHERE r.fechaReservaInicio BETWEEN :inicio AND :fin")
    List<Reserva> findByFechaReservaInicioBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT r FROM Reserva r WHERE " +
           "(r.fechaReservaInicio <= :fin AND r.fechaReservaFin >= :inicio)")
    List<Reserva> findReservasConflictivas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
