import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';

@Component({
  selector: 'app-oferta-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './oferta-detail.component.html',
  styleUrls: []
})
export class OfertaDetailComponent implements OnInit {
  oferta: Oferta | null = null;

  constructor(
    private route: ActivatedRoute,
    private ofertasService: OfertasService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.ofertasService.getOferta(id).subscribe({
        next: (o) => (this.oferta = o),
        error: (err) => console.error('Error loading oferta', err)
      });
    }
  }
}
