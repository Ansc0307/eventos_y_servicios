import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
//import { ReservasListComponent } from './reservas/reservas-list.component';
//import { SolicitudesListComponent } from './solicitudes/solicitudes-list.component';
//import { UsuariosDashboardComponent } from './usuarios/usuarios-dashboard.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { UsuariosService } from './services/usuarios.service';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakEventType } from 'keycloak-angular';
import { environment } from '../environments/environment.dev';

@Component({
  selector: 'app-root',
  standalone: true, // ← asegurarse que esto esté
  imports: [
    CommonModule,
    RouterOutlet, 
    NavbarComponent,    
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit {
  title = 'eventos-frontend';
  showNavbar = false;

  constructor(
    private usuariosService: UsuariosService,
    private keycloak: KeycloakService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const syncNavbar = (url: string) => {
      const path = url.split('?')[0];
      const isPublic = path === '/' || path === '/registro';
      this.showNavbar = !isPublic;
    };

    syncNavbar(this.router.url);
    this.router.events.subscribe((e) => {
      if (e instanceof NavigationEnd) {
        syncNavbar(e.urlAfterRedirects);
      }
    });

    // Logs para depurar loops de login/logout.
    if (!environment.production) {
      try {
        this.keycloak.keycloakEvents$.subscribe({
          next: (e) => {
            // e.type es un enum: KeycloakEventType.*
            console.info('[keycloak-event]', e.type, e);
          },
          error: (err) => console.warn('[keycloak-event] error', err)
        });
      } catch {
        // ignore
      }
    }

    // HU_5: solo si hay sesión, fuerza sincronización de verificación a BD.
    void (async () => {
      try {
        const loggedIn = await this.keycloak.isLoggedIn();
        console.info('[App] keycloak.isLoggedIn()', loggedIn, {
          url: window.location.href,
          pathname: window.location.pathname,
          search: window.location.search,
          hash: window.location.hash
        });

        if (loggedIn) {
          this.usuariosService.me().subscribe({ next: () => {}, error: () => {} });
        }
      } catch (e) {
        console.warn('[App] isLoggedIn() failed', e);
      }
    })();
  }
}

