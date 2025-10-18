package com.eventos.ofertas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desarrollo");
        
        Contact contact = new Contact();
        contact.setEmail("contacto@ofertas.com");
        contact.setName("Equipo de Ofertas");
        contact.setUrl("https://www.ofertas.com");
        
        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");
        
        Info info = new Info()
                .title("API de Microservicio de Ofertas")
                .version("1.0.0")
                .contact(contact)
                .description("API RESTful para gestionar ofertas, categorías, descuentos, medias y reseñas. " +
                        "Permite crear, actualizar, consultar y eliminar ofertas con sus relaciones.")
                .termsOfService("https://www.ofertas.com/terms")
                .license(mitLicense);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}