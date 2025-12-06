#!/bin/bash

echo "=========================================="
echo "Compilando todos los microservicios..."
echo "=========================================="

# Guardar ruta base
BASE_DIR=$(pwd)

# Buscar un Maven Wrapper disponible
WRAPPER_CMD=""

for w in \
    config-server \
    eureka-server \
    ms-edge-server \
    ms-usuarios \
    ms-notifications \
    ms-reservas \
    ms-ofertas
do
    if [[ -f "$BASE_DIR/$w/mvnw" ]]; then
        WRAPPER_CMD="$BASE_DIR/$w/mvnw"
        break
    fi
done

if [[ -z "$WRAPPER_CMD" ]]; then
    echo "No se encontró mvnw en los módulos. Usando mvn del sistema..."
    WRAPPER_CMD="mvn"
fi

# Compilar cada módulo
for d in \
    config-server \
    eureka-server \
    ms-edge-server \
    ms-usuarios \
    ms-notifications \
    ms-reservas \
    ms-ofertas
do
    echo "------------------------------------------"
    echo "Compilando $d ..."
    echo "------------------------------------------"

    "$WRAPPER_CMD" -f "$BASE_DIR/$d/pom.xml" clean package -DskipTests
    if [[ $? -ne 0 ]]; then
        echo "❌ Error al compilar $d"
        exit 1
    fi
done

echo "=========================================="
echo "✅ Todos los proyectos se compilaron correctamente."
echo "=========================================="
