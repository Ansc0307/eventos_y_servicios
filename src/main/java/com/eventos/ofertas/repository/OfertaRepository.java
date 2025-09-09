package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.entity.Oferta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OfertaRepository {
    Mono<Oferta> save(Oferta oferta);
    Mono<Oferta> findById(Long id);
    Flux<Oferta> findAll();
    Flux<Oferta> findByCategoria(Categoria categoria);
    Mono<Void> deleteById(Long id);
    Mono<Boolean> existsByProviderIdAndTitulo(Long providerId, String titulo);
}