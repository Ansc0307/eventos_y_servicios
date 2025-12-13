import { APP_INITIALIZER } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { environment } from '../environments/environment.dev';

export function initializeKeycloakFactory(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: environment.keycloakUrl,
        realm: environment.realm,
        clientId: environment.clientId
      },
      initOptions: {
        // Permite rutas públicas (p.ej. /registro) sin forzar login.
        // Las rutas privadas se protegen con un guard.
        onLoad: 'check-sso',
        checkLoginIframe: false
      }
    });
}

export const APP_INIT_PROVIDER = {
  provide: APP_INITIALIZER,
  useFactory: initializeKeycloakFactory,
  multi: true,
  deps: [KeycloakService]
};
