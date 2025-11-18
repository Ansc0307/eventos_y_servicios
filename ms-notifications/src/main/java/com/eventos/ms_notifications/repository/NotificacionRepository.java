package com.eventos.ms_notifications.repository;

import com.eventos.ms_notifications.model.Notificacion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface NotificacionRepository extends ReactiveCrudRepository<Notificacion, Long> {
    // Derived Query
    Flux<Notificacion> findByUserId(Long userId);

    // Query nativa (opcional, si necesitas personalizar)
    @Query("SELECT * FROM notificacion WHERE user_id = :userId")
    Flux<Notificacion> buscarPorUserId(Long userId);
}
