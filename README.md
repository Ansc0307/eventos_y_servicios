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