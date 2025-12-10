package com.eventos.ofertas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(JwtRoleConverter.class);

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // 1) Scopes por defecto (SCOPE_*)
        Set<GrantedAuthority> authorities = new HashSet<>(Optional.ofNullable(defaultConverter.convert(jwt)).orElse(Set.of()));

        // 2) Roles a nivel de realm
        Set<String> roles = new HashSet<>();
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            Object rr = realmAccess.get("roles");
            if (rr instanceof Collection<?> col) {
                col.forEach(r -> roles.add(String.valueOf(r)));
            }
        }

        // 3) Roles a nivel de todos los clientes (resource_access)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Object v : resourceAccess.values()) {
                if (v instanceof Map<?, ?> client) {
                    Object cr = client.get("roles");
                    if (cr instanceof Collection<?> col) {
                        col.forEach(r -> roles.add(String.valueOf(r)));
                    }
                }
            }
        }

        // 4) Normalizar a ROLE_...
        Set<SimpleGrantedAuthority> roleAuthorities = roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replace(' ', '_').toUpperCase(Locale.ROOT))
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        authorities.addAll(roleAuthorities);

        if (log.isDebugEnabled()) {
            log.debug("JWT authorities: {}", authorities);
        }

        return authorities;
    }
}
