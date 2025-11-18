package com.eventos.ms_notifications.config;

import com.eventos.ms_notifications.model.Prioridad;
import com.eventos.ms_notifications.model.TipoNotificacion;
import com.eventos.ms_notifications.repository.PrioridadRepository;
import com.eventos.ms_notifications.repository.TipoNotificacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initPrioridades(PrioridadRepository prioridadRepository) {
        return args -> {
            prioridadRepository.count()
                .flatMap(count -> {
                    if (count == 0) {
                        return Mono.when(
                                prioridadRepository.save(new Prioridad(null, "BAJA", "Prioridad baja")),
                                prioridadRepository.save(new Prioridad(null, "MEDIA", "Prioridad media")),
                                prioridadRepository.save(new Prioridad(null, "ALTA", "Prioridad alta"))
                        ).doOnSuccess(v -> System.out.println("Datos iniciales insertados en tabla prioridad"));
                    } else {
                        System.out.println("Tabla prioridad ya contiene datos, no se insertan nuevos registros");
                        return Mono.empty();
                    }
                })
                .subscribe();
        };
    }

    @Bean
    CommandLineRunner initTiposNotificacion(TipoNotificacionRepository tipoNotificacionRepository) {
        return args -> {
            tipoNotificacionRepository.count()
                .flatMap(count -> {
                    if (count == 0) {
                        return Mono.when(
                                tipoNotificacionRepository.save(new TipoNotificacion(null, "INFORMATIVA", "Notificación informativa para el usuario")),
                                tipoNotificacionRepository.save(new TipoNotificacion(null, "ALERTA", "Notificación de alerta importante")),
                                tipoNotificacionRepository.save(new TipoNotificacion(null, "RECORDATORIO", "Recordatorio de eventos o tareas")),
                                tipoNotificacionRepository.save(new TipoNotificacion(null, "PROMOCION", "Promoción o publicidad")),
                                tipoNotificacionRepository.save(new TipoNotificacion(null, "SISTEMA", "Notificación del sistema o administrativa"))
                        ).doOnSuccess(v -> System.out.println("Datos iniciales insertados en tabla tipo_notificacion"));
                    } else {
                        System.out.println("Tabla tipo_notificacion ya contiene datos, no se insertan nuevos registros");
                        return Mono.empty();
                    }
                })
                .subscribe();
        };
    }
}
