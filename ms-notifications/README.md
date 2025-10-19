Ejecutar los siguientes comandos en la terminal:

```bash
# Navegar al directorio del microservicio
cd ms-notifications

# Construir el paquete sin ejecutar los tests
mvn clean package -DskipTests

# Construir la imagen de Docker
docker build -t ms-notifications .

# Ejecutar el contenedor en el puerto 8087
docker run -p 8087:8087 ms-notifications