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

## Keycloak SMTP (emails de verificación)

El envío de emails (verificación, reset password, etc.) lo hace Keycloak. El backend (`ms-usuarios`) solo dispara la acción vía Admin REST (`execute-actions-email`).

En este repo se configuró un servicio `keycloak-init` en [docker-compose.yml](docker-compose.yml) que, al levantar el stack, aplica la configuración SMTP al realm `eventos` usando las variables de `.env`.

### Opción recomendada (más fácil): SMTP local con Mailpit (Docker)

Esta es la forma más simple y estable para desarrollo: no requiere Gmail, 2FA, App Password, ni servicios externos. Captura los emails en una UI local.

1) Levanta el stack (incluye Mailpit):
- `docker compose up -d --build`

2) Configura `.env` así (mínimo funcional con Mailpit):
```
KEYCLOAK_SMTP_HOST=mailpit
KEYCLOAK_SMTP_PORT=1025
KEYCLOAK_SMTP_FROM=no-reply@eventos.local
KEYCLOAK_SMTP_FROM_DISPLAY_NAME=Eventos
KEYCLOAK_SMTP_AUTH=false
KEYCLOAK_SMTP_STARTTLS=false
KEYCLOAK_SMTP_SSL=false
```

3) Aplica la configuración SMTP al realm (sin tumbar todo):
- `docker compose up -d keycloak-init --force-recreate`
- `docker logs -f keycloak-init`

4) Abre la bandeja:
- UI Mailpit: `http://localhost:8025`

Con esto, cuando registres un usuario y se dispare `execute-actions-email`, el correo debe aparecer en Mailpit.

### ¿Para qué sirve el link de verificación?

El link es un “action link” generado por Keycloak (incluye un token temporal). Sirve para:
- Confirmar que el usuario **tiene acceso** a ese correo.
- Marcar el usuario en Keycloak como `emailVerified=true`.

Cómo sabes que sí funcionó:
- Abres el correo en Mailpit y haces click en el link.
- Keycloak muestra una pantalla tipo “Account updated / Email verified”.
- Si luego entras al Admin Console, el usuario aparece con **Email verified = true**.

Nota: hoy tu BD guarda `activo=false` hasta que implementemos el paso de “activación” (pendiente: `/usuarios/me`). Keycloak ya puede quedar verificado aunque la BD todavía no se actualice.

### Habilitar SMTP con Gmail

Requisitos (Gmail):
- Tener 2FA activado.
- Crear un **App Password** (Google ya no permite “less secure apps”).

Pasos (Gmail):
1) En tu cuenta de Google activa 2FA.
2) Genera un App Password: Google Account → Security → “App passwords” → crea uno para “Mail”.
3) Edita `.env` (NO lo subas a Git) y llena las variables SMTP.

En `.env` completa:
- `KEYCLOAK_SMTP_HOST=smtp.gmail.com`
- `KEYCLOAK_SMTP_PORT=587`
- `KEYCLOAK_SMTP_FROM=tu_correo@gmail.com` (recomendado que sea el mismo que el user)
- `KEYCLOAK_SMTP_FROM_DISPLAY_NAME=Eventos` (opcional)
- `KEYCLOAK_SMTP_USER=tu_correo@gmail.com`
- `KEYCLOAK_SMTP_PASSWORD=TU_APP_PASSWORD` (NO es tu password normal)
- `KEYCLOAK_SMTP_STARTTLS=true`
- `KEYCLOAK_SMTP_SSL=false`
- `KEYCLOAK_SMTP_AUTH=true`

Ejemplo:
```
KEYCLOAK_SMTP_HOST=smtp.gmail.com
KEYCLOAK_SMTP_PORT=587
KEYCLOAK_SMTP_FROM=midireccion@gmail.com
KEYCLOAK_SMTP_FROM_DISPLAY_NAME=Eventos
KEYCLOAK_SMTP_USER=midireccion@gmail.com
KEYCLOAK_SMTP_PASSWORD=abcd efgh ijkl mnop
KEYCLOAK_SMTP_STARTTLS=true
KEYCLOAK_SMTP_SSL=false
KEYCLOAK_SMTP_AUTH=true
```

Aplicar cambios (sin borrar nada):
- Si ya tenías los contenedores arriba y solo cambiaste `.env`, ejecuta:
	- `docker compose up -d keycloak-init --force-recreate`
	- `docker logs -f keycloak-init`
	Debes ver: `SMTP configured for realm 'eventos' ...`

Luego reinicia el stack:
- `docker compose up -d --build`

Si `KEYCLOAK_SMTP_HOST` está vacío, el init no configura SMTP y Keycloak no enviará correos.

### Cambiar entre Mailpit (local) y Gmail (Internet)

- Para DEV estable: deja Mailpit (`KEYCLOAK_SMTP_HOST=mailpit`) y revisa correos en `http://localhost:8025`.
- Para probar entrega real por Internet: cambia `.env` a Gmail (smtp.gmail.com + App Password) y ejecuta:
	- `docker compose up -d keycloak-init --force-recreate`
	Luego registra un usuario con tu correo real y revisa Inbox/Spam.

### Troubleshooting (Gmail)

- Error típico `535-5.7.8 Username and Password not accepted`:
	- Estás usando el password normal o no tienes 2FA + App Password.
- Si no llega el correo:
	- Revisa logs: `docker logs keycloak` y `docker logs keycloak-init`.
	- Asegúrate de registrar un usuario con un email real (no `@eventos.local`).

### Alternativas a Gmail (recomendadas para dev)

- Mailtrap (SMTP “sandbox”): te da host/puerto/user/pass y ves los correos en su UI.
- testmail.app (GitHub Education): similar, no dependes de Gmail.

En ambos casos solo reemplazas `KEYCLOAK_SMTP_HOST/PORT/USER/PASSWORD/FROM` con los datos del proveedor.