package com.eventos.ofertas.repository;

import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.entity.Oferta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryOfertaRepository implements OfertaRepository {  

    private final Map<Long, Oferta> store = new ConcurrentHashMap<>();

    @Override
    public Mono<Oferta> save(Oferta oferta) {
        store.put(oferta.getId(), oferta);
        return Mono.just(oferta);
    }


    @Override
    public Mono<Oferta> findById(Long id) {
        Oferta o = store.get(id);
        return o == null ? Mono.empty() : Mono.just(o);
    }


    @Override
    public Flux<Oferta> findAll() {
        return Flux.fromIterable(store.values());
    }


    @Override
    public Flux<Oferta> findByCategoria(Categoria categoria) {
        return Flux.fromStream(store.values().stream().filter(o -> o.getCategoria() == categoria));
    }


    @Override
    public Mono<Void> deleteById(Long id) {
        store.remove(id);
        return Mono.empty();
    }
    @Override
    public Mono<Boolean> existsByProviderIdAndTitulo(Long providerId, String titulo) {
        boolean exists = store.values().stream()
            .anyMatch(o -> o.getProviderId().equals(providerId)
            && o.getTitulo().equalsIgnoreCase(titulo));
        return Mono.just(exists);
    }
}