package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    /**
     * Busca una categoría por su campo 'detalle'.
     * @param detalle El detalle a buscar.
     * @return Un Optional que contiene la Categoria si se encuentra.
     */
    Optional<Categoria> findByDetalle(String detalle);

    /**
     * Verifica si ya existe una categoría con un 'detalle' específico.
     * Es más eficiente que findByDetalle().isPresent() porque no necesita traer la entidad.
     * @param detalle El detalle a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByDetalle(String detalle);
}