package com.eventos.ofertas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${api.common.title}")
    private String title;

    @Value("${api.common.description}")
    private String description;

    @Value("${api.common.version}")
    private String version;

    @Value("${api.common.termsOfService}")
    private String termsOfService;

    @Value("${api.common.license}")
    private String licenseName;

    @Value("${api.common.licenseUrl}")
    private String licenseUrl;

    @Value("${api.common.contact.name}")
    private String contactName;

    @Value("${api.common.contact.url}")
    private String contactUrl;

    @Value("${api.common.contact.email}")
    private String contactEmail;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .termsOfService(termsOfService)
                        .license(new License().name(licenseName).url(licenseUrl))
                        .contact(new Contact().name(contactName).url(contactUrl).email(contactEmail)));
    }
}