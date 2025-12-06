import { Component } from '@angular/core';
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
  
  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  logout() {
    this.keycloakService.logout();
  }
}