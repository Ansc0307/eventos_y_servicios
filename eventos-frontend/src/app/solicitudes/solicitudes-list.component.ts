import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudesService } from '../services/solicitudes.service';
import { Solicitud } from '../models/solicitud.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-solicitudes-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="solicitudes">
      <h2>Solicitudes</h2>

      <div class="controls" style="display:flex;gap:8px;align-items:center;margin-bottom:0.75rem">
        <label for="solicitudId" style="font-weight:600">ID Solicitud:</label>
        <input id="solicitudId" name="solicitudId" type="number" [(ngModel)]="solicitudId" placeholder="Ej: 1" />
        <button class="btn" (click)="buscarPorId()" [disabled]="!solicitudId">Buscar</button>
        <button class="btn secondary" (click)="clear()">Limpiar</button>
      </div>

      <div *ngIf="loading">Cargando solicitud...</div>
      <div *ngIf="error" class="error">Error: {{ error }}</div>

      <table *ngIf="solicitud" class="tabla">
        <thead>
          <tr>
            <th>ID</th>
            <th>Fecha</th>
            <th>Estado</th>
            <th>Organizador</th>
            <th>Proveedor</th>
            <th>Oferta</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ solicitud.idSolicitud }}</td>
            <td>{{ solicitud.fechaSolicitud | date:'short' }}</td>
            <td>{{ solicitud.estadoSolicitud }}</td>
            <td>{{ solicitud.idOrganizador }}</td>
            <td>{{ solicitud.idProovedor }}</td>
            <td>{{ solicitud.idOferta }}</td>
          </tr>
        </tbody>
      </table>

      <div *ngIf="!loading && !solicitud" class="empty">Introduce un ID y pulsa Buscar para ver una solicitud.</div>
    </section>
  `,
  styles: [
    `
      .solicitudes { margin-top: 1rem; }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.75rem }
      th, td { padding: 8px 12px; border: 1px solid #e0e0e0; text-align: left; }
      .error { color: #9b1c1c; }
      .btn { background: #7c3aed; color: white; padding:6px 10px; border-radius:6px; border:none; cursor:pointer }
      .btn.secondary { background: transparent; border:1px solid #ddd; color: #333 }
    `
  ]
})
export class SolicitudesListComponent {
  solicitudId: number | null = null;
  solicitud: Solicitud | null = null;
  loading = false;
  error: string | null = null;

  constructor(private service: SolicitudesService, private cd: ChangeDetectorRef) {}

  buscarPorId() {
    if (!this.solicitudId) return;
    this.loading = true;
    this.error = null;
    this.solicitud = null;
    this.service.getById(this.solicitudId).subscribe({
      next: (data) => {
        this.solicitud = data;
        this.loading = false;
        try { this.cd.detectChanges(); } catch(e) {}
      },
      error: (err) => {
        this.error = err?.status === 404 ? 'Solicitud no encontrada' : (err?.message || 'Error desconocido');
        this.loading = false;
        try { this.cd.detectChanges(); } catch(e) {}
      }
    });
  }

  clear() {
    this.solicitudId = null;
    this.solicitud = null;
    this.error = null;
  }
}
