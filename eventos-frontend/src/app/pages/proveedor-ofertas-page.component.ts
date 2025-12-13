import { Component, OnInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';
import { KeycloakService } from 'keycloak-angular';
import { OfertaCardProveedorComponent } from '../components/oferta-card/oferta-card-proveedor.component';

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
    private keycloak: KeycloakService
    , private cdr: ChangeDetectorRef
  ) {}
loadProveedorId() {
  //const tokenParsed: any = this.keycloak.getKeycloakInstance().tokenParsed;
  //console.log('ProveedorOfertasPage - tokenParsed:', tokenParsed);
  this.idProveedor = 1; // <-- el proveedor que está logueado
}

  async ngOnInit(): Promise<void> {
  await this.keycloak.isLoggedIn();

  this.loadProveedorId();

  if (this.idProveedor > 0) {
    this.loadMisOfertas();
  } else {
    console.warn('Proveedor ID inválido, no se cargan ofertas');
    this.loading = false;
  }
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
