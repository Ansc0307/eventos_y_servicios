package com.eventos.ms_reservas.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eventos.ms_reservas.model.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    // Buscar solicitudes por estado
    List<Solicitud> findByEstadoSolicitud(String estadoSolicitud);

    // Buscar por ID de oferta
    Optional<Solicitud> findByIdOferta(Integer idOferta);

    // Verificar existencia por organizador y proveedor
    boolean existsByIdOrganizadorAndIdProovedor(Integer idOrganizador, Integer idProovedor);

    // Buscar solicitudes de un organizador específico
    @Query("SELECT s FROM Solicitud s WHERE s.idOrganizador = :idOrganizador")
    List<Solicitud> findByOrganizador(@Param("idOrganizador") Integer idOrganizador);

    // Buscar solicitudes de un proveedor específico
    @Query("SELECT s FROM Solicitud s WHERE s.idProovedor = :idProovedor")
    List<Solicitud> findByProovedor(@Param("idProovedor") Integer idProovedor);

    // Buscar solicitudes creadas dentro de un rango de fechas (si tienes fechaSolicitud en el modelo)
    @Query("SELECT s FROM Solicitud s WHERE s.fechaSolicitud BETWEEN :inicio AND :fin")
    List<Solicitud> findByFechaSolicitudBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Buscar solicitudes con estado específico y dentro de un rango de fechas
    @Query("SELECT s FROM Solicitud s WHERE s.estadoSolicitud = :estado AND s.fechaSolicitud BETWEEN :inicio AND :fin")
    List<Solicitud> findByEstadoAndFechaSolicitudBetween(
            @Param("estado") String estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}