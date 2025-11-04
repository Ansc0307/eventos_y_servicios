package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.TipoNotificacion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TipoNotificacionRepository extends ReactiveCrudRepository<TipoNotificacion, Long> {

    // Derived Query
    Mono<Boolean> existsByNombreIgnoreCase(String nombre);

    // Query nativa
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tipo_notificacion WHERE LOWER(nombre) = LOWER(:nombre)")
    Mono<Boolean> existePorNombre(String nombre);
}
