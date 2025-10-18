# Multi-stage build for ms-usuarios

# 1) Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and sources
COPY pom.xml ./
COPY src ./src

# Build the application (skip tests in container)
RUN mvn -DskipTests=true clean package

# 2) Runtime stage
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Copy the fat jar from build stage
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar

# Default port (overridable via PORT)
ENV PORT=8081
EXPOSE 8081

# Default DB envs (override in compose or env)
ENV DB_URL=jdbc:postgresql://localhost:5432/usuarios_eventos_db \
    DB_USERNAME=postgres \
    DB_PASSWORD=admin

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
