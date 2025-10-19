# 1) Etapa de build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -DskipTests=true clean package

# 2) Etapa de runtime
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Usa el .jar generado por Maven
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar
# Default port (overridable via PORT)
ENV PORT=8081
EXPOSE 8081

# Default envs to work out-of-the-box hitting services on the host machine.
# - DB at host.docker.internal:5432 (published by docker compose)
# - Keycloak JWKS at host.docker.internal:8090 (avoids issuer mismatch inside container)
ENV DB_URL=jdbc:postgresql://host.docker.internal:5432/usuarios_eventos_db \
    DB_USERNAME=postgres \
    DB_PASSWORD=admin \
    SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://host.docker.internal:8090/realms/eventos/protocol/openid-connect/certs

ENTRYPOINT ["java", "-jar", "app.jar"]
