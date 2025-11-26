import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true, // ← Hacerlo standalone
  imports: [CommonModule, RouterLink], // ← Agregar imports
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userName: string = '';

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit() {
    const userProfile = await this.keycloakService.loadUserProfile();
    this.userName = userProfile.firstName || 'Usuario';
  }
}