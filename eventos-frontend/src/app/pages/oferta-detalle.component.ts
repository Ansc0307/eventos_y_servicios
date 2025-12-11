import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, NgIf, NgFor, CurrencyPipe } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';

@Component({
  selector: 'app-oferta-detalle',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    NgFor,
    CurrencyPipe
  ],
  templateUrl: 'oferta-detalle.component.html'
})
export class OfertaDetalleComponent implements OnInit {

  oferta: any = null;
  images: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private ofertaService: OfertasService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

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
      },
      error: (err) => {
        console.error('Error cargando oferta', err);
      }
    });
  }

  get mainImage(): string {
    return this.images.length > 0 ? this.images[0] : '';
  }
}
