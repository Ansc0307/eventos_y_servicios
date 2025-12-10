import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriasService } from '../services/categorias.service';
import { Categoria } from '../models/categoria.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-categorias-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="categorias">
      <h2>Categorías</h2>

      <div class="controls" style="display:flex;gap:8px;align-items:center;margin-bottom:0.75rem">
        <input name="detalle" [(ngModel)]="detalle" placeholder="Nombre categoría" />
        <button class="btn" (click)="crear()" [disabled]="!detalle">Crear</button>
        <button class="btn secondary" (click)="refresh()">Refrescar</button>
      </div>

      <div *ngIf="loading">Cargando categorías...</div>
      <div *ngIf="error" class="error">Error: {{ error }}</div>

      <ul *ngIf="categorias.length" class="lista">
        <li *ngFor="let c of categorias">
          <strong>{{ c.detalle }}</strong>
        </li>
      </ul>

      <div *ngIf="!loading && (!categorias || categorias.length === 0)" class="empty">No hay categorías.</div>
    </section>
  `,
  styles: [
    `
      .categorias { margin-top: 1rem; }
      .lista { margin-top: 0.75rem; padding-left: 1rem; }
      .controls input { padding: 6px 8px; border: 1px solid #ddd; border-radius: 6px; }
      .btn { background: #7c3aed; color: white; padding:6px 10px; border-radius:6px; border:none; cursor:pointer }
      .btn.secondary { background: transparent; border:1px solid #ddd; color: #333 }
      .error { color: #9b1c1c; margin-top: 0.5rem; }
      .empty { color: #666; margin-top: 0.5rem; }
    `
  ]
})
export class CategoriasListComponent implements OnInit {

  categorias: Categoria[] = [];
  loading = false;
  error: string | null = null;

  // único campo que usa el backend
  detalle: string = '';

  constructor(private service: CategoriasService, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll() {
    this.loading = true;
    this.error = null;

    this.service.obtenerCategorias().subscribe({
      next: (data) => {
        this.categorias = data || [];
        this.loading = false;
        try { this.cd.detectChanges(); } catch {}
      },
      error: (err) => {
        this.error = err?.message || 'Error desconocido';
        this.loading = false;
        try { this.cd.detectChanges(); } catch {}
      }
    });
  }

  crear() {
    if (!this.detalle) return;

    const payload = { detalle: this.detalle };

    this.service.crearCategoria(payload).subscribe({
      next: () => {
        this.detalle = '';
        this.loadAll();
      },
      error: (err) => {
        console.error('[CategoriasList] crear error', err);
        this.error = err?.error?.message || err?.message || 'No se pudo crear la categoría';
      }
    });
  }

  refresh() {
    this.loadAll();
  }
}
