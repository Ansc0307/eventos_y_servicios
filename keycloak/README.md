This folder holds realm configuration for the shared Keycloak instance used by the root docker-compose stack.

realm-eventos.json is copied from ms-usuarios/keycloak/realm-eventos.json
The root docker-compose mounts it into Keycloak with --import-realm in dev mode