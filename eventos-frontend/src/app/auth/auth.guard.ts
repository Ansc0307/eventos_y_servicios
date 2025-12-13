import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

/**
 * Protege rutas privadas: si no hay sesión, redirige a Keycloak.
 * Mantiene el comportamiento previo (login obligatorio) pero permitiendo rutas públicas (p.ej. /registro).
 */
export const authGuard: CanActivateFn = async (_route, state) => {
  const keycloak = inject(KeycloakService);

  // Si estamos volviendo desde Keycloak con code/state/session_state, NO dispares login otra vez.
  // OJO: dependiendo de config/versión, Keycloak puede devolver esos parámetros en query (?..) o en hash (#..).
  // Angular state.url normalmente NO incluye el hash, por eso el loop ocurre (login -> callback -> guard -> login ...).
  const url = state.url || '';
  const href = window.location.href || '';
  const search = window.location.search || '';
  const hash = window.location.hash || '';

  const isOidcCallback =
    href.includes('code=') ||
    href.includes('session_state=') ||
    href.includes('state=') ||
    search.includes('code=') ||
    search.includes('session_state=') ||
    search.includes('state=') ||
    hash.includes('code=') ||
    hash.includes('session_state=') ||
    hash.includes('state=');

  if (isOidcCallback) {
    console.info('[authGuard] OIDC callback detectado; se permite navegación sin login()', {
      url,
      search,
      hash
    });
    return true;
  }

  const loggedIn = await keycloak.isLoggedIn();
  if (loggedIn) return true;

  // Evita incluir fragmentos (#...) en el redirectUri.
  const cleanPath = (url || '/').split('#')[0].split('?')[0];
  const redirectUri = window.location.origin + cleanPath;

  console.info('[authGuard] No hay sesión; redirigiendo a login()', { redirectUri, requestedUrl: url });
  await keycloak.login({ redirectUri });
  return false;
};