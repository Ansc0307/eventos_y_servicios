package com.eventos.ofertas.config;

import com.eventos.ofertas.repository.InMemoryOfertaRepository;
import com.eventos.ofertas.repository.OfertaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public OfertaRepository ofertaRepository() {
        return new InMemoryOfertaRepository();
    }
}