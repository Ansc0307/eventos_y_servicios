package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    
    // Obtener todas las ofertas activas
    List<Oferta> findByActivoTrue();
    
    // Obtener ofertas por categoría
    @Query("SELECT o FROM Oferta o WHERE o.categoria.idCategoria = :idCategoria AND o.activo = true")
    List<Oferta> findByCategoria(@Param("idCategoria") Long idCategoria);
    
    // Buscar oferta por ID y activo
    @Query("SELECT o FROM Oferta o WHERE o.idOfertas = :id AND o.activo = true")
    Oferta findByIdAndActivoTrue(@Param("id") Long id);
}