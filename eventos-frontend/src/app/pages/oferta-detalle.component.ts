import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CurrencyPipe, NgIf, NgFor, CommonModule } from '@angular/common';
import { OfertasService } from '../services/ofertas.service';

@Component({
  selector: 'app-oferta-detalle',
  standalone: true,
  imports: [
    CommonModule,
    NgIf,
    NgFor,
    CurrencyPipe   // <-- IMPORTANTE
  ],
  templateUrl: './oferta-detalle.component.html'
})
export class OfertaDetalleComponent implements OnInit {

  oferta: any;
  id!: number;

  constructor(
    private route: ActivatedRoute,
    private ofertaService: OfertasService
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.ofertaService.getOfertaById(this.id).subscribe({
      next: (data:any) => {
        this.oferta = data;
      },
      error: (err) => {
        console.error('Error cargando oferta', err);
      }
    });
  }

  get mainImage(): string {
    return this.oferta?.medias?.length > 0 ? this.oferta.medias[0].url : '';
  }
}
