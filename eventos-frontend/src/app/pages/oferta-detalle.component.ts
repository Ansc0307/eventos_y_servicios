import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, NgIf, NgFor, CurrencyPipe } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-oferta-detalle',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    NgFor,
    CurrencyPipe
  ],
  templateUrl: './oferta-detalle.component.html'
})

export class OfertaDetalleComponent implements OnInit {

  oferta: any = null;
  images: string[] = [];
  isProveedorView = false;
  idProveedorLogueado = 1; //cambiar idProveedorLogueado por el id del proveedor logueado

  constructor(
  private route: ActivatedRoute,
  private ofertaService: OfertasService,
  private cdr: ChangeDetectorRef,
  private router: Router
) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.isProveedorView = window.location.pathname.includes('proveedor');

    this.ofertaService.getOfertaById(id).subscribe({
      next: (data: any) => {
        console.log("DETALLE RECIBIDO ===>", data);

        this.oferta = data;

        // Preferimos medias si existen
        if (data.medias?.length > 0) {
          this.images = data.medias.map((m: any) => m.url);
        }
        // Fallback: usar urlsMedia
        else if (data.urlsMedia?.length > 0) {
          this.images = data.urlsMedia;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando oferta', err);
      }
    });
  }

  get mainImage(): string {
    return this.images.length > 0 ? this.images[0] : '';
  }
  get puedeEditar(): boolean {
  return true;
}


  editarOferta() {
  this.router.navigate([
    '/proveedor/ofertas',
    this.oferta.id,
    'editar'
  ]);
}

  eliminarOferta() {
    if (!confirm('¿Seguro que deseas eliminar esta oferta?')) return;

    this.ofertaService.deleteOferta(this.oferta.id).subscribe({
      next: () => {
        alert('Oferta eliminada');
        this.router.navigate(['/proveedor/ofertas']);
      },
      error: err => console.error(err)
    });
  }

}
