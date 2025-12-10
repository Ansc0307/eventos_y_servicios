import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';

@Component({
  selector: 'app-oferta-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <section class="ofertas">
      <h2>Ofertas</h2>

      <button class="btn" routerLink="/ofertas/create">Crear nueva oferta</button>

      <div *ngIf="loading">Cargando ofertas...</div>
      <div *ngIf="error" class="error">{{ error }}</div>

      <ul *ngIf="ofertas.length" class="lista">
        <li *ngFor="let o of ofertas" class="item">
          <strong>{{ o.titulo }}</strong>
          <span>Bs {{ o.precioBase }}</span>

          <div class="actions">
            <a [routerLink]="['/ofertas', o.idOferta]">Ver</a>
            <a [routerLink]="['/ofertas/edit', o.idOferta]">Editar</a>
          </div>
        </li>
      </ul>

      <div *ngIf="!loading && ofertas.length === 0" class="empty">
        No hay ofertas registradas.
      </div>
    </section>
  `,
  styles: [
    `
      .btn { background:#7c3aed;color:white;padding:6px 12px;border-radius:6px;border:none;cursor:pointer;margin-bottom:1rem }
      .lista { list-style:none;padding:0;margin-top:1rem }
      .item {
        display:flex;
        justify-content:space-between;
        padding:8px;
        border-bottom:1px solid #ddd;
        align-items:center;
      }
      .actions a { margin-left:10px; text-decoration:underline; cursor:pointer }
      .error { color:#c62828; margin-top:0.5rem; }
      .empty { color:#777; margin-top:0.5rem }
    `
  ]
})
export class OfertaListComponent implements OnInit {
  ofertas: Oferta[] = [];
  error = '';
  loading = false;

  constructor(private service: OfertasService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;

    this.service.getAll().subscribe({
      next: (data: Oferta[]) => {
        this.ofertas = data || [];
        this.loading = false;
      },
      error: (err: any) => {
        this.error = err?.message || 'Error al cargar ofertas';
        this.loading = false;
      }
    });
  }
}
