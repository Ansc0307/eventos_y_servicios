# Usamos una imagen base oficial de Java con JDK 17 (o la versión que uses)
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar construido al contenedor
COPY target/ms-edge-server-0.0.1-SNAPSHOT.jar edge-server.jar

# Exponemos el puerto 8080 (igual que en application.yml)
EXPOSE 8080

# Comando para ejecutar la app Spring Boot
ENTRYPOINT ["java", "-jar", "edge-server.jar"]
