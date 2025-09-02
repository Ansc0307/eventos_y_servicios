package com.eventos.ofertas.service;
import com.eventos.ofertas.dto.OfertaDTO;
import com.eventos.ofertas.dto.UpdateOfertaDTO;
import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.entity.Oferta;
import com.eventos.ofertas.exception.DuplicateTituloException;
import com.eventos.ofertas.exception.OfertaNotFoundException;
import com.eventos.ofertas.mapper.OfertaMapper;
import com.eventos.ofertas.repository.OfertaRepository;
import com.eventos.ofertas.util.SlugUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

@Service
public class OfertaServiceImpl implements OfertaService {
    private final OfertaRepository ofertaRepository;
    public OfertaServiceImpl(OfertaRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }
    @Override
    public Mono<OfertaDTO> crear(OfertaDTO request) {
        return ofertaRepository.existsByProviderIdAndTitulo(request.getProviderId(), request.getTitulo())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new DuplicateTituloException("El título ya existe"));
                }
                Oferta oferta = OfertaMapper.toEntity(request);
                oferta.setSlug(SlugUtil.slugify(oferta.getTitulo()));
                Instant now = Instant.now();
                oferta.setCreatedAt(now);
                oferta.setUpdatedAt(now);
                return ofertaRepository.save(oferta)
                    .map(OfertaMapper::toDTO);
            });
    }
    @Override
    public Mono<OfertaDTO> obtener(Long id) {
        return ofertaRepository.findById(id)
            .switchIfEmpty(Mono.error(new OfertaNotFoundException("Oferta no encontrada")))
            .map(OfertaMapper::toDTO);
    }
    @Override
    public Flux<OfertaDTO> listar(Categoria categoria) {
        if (categoria != null) {
            return ofertaRepository.findByCategoria(categoria)
                .map(OfertaMapper::toDTO);
        } else {
            return ofertaRepository.findAll()
                .map(OfertaMapper::toDTO);
        }
    }
    @Override
    public Mono<OfertaDTO> actualizarParcial(Long id, UpdateOfertaDTO patch) {
        return ofertaRepository.findById(id)
            .switchIfEmpty(Mono.error(new OfertaNotFoundException("Oferta no encontrada")))
            .flatMap(existing -> {
                if (patch.getTitulo() != null && !patch.getTitulo().equals(existing.getTitulo())) {
                    return ofertaRepository.existsByProviderIdAndTitulo(existing.getProviderId(), patch.getTitulo())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new DuplicateTituloException("El título ya existe"));
                            }
                            OfertaMapper.applyPatch(existing, patch);
                            if (patch.getTitulo() != null) {
                                existing.setSlug(SlugUtil.slugify(patch.getTitulo()));
                            }
                            return ofertaRepository.save(existing)
                                .map(OfertaMapper::toDTO);
                        });
                } else {
                    OfertaMapper.applyPatch(existing, patch);
                    return ofertaRepository.save(existing)
                        .map(OfertaMapper::toDTO);
                }
            });
    }
    @Override
    public Mono<Void> eliminar(Long id) {
        return ofertaRepository.findById(id)
            .switchIfEmpty(Mono.error(new OfertaNotFoundException("Oferta no encontrada")))
            .flatMap(existing -> ofertaRepository.deleteById(existing.getId()));
    }

}
