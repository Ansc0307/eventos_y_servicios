package com.eventos.ms_usuarios.config;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/openapi/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()
            .requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/usuarios/**").hasAnyRole("ADMIN","ORGANIZADOR")
            .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasAnyRole("ADMIN","ORGANIZADOR")
            .requestMatchers(HttpMethod.PATCH, "/usuarios/**").hasAnyRole("ADMIN","ORGANIZADOR")
            .anyRequest().authenticated())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              String msg = "No autenticado: envíe un Bearer token válido";
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                  "Bearer error=\"unauthorized\", error_description=\"" + msg + "\"");
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              String body = String.format(
                  "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                  java.time.Instant.now().toString(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", msg,
                  request.getRequestURI());
              response.getWriter().write(body);
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              String msg = "Acceso denegado: requiere rol ORGANIZADOR para esta operación";
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                  "Bearer error=\"insufficient_scope\", error_description=\"" + msg + "\"");
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              String body = String.format(
                  "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                  java.time.Instant.now().toString(), HttpStatus.FORBIDDEN.value(), "Forbidden", msg,
                  request.getRequestURI());
              response.getWriter().write(body);
            }))
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakAuthoritiesConverter())));
    return http.build();
  }

  /**
   * Convierte roles de Keycloak (realm_access.roles y resource_access.*.roles)
   * en authorities con prefijo ROLE_ para Spring Security.
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
      if (realmAccess != null) {
        Object rolesObj = realmAccess.get("roles");
        if (rolesObj instanceof List<?> rl) {
          realmRoles = rl.stream().map(Object::toString).toList();
        }
      }

      // Roles a nivel de cliente (resource_access)
      List<String> clientRoles = List.of();
      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      if (resourceAccess != null) {
        for (Object v : resourceAccess.values()) {
          if (v instanceof Map<?, ?> client) {
            Object roles = client.get("roles");
            if (roles instanceof List<?> rl) {
              clientRoles = Stream.concat(clientRoles.stream(), rl.stream().map(Object::toString)).toList();
            }
          }
        }
      }

      Set<GrantedAuthority> authorities = new HashSet<>();
      realmRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
      clientRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
      Collection<GrantedAuthority> scopeAuthorities = scopeConverter.convert(jwt);
      if (scopeAuthorities != null)
        authorities.addAll(scopeAuthorities);
      return authorities;
    });
    return converter;
  }
}
