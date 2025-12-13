import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  constructor(private keycloak: KeycloakService) {}

  login(): void {
    this.keycloak.login({ redirectUri: window.location.origin + '/dashboard' });
  }
}
