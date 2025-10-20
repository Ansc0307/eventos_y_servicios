package com.eventos.ms_reservas.config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

import com.fasterxml.jackson.databind.ObjectMapper;

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
                .requestMatchers(HttpMethod.GET, "/v1/reservas/**", "/solicitudes/**", "/no-disponibilidades/**").authenticated()

                // Crear reservas: ORGANIZADOR o ADMIN
                .requestMatchers(HttpMethod.POST, "/v1/reservas/**").hasAnyRole("ORGANIZADOR", "ADMIN")

                // Crear solicitudes: ORGANIZADOR o ADMIN
                .requestMatchers(HttpMethod.POST, "/solicitudes/**").hasAnyRole("ORGANIZADOR", "ADMIN")

                // Crear no disponibilidades: solo ADMIN
                .requestMatchers(HttpMethod.POST, "/no-disponibilidades/**").hasRole("ADMIN")

                // Eliminar recursos (ADMIN u ORGANIZADOR)
                .requestMatchers(HttpMethod.DELETE, "/v1/reservas/**", "/solicitudes/**").hasAnyRole("ADMIN", "ORGANIZADOR")

                // Eliminar no disponibilidad (solo ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/no-disponibilidades/**").hasRole("ADMIN")

                // Cualquier otra petición autenticada
                .anyRequest().authenticated()
            )
            // Manejo de errores (401 y 403) — devolver JSON consistente con RestExceptionHandler
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        String path = request.getRequestURI();
                        Map<String, Object> body = new HashMap<>();
                        body.put("timestamp", Instant.now().toString());
                        body.put("status", HttpStatus.UNAUTHORIZED.value());
                        body.put("error", "Unauthorized");
                        body.put("message", "No autenticado: envíe un token Bearer válido");
                        body.put("path", path);
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        ObjectMapper om = new ObjectMapper();
                        om.writeValue(response.getWriter(), body);
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        String path = request.getRequestURI();
                        Map<String, Object> body = new HashMap<>();
                        body.put("timestamp", Instant.now().toString());
                        body.put("status", HttpStatus.FORBIDDEN.value());
                        body.put("error", "Forbidden");
                        body.put("message", "Acceso denegado: no tiene permisos");
                        body.put("path", path);
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        ObjectMapper om = new ObjectMapper();
                        om.writeValue(response.getWriter(), body);
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
