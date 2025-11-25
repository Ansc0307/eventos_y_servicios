@echo off
echo ==========================================
echo Compilando todos los microservicios...
echo ==========================================

rem Guardar la ruta base actual (eventos_y_servicios)
set "BASE_DIR=%cd%"

setlocal enabledelayedexpansion

rem Buscar un Maven Wrapper disponible en alguno de los módulos
set "WRAPPER_CMD="
for %%w in (
    ms-usuarios
    ms-notifications
    ms-reservas
    ms-ofertas
) do (
    if exist "%BASE_DIR%\%%w\mvnw.cmd" (
        set "WRAPPER_CMD=%BASE_DIR%\%%w\mvnw.cmd"
        goto :FOUND_WRAPPER
    )
)

:FOUND_WRAPPER
if "%WRAPPER_CMD%"=="" (
    echo No se encontro mvnw.cmd en modulos conocidos. Intentando usar mvn del sistema...
    set "WRAPPER_CMD=mvn"
)

for %%d in (
    config-server
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
    call "%WRAPPER_CMD%" -f "%BASE_DIR%\%%d\pom.xml" clean package -DskipTests
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
