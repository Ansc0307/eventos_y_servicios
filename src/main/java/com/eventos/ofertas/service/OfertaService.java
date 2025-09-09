package com.eventos.ofertas.service;

import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.dto.UpdateOfertaDTO;
import com.eventos.ofertas.entity.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface OfertaService {
    Mono<OfertaDTO> crear(OfertaDTO request);
    Mono<OfertaDTO> obtener(Long id);
    Flux<OfertaDTO> listar(Categoria categoria);
    Mono<OfertaDTO> actualizarParcial(Long id, UpdateOfertaDTO patch);
    Mono<Void> eliminar(Long id);
}
