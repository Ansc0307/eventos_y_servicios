package com.eventos.ms_reservas.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eventos.ms_reservas.model.Solicitud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    // --- Derived Queries ---
    List<Solicitud> findByEstadoSolicitud(String estadoSolicitud);

    Optional<Solicitud> findByIdOferta(Integer idOferta);

    boolean existsByIdOrganizadorAndIdProovedor(Integer idOrganizador, Integer idProovedor);

    // --- JPQL Queries ---
    @Query("SELECT s FROM Solicitud s WHERE s.idOrganizador = :idOrganizador")
    List<Solicitud> findByOrganizador(@Param("idOrganizador") Integer idOrganizador);

    @Query("SELECT s FROM Solicitud s WHERE s.idProovedor = :idProovedor")
    List<Solicitud> findByProovedor(@Param("idProovedor") Integer idProovedor);

    @Query("SELECT s FROM Solicitud s WHERE s.fechaSolicitud BETWEEN :inicio AND :fin")
    List<Solicitud> findByFechaSolicitudBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT s FROM Solicitud s WHERE s.estadoSolicitud = :estado AND s.fechaSolicitud BETWEEN :inicio AND :fin")
    List<Solicitud> findByEstadoAndFechaSolicitudBetween(
            @Param("estado") String estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    // --- Native Query Example ---
    @Query(
        value = "SELECT * FROM solicitud s WHERE s.estado_solicitud = :estado AND s.id_organizador = :idOrganizador",
        nativeQuery = true
    )
    List<Solicitud> findNativeByEstadoAndOrganizador(
            @Param("estado") String estado,
            @Param("idOrganizador") Integer idOrganizador
    );
}

// --- Criteria Query Example (requiere clase aparte) ---
@Repository
@Transactional
class SolicitudRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Solicitud> findByEstadoAndProveedorCriteria(String estado, Integer idProveedor) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(Solicitud.class);
        var root = cq.from(Solicitud.class);

        cq.select(root)
          .where(
              cb.and(
                  cb.equal(root.get("estadoSolicitud"), estado),
                  cb.equal(root.get("idProovedor"), idProveedor)
              )
          );

        TypedQuery<Solicitud> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
