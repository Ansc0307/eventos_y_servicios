# eventos_y_servicios

## Configuración de entorno (.env)

Copiar el archivo `.env.example` a `.env` y ajustar valores sensibles (no se versiona `.env`).

Variables actuales:
```
CONFIG_SERVER_USER=config
CONFIG_SERVER_PASSWORD=config
```
Se usan para la autenticación Basic del Config Server (puerto 8888). El endpoint `/actuator/health` permanece abierto.

## Criterio 1 (resumen)
Config Server funcional: responde `/ms-usuarios/default` con propertySources y protegido con Basic Auth.

## Criterio 2 (resumen)
Configuraciones externalizadas en repo Git `eventos-config-repo`.
Se evidencia el hash de commit en el campo `version` al llamar `http://localhost:8888/ms-usuarios/default`.

## Criterio 3 (configuración global y específica)
Se utiliza `application.yml` global + archivos específicos por microservicio:
- `ms-usuarios.yml`
- `eureka-server.yml`
- `edge-server.yml`

Evidencias vía Config Server (Basic Auth `config:config`):
- `GET http://localhost:8888/ms-usuarios/default` -> propertySources: primero `ms-usuarios.yml`, luego `application.yml`.
- `GET http://localhost:8888/eureka-server/default` -> propertySources: `eureka-server.yml` + `application.yml`.
- `GET http://localhost:8888/edge-server/default` -> propertySources: `edge-server.yml` + `application.yml`.
- `GET http://localhost:8888/ms-reservas/default` -> propertySources: `ms-reservas.yml` + `application.yml`.
- `GET http://localhost:8888/ms-ofertas/default` -> propertySources: `ms-ofertas.yml` + `application.yml`.
- `GET http://localhost:8888/ms-notifications/default` -> propertySources: `ms-notifications.yml` + `application.yml`.

Ejemplo de respuesta (edge-server): muestra `propertySources[0].name = .../edge-server.yml` y `propertySources[1].name = .../application.yml` confirmando la composición.

El gateway (`edge-server`) consume su configuración (puerto y rutas) desde el Config Server y enruta correctamente:
- `GET http://localhost:8080/usuarios/usuarios` (200 OK)
- `GET http://localhost:8080/ms-usuarios/usuarios` (200 OK)

Esto demuestra: separación de configuración, centralización y resolución dinámica.