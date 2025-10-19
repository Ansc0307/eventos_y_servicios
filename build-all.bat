@echo off
echo ==========================================
echo Compilando todos los microservicios...
echo ==========================================

rem Guardar la ruta base actual (eventos_y_servicios)
set "BASE_DIR=%cd%"

setlocal enabledelayedexpansion

for %%d in (
    eureka-server
    ms-edge-server
    ms-notifications
    ms-ofertas
    ms-reservas
    ms-usuarios
) do (
    echo ------------------------------------------
    echo Compilando %%d ...
    echo ------------------------------------------
    cd "%BASE_DIR%\%%d"
    mvn clean package -DskipTests
    if errorlevel 1 (
        echo ❌ Error al compilar %%d
        pause
        exit /b 1
    )
)

echo ==========================================
echo ✅ Todos los proyectos se compilaron correctamente.
echo ==========================================
pause
