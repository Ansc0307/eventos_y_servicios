package com.eventos.ms_notifications.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
@OpenAPIDefinition(
    servers = {
        @Server(
            url = "/ms-notifications",
            description = "Ruta del microservicio a través del Gateway"
        )
    }
)
public class OpenApiConfig {
    
    @Value("${api.common.version}")
    String apiVersion;

    @Value("${api.common.title}")
    String apiTitle;

    @Value("${api.common.description}")
    String apiDescription;

    @Value("${api.common.termsOfService}")
    String apiTermsOfService;

    @Value("${api.common.license}")
    String apiLicense;

    @Value("${api.common.licenseUrl}")
    String apiLicenseUrl;

    @Value("${api.common.externalDocDesc}")
    String apiExternalDocDesc;

    @Value("${api.common.externalDocUrl}")
    String apiExternalDocUrl;

    @Value("${api.common.contact.name}")
    String apiContactName;

    @Value("${api.common.contact.url}")
    String apiContactUrl;

    @Value("${api.common.contact.email}")
    String apiContactEmail;

    /**
     * Será expuesto en:
     * - SIN GW (sólo el ms) http://localhost:8087/openapi/webjars/swagger-ui/index.html (este no debería, porque en el dockerfile no se expone el puerto 8087)
     * - CON DOCKER COMPOSE: http://localhost:8080/ms-notifications/openapi/webjars/swagger-ui/index.html (a través del Gateway)
     */
    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(new Contact()
                                .name(apiContactName)
                                .url(apiContactUrl)
                                .email(apiContactEmail))
                        .termsOfService(apiTermsOfService)
                        .license(new License()
                                .name(apiLicense)
                                .url(apiLicenseUrl)))
                .externalDocs(new ExternalDocumentation()
                        .description(apiExternalDocDesc)
                        .url(apiExternalDocUrl));
    }
}
