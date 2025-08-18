package com.eventos.notifications.controller;

import com.eventos.notifications.dto.Notification;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    @GetMapping("/{notificationId}")
    public Mono<Notification> obtenerNotificacion(@PathVariable("notificationId") String notificationId) {
        return Mono.just(
                new Notification(
                        notificationId,
                        "Nueva notificacion",
                        "Tienes una nueva notificación",
                        false
                )
        );
    }

}

