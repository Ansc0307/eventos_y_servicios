# 1) Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# 2) Etapa de runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Usa el .jar generado por Maven
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8081
EXPOSE 8081

ENV DB_URL=jdbc:postgresql://localhost:5432/usuarios_eventos_db \
    DB_USERNAME=postgres \
    DB_PASSWORD=admin

ENTRYPOINT ["java", "-jar", "app.jar"]
