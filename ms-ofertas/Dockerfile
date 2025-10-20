# Fase de construcción
# Usamos una imagen con JRE 21 de Alpine Linux para la primera etapa.
FROM eclipse-temurin:21-jre-alpine AS builder

# Establecemos el directorio de trabajo donde se extraerán las capas.
WORKDIR /extracted

# Definimos un argumento para el archivo JAR, usando el patrón de tu proyecto.
ARG JAR_FILE=target/ms-ofertas-*.jar

# Copiamos el archivo JAR a la imagen.
COPY ${JAR_FILE} app.jar

# Usamos las layertools de Spring Boot para extraer la aplicación en capas.
# Esto mejora el almacenamiento en caché de las dependencias.
RUN java -Djarmode=layertools -jar app.jar extract

# Fase final
# Usamos la misma imagen ligera para la versión final.
FROM eclipse-temurin:21-jre-alpine

# Establecemos el directorio de trabajo final de la aplicación.
WORKDIR /application

# Creamos un grupo y un usuario 'spring' sin privilegios por seguridad.
RUN addgroup -S spring && adduser -S spring -G spring
# Cambiamos al nuevo usuario.
USER spring:spring

# Copiamos las capas extraídas desde la fase de construcción.
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

# Exponemos el puerto original de tu aplicación.
EXPOSE 8083

# El punto de entrada para ejecutar la aplicación desde las capas extraídas.
ENTRYPOINT ["java", "-cp","/application","org.springframework.boot.loader.launch.JarLauncher"]
