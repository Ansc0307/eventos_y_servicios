package com.eventos.ms_reservas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // Permitir Swagger y OpenAPI públicamente
            .requestMatchers(
    "/openapi/**",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/ms-reservas/openapi/**"
).permitAll()

            .anyRequest().authenticated()
        )
        // JWT para APIs
        .oauth2ResourceServer(oauth2 -> oauth2.jwt())
        // Deshabilitar CSRF solo para Swagger (opcional)
        .csrf(csrf -> csrf.ignoringRequestMatchers("/v3/api-docs/**", "/swagger-ui/**"));
    return http.build();
}

}
