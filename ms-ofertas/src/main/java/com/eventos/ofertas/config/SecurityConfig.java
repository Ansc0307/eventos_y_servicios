package com.eventos.ofertas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configura el convertidor de JWT para que Spring Security entienda los roles de Keycloak.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(@Value("${keycloak.client-id:api-ms}") String clientId) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter(clientId));
        return converter;
    }

    /**
     * Define la cadena de filtros de seguridad que protege todos los endpoints.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtConverter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
            .requestMatchers(
                "/actuator/health", "/actuator/info",
                "/swagger-ui.html", "/swagger-ui/**",
                "/v3/api-docs/**", "/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
            )
            .anonymous(anon -> anon.disable()) // 👈 SIN token => 401 seguro
            .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
            );

        return http.build();
    }
}