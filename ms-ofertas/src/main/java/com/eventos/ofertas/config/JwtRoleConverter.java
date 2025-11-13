package com.eventos.ofertas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final Logger log = LoggerFactory.getLogger(JwtRoleConverter.class);

  /** Debe coincidir con tu application.properties: keycloak.client-id=api-ms */
  private final String apiClientId;

  /** Para conservar las autoridades por defecto (SCOPE_*) */
  private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

  public JwtRoleConverter(@Value("${keycloak.client-id:api-ms}") String apiClientId) {
    this.apiClientId = apiClientId;
  }

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    // 1) Autoridades por defecto (SCOPE_... de "scope"/"scp")
    Set<GrantedAuthority> authorities = new HashSet<>(defaultConverter.convert(jwt));

    // 2) Roles de realm
    Set<String> roles = new HashSet<>();
    Map<String, Object> realmAccess = jwt.getClaim("realm_access");
    if (realmAccess != null) {
      Object rr = realmAccess.get("roles");
      if (rr instanceof Collection<?> col) {
        col.forEach(r -> roles.add(String.valueOf(r)));
      }
    }

    // 3) Roles del cliente específico de la API (api-ms por defecto)
    Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
    if (resourceAccess != null) {
      Object client = resourceAccess.get(apiClientId);
      if (client instanceof Map<?, ?> m) {
        Object cr = m.get("roles");
        if (cr instanceof Collection<?> col) {
          col.forEach(r -> roles.add(String.valueOf(r)));
        }
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("JWT roles (realm + client:{}): {}", apiClientId, roles);
    }

    // 4) Normalizar y prefijar ROLE_
    Set<SimpleGrantedAuthority> roleAuthorities = roles.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        // normaliza a mayúsculas para que hasRole('USER') machee sin sorpresas
        .map(s -> s.replace(' ', '_').toUpperCase(Locale.ROOT))
        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    authorities.addAll(roleAuthorities);
    return authorities;
  }
}
