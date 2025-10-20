package com.eventos.ms_reservas.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Swagger y endpoints públicos
                .requestMatchers(
                    "/openapi/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()

                // --- Rutas ms_reservas ---
                // Solo lectura para cualquier autenticado
                .requestMatchers(HttpMethod.GET, "/reserva/**", "/solicitud/**", "/no-disponibilidad/**").authenticated()

                // Crear reservas: ORGANIZADOR o ADMIN
                .requestMatchers(HttpMethod.POST, "/reserva/**").hasAnyRole("ORGANIZADOR", "ADMIN")

                // Crear solicitudes: ORGANIZADOR o ADMIN
                .requestMatchers(HttpMethod.POST, "/solicitud/**").hasAnyRole("ORGANIZADOR", "ADMIN")

                // Crear no disponibilidades: solo ADMIN
                .requestMatchers(HttpMethod.POST, "/no-disponibilidad/**").hasRole("ADMIN")

                // Eliminar recursos (ADMIN u ORGANIZADOR)
                .requestMatchers(HttpMethod.DELETE, "/reserva/**", "/solicitud/**").hasAnyRole("ADMIN", "ORGANIZADOR")

                // Eliminar no disponibilidad (solo ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/no-disponibilidad/**").hasRole("ADMIN")

                // Cualquier otra petición autenticada
                .anyRequest().authenticated()
            )
            // Manejo de errores (401 y 403 personalizados)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    String msg = "No autenticado: envíe un token Bearer válido.";
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                        "Bearer error=\"unauthorized\", error_description=\"" + msg + "\"");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    String body = String.format(
                        "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                        java.time.Instant.now().toString(),
                        HttpStatus.UNAUTHORIZED.value(),
                        msg,
                        request.getRequestURI());
                    response.getWriter().write(body);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    String msg = "Acceso denegado: el rol no tiene permisos para esta operación.";
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                        "Bearer error=\"insufficient_scope\", error_description=\"" + msg + "\"");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    String body = String.format(
                        "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"Forbidden\",\"message\":\"%s\",\"path\":\"%s\"}",
                        java.time.Instant.now().toString(),
                        HttpStatus.FORBIDDEN.value(),
                        msg,
                        request.getRequestURI());
                    response.getWriter().write(body);
                })
            )
            // Integración con Keycloak (JWT)
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakAuthoritiesConverter()))
            );

        return http.build();
    }

    /**
     * Convierte los roles de Keycloak (realm_access y resource_access)
     * en authorities con el prefijo ROLE_.
     */
    @Bean
    JwtAuthenticationConverter keycloakAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthorityPrefix("SCOPE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Roles a nivel de realm
            List<String> realmRoles = Collections.emptyList();
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof List<?> rl) {
                realmRoles = rl.stream().map(Object::toString).toList();
            }

            // Roles a nivel de cliente (resource_access)
            List<String> clientRoles = new ArrayList<>();
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                for (Object v : resourceAccess.values()) {
                    if (v instanceof Map<?, ?> client && client.get("roles") instanceof List<?> rl) {
                        clientRoles.addAll(rl.stream().map(Object::toString).toList());
                    }
                }
            }

            Set<GrantedAuthority> authorities = new HashSet<>();
            realmRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
            clientRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));

            Collection<GrantedAuthority> scopeAuthorities = scopeConverter.convert(jwt);
            if (scopeAuthorities != null) authorities.addAll(scopeAuthorities);

            return authorities;
        });
        return converter;
    }
}
