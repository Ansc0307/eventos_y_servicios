package com.eventos.ms_usuarios.config;

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

  @Value("${api.common.title:API Usuarios}")
  private String title;

  @Value("${api.common.description:Servicio de gestión de usuarios}")
  private String description;

  @Value("${api.common.version:1.0.0}")
  private String version;

  @Value("${api.common.termsOfService:https://www.miempresa.com/terminos}")
  private String termsOfService;

  @Value("${api.common.license:Licencia MIT}")
  private String licenseName;

  @Value("${api.common.licenseUrl:https://opensource.org/licenses/MIT}")
  private String licenseUrl;

  @Value("${api.common.contact.name:Soporte Plataforma}")
  private String contactName;

  @Value("${api.common.contact.url:https://www.miempresa.com/contacto}")
  private String contactUrl;

  @Value("${api.common.contact.email:soporte@miempresa.com}")
  private String contactEmail;

  @Value("${api.common.externalDocDesc:Documentación interna}")
  private String externalDesc;

  @Value("${api.common.externalDocUrl:https://www.miempresa.com/wiki/usuarios}")
  private String externalUrl;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title(title)
            .description(description)
            .version(version)
            .termsOfService(termsOfService)
            .license(new License().name(licenseName).url(licenseUrl))
            .contact(new Contact().name(contactName).url(contactUrl).email(contactEmail)))
        .externalDocs(new ExternalDocumentation().description(externalDesc).url(externalUrl));
  }
}