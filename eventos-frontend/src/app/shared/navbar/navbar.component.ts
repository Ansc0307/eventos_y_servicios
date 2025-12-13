import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true, // ← Hacerlo standalone
  imports: [CommonModule, RouterLink, RouterLinkActive], // ← Agregar imports
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  isLoggedIn = false;
  
  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    try {
      this.isLoggedIn = await this.keycloakService.isLoggedIn();
    } catch {
      this.isLoggedIn = false;
    }
  }

  login() {
    // Para DEV: evita que al “iniciar sesión” vuelva a entrar automáticamente con la última sesión/usuario.
    // prompt=login fuerza a mostrar pantalla de login aunque haya SSO.
    const redirectUri = window.location.origin + '/dashboard';
    console.info('[navbar] login()', { redirectUri });
    this.keycloakService.login({ redirectUri, prompt: 'login' });
  }

  async logout(): Promise<void> {
    // Importante: si vuelves a una ruta protegida (p.ej. /dashboard), el guard
    // disparará login de nuevo y parece un "loop". Volvemos a la landing pública.
    this.isLoggedIn = false;

    const redirectUri = window.location.origin + '/';
    console.info('[navbar] logout() -> Keycloak end-session', { redirectUri });

    try {
      const kc = this.keycloakService.getKeycloakInstance();
      await kc.logout({ redirectUri });
    } catch {
      // Fallback: al menos limpia token local y vuelve a la landing.
      try {
        const kc = this.keycloakService.getKeycloakInstance();
        kc.clearToken();
      } catch {
        // ignore
      }
      window.location.href = redirectUri;
    }
  }
}