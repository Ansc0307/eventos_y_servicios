package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.OfertaMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaMediaRepository extends JpaRepository<OfertaMedia, Long> {
}
