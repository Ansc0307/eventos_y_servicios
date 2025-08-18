package com.eventos.notificaciones.controller;

import com.eventos.notificaciones.dto.Notificacion;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notification")
public class NotificacionController {

    @GetMapping("/{notificationId}")
    public Mono<Notificacion> obtenerNotificacion(@PathVariable String notificationId) {
        return Mono.just(
            new Notificacion(
                "1",
                "Nueva notificacion",
                "Tienes una nueva notificación"
            )
        );
    }
}
