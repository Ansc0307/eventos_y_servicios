import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private kc: KeycloakService) {}

  async isLoggedIn(): Promise<boolean> {
    return this.kc.isLoggedIn();
  }

  getUsername(): string | undefined {
    return this.kc.getKeycloakInstance().tokenParsed?.['preferred_username'];
  }

  getRoles(): string[] {
    return this.kc.getKeycloakInstance().tokenParsed?.realm_access?.roles ?? [];
  }

  // devuelve Promise<string|null>
  async getToken(): Promise<string | null> {
    try {
      const token = await this.kc.getToken();
      return token ?? null;
    } catch (e) {
      return null;
    }
  }

  login() {
    return this.kc.login();
  }

  logout() {
    return this.kc.logout(window.location.origin);
  }
}
