package com.eventos.ms_notifications.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = defaultGrantedAuthoritiesConverter.convert(jwt);
        authorities.addAll(extractRealmRoles(jwt));
        return new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) return List.of();

        List<String> roles = (List<String>) realmAccess.get("roles");
        if (roles == null) return List.of();

        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());
    }
}
