package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.PrecioDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioDescuentoRepository extends JpaRepository<PrecioDescuento, Long> {
}