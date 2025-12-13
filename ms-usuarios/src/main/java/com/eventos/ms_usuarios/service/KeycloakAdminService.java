package com.eventos.ms_usuarios.service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KeycloakAdminService {

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${keycloak.admin.base-url:http://localhost:8090}")
  private String keycloakBaseUrl;

  @Value("${keycloak.admin.realm:eventos}")
  private String targetRealm;

  @Value("${keycloak.admin.client-id:ms-usuarios-admin}")
  private String adminClientId;

  @Value("${keycloak.admin.client-secret:}")
  private String adminClientSecret;

  @Value("${keycloak.admin.frontend-client-id:angular-app}")
  private String frontendClientId;

  @Value("${keycloak.admin.verify-email-redirect-uri:http://localhost:4200/}")
  private String verifyEmailRedirectUri;

  public String getServiceAccountToken() {
    if (adminClientSecret == null || adminClientSecret.isBlank()) {
      throw new IllegalStateException(
          "keycloak.admin.client-secret no está configurado (use variable de entorno / properties)");
    }

    String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakBaseUrl, targetRealm);

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "client_credentials");
    form.add("client_id", adminClientId);
    form.add("client_secret", adminClientSecret);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    @SuppressWarnings("unchecked")
    Map<String, Object> res = restTemplate.postForObject(tokenUrl, new HttpEntity<>(form, headers), Map.class);
    if (res == null || res.get("access_token") == null) {
      throw new IllegalStateException("No se pudo obtener access_token desde Keycloak");
    }
    return res.get("access_token").toString();
  }

  public String createUser(String accessToken, String username, String email, String firstName, String lastName) {
    String url = String.format("%s/admin/realms/%s/users", keycloakBaseUrl, targetRealm);

    Map<String, Object> payload = Map.of(
        "username", username,
        "email", email,
        "firstName", firstName,
        "lastName", lastName,
        "enabled", true,
        "emailVerified", false,
        "requiredActions", List.of("VERIFY_EMAIL"));

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload, headers),
        Void.class);
    URI location = response.getHeaders().getLocation();
    if (location == null) {
      throw new IllegalStateException("Keycloak no devolvió Location al crear el usuario");
    }
    String path = location.getPath();
    return path.substring(path.lastIndexOf('/') + 1);
  }

  public void deleteUser(String accessToken, String userId) {
    String url = String.format("%s/admin/realms/%s/users/%s", keycloakBaseUrl, targetRealm, userId);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
  }

  public void setPassword(String accessToken, String userId, String password) {
    String url = String.format("%s/admin/realms/%s/users/%s/reset-password", keycloakBaseUrl, targetRealm, userId);

    Map<String, Object> payload = Map.of(
        "type", "password",
        "temporary", false,
        "value", password);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(payload, headers), Void.class);
  }

  public void assignRealmRole(String accessToken, String userId, String roleName) {
    String roleUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakBaseUrl, targetRealm, roleName);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    @SuppressWarnings("unchecked")
    Map<String, Object> roleRep = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class)
        .getBody();
    if (roleRep == null) {
      throw new IllegalStateException("No se pudo obtener representación del rol: " + roleName);
    }

    String mapUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakBaseUrl, targetRealm,
        userId);
    restTemplate.exchange(mapUrl, HttpMethod.POST, new HttpEntity<>(List.of(roleRep), headers), Void.class);
  }

  public void sendVerifyEmail(String accessToken, String userId) {
    URI uri = UriComponentsBuilder
        .fromHttpUrl(keycloakBaseUrl)
        .path(String.format("/admin/realms/%s/users/%s/execute-actions-email", targetRealm, userId))
        .queryParam("client_id", frontendClientId)
        .queryParam("redirect_uri", verifyEmailRedirectUri)
        .build()
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Body: required actions
    restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(List.of("VERIFY_EMAIL"), headers), Void.class);
  }

  public boolean isEmailAlreadyUsed(HttpClientErrorException ex) {
    // Keycloak devuelve 409 Conflict cuando email/username ya existe
    return ex.getStatusCode().value() == 409;
  }
}
