import { Component, OnInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';
import { KeycloakService } from 'keycloak-angular';
import { OfertaCardProveedorComponent } from '../components/oferta-card/oferta-card-proveedor.component';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-proveedor-ofertas-page',
  standalone: true,
  imports: [CommonModule, OfertaCardProveedorComponent],
  templateUrl: './proveedor-ofertas-page.component.html'
})
export class ProveedorOfertasPageComponent implements OnInit {

  ofertas: Oferta[] = [];
  idProveedor: number = 0;
  loading: boolean = true;

  constructor(
    private ofertasService: OfertasService,
    private keycloak: KeycloakService,
    private usuariosService: UsuariosService,
    private cdr: ChangeDetectorRef
  ) {}
  loadProveedorId(): void {
    this.usuariosService.me().subscribe({
      next: (u) => {
        this.idProveedor = u.id;
        this.loadMisOfertas();
      },
      error: (err) => {
        console.error('No se pudo obtener el usuario actual (/usuarios/me).', err);
        this.idProveedor = 0;
        this.loading = false;
        try { this.cdr.detectChanges(); } catch { /* ignore */ }
      }
    });
  }

  async ngOnInit(): Promise<void> {
    const loggedIn = await this.keycloak.isLoggedIn();
    if (!loggedIn) {
      console.warn('Usuario no autenticado, no se cargan ofertas del proveedor');
      this.loading = false;
      return;
    }

    this.loadProveedorId();
}



  loadMisOfertas() {
    this.loading = true;
    this.ofertasService.getOfertasPorProveedor(this.idProveedor).subscribe({
      next: (data) => {
        console.log('ProveedorOfertasPage - ofertas recibidas:', data);
        this.ofertas = data || [];
        this.loading = false;
        // Force change detection in case update happened outside Angular zone
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      },
      error: (err) => {
        console.error('Error cargando ofertas del proveedor', err);
        this.loading = false;
        try { this.cdr.detectChanges(); } catch (e) { /* ignore */ }
      }
    });
  }
}
