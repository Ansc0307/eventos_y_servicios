package com.eventos.ms_usuarios.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakUserInfoService {

  @Value("${keycloak.admin.base-url}")
  private String keycloakBaseUrl;

  @Value("${keycloak.admin.realm}")
  private String realm;

  private final RestTemplate restTemplate = new RestTemplate();

  public Optional<Boolean> getEmailVerified(String accessToken) {
    if (accessToken == null || accessToken.isBlank()) {
      return Optional.empty();
    }

    String url = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/userinfo";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    try {
      ResponseEntity<Map> res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
      Map body = res.getBody();
      if (body == null) {
        return Optional.empty();
      }

      Object value = body.get("email_verified");
      if (value instanceof Boolean b) {
        return Optional.of(b);
      }
      if (value instanceof String s) {
        return Optional.of(Boolean.parseBoolean(s));
      }
      return Optional.empty();

    } catch (RestClientException ex) {
      return Optional.empty();
    }
  }
}
