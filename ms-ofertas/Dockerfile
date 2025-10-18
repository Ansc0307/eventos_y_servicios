# Etapa 1: Construcción del JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia pom.xml y descarga dependencias (capa cacheable)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el resto del código y construye el jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final con el JAR
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar el JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8083

# Comando para ejecutar
ENTRYPOINT ["java", "-jar", "app.jar"]
