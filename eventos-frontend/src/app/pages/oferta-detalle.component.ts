import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, NgIf, NgFor, CurrencyPipe } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';
import { ChangeDetectorRef } from '@angular/core';
import { SolicitudReservaFormComponent } from '../solicitudes/solicitud-reserva-form.component';

@Component({
  selector: 'app-oferta-detalle',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    NgFor,
    CurrencyPipe,
    SolicitudReservaFormComponent
  ],
  templateUrl: './oferta-detalle.component.html'
})
export class OfertaDetalleComponent implements OnInit {

  oferta: any = null;
  images: string[] = [];
  // 1. Propiedad para almacenar el ID de la oferta
  idOfertaActual: number | undefined;

  constructor(
    private route: ActivatedRoute,
    private ofertaService: OfertasService,
    private cdr: ChangeDetectorRef
  ) {}

  /**
   * Getter para obtener la URL de la imagen principal.
   * Resuelve el error 'TS2339' en el template.
   */
  get mainImage(): string {
    return this.images.length > 0 ? this.images[0] : '';
  }

  // 🟢 CORRECCIÓN: Nombre de la función, implementando OnInit.
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    // 2. Almacenar el ID actual de la URL
    // Si la URL no tiene un ID válido, idOfertaActual será undefined.
    this.idOfertaActual = isNaN(id) ? undefined : id; 

    // Solo cargamos la oferta si tenemos un ID válido
    if (this.idOfertaActual) {
      this.ofertaService.getOfertaById(this.idOfertaActual).subscribe({
        
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
  }
}