package com.eventos.ofertas.config;

import com.eventos.ofertas.entity.Categoria;
import com.eventos.ofertas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class CategoriaSeeder implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(CategoriaSeeder.class);

  private final CategoriaRepository categoriaRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    // Solo asegurar que existan algunas categorías base.
    // Esto NO borra datos; solo inserta las que falten.
    List<String> defaults = List.of(
        "salón",
        "fotografia",
        "catering",
        "música",
        "decoración");

    int created = 0;
    for (String detalle : defaults) {
      if (categoriaRepository.existsByDetalle(detalle)) {
        continue;
      }
      Categoria categoria = new Categoria();
      categoria.setDetalle(detalle);
      categoriaRepository.save(categoria);
      created++;
    }

    if (created > 0) {
      log.info("CategoriaSeeder: creadas {} categorías por defecto", created);
    } else {
      log.info("CategoriaSeeder: no se crearon categorías (ya existían)");
    }
  }
}
