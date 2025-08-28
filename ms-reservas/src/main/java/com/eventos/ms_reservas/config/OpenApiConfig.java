package com.eventos.ms_reservas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Value("${api.common.version:1.0.0}")
    String apiVersion;
    @Value("${api.common.title:API Reservas}")
    String apiTitle;
    @Value("${api.common.description:Documentación de la API de reservas}")
    String apiDescription;
    @Value("${api.common.termsOfService:https://www.miempresa.com/terminos}")
    String apiTermsOfService;
    @Value("${api.common.license:Licencia MIT}")
    String apiLicense;
    @Value("${api.common.licenseUrl:https://opensource.org/licenses/MIT}")
    String apiLicenseUrl;
    @Value("${api.common.externalDocDesc:Documentación en Wiki}")
    String apiExternalDocDesc;
    @Value("${api.common.externalDocUrl:https://www.miempresa.com/wiki}")
    String apiExternalDocUrl;
    @Value("${api.common.contact.name:Soporte Mi Empresa}")
    String apiContactName;
    @Value("${api.common.contact.url:https://www.miempresa.com/contacto}")
    String apiContactUrl;
    @Value("${api.common.contact.email:soporte@miempresa.com}")
    String apiContactEmail;

    /**
     * Configuración de OpenAPI para Swagger UI.
     * Accesible en $HOST:$PORT/openapi/swagger-ui.html
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