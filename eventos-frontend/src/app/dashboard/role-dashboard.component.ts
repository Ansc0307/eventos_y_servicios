import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { OrganizadorDashboardComponent } from './organizador-dashboard.component';
import { ProveedorDashboardComponent } from './proveedor-dashboard.component';

/**
 * Muestra el dashboard adecuado según el rol de Keycloak.
 * Roles esperados: "organizador" o "proveedor" (realm roles).
 */
@Component({
  selector: 'app-role-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, OrganizadorDashboardComponent, ProveedorDashboardComponent],
  template: `
    <ng-container *ngIf="selectedRole() as role">
      <app-organizador-dashboard *ngIf="role === 'organizador'"></app-organizador-dashboard>
      <app-proveedor-dashboard *ngIf="role === 'proveedor'"></app-proveedor-dashboard>

      <div *ngIf="role === 'none'" class="p-8 text-center">
        <h2 class="text-2xl font-semibold mb-2">Rol no asignado</h2>
        <p class="text-gray-600">No se encontró un rol válido en Keycloak. Solicita acceso al administrador.</p>
      </div>
    </ng-container>
  `
})
export class RoleDashboardComponent implements OnInit {
  private roleSignal = signal<'organizador' | 'proveedor' | 'none'>('none');
  selectedRole = computed(() => this.roleSignal());

  constructor(private keycloak: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    const roles = this.keycloak.getUserRoles(true); // true => realm roles
    console.log('Roles de Keycloak:', roles); // Debug
    
    // Los roles en Keycloak están en MAYÚSCULAS
    if (roles.includes('ORGANIZADOR')) {
      this.roleSignal.set('organizador');
      return;
    }
    if (roles.includes('PROVEEDOR')) {
      this.roleSignal.set('proveedor');
      return;
    }
    this.roleSignal.set('none');
  }
}
