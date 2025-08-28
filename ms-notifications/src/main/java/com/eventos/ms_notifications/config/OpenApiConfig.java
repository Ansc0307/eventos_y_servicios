package com.eventos.ms_notifications.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Notificaciones",
        version = "1.0",
        description = "Documentación de la API para gestión de notificaciones"
    )
)
public class OpenApiConfig {
}
