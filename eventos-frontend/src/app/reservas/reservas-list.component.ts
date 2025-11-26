import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ReservasService } from '../services/reservas.service';
import { Reserva } from '../models/reserva.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-reservas-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="reservas">
      <h2>Microservicio Reservas</h2>
      <div *ngIf="loading">Cargando reservas...</div>
      <div *ngIf="error" class="error">Error cargando reservas: {{ error }}</div>
      <table *ngIf="reservas?.length" class="tabla">
        <thead>
          <tr>
            <th>ID</th>
            <th>Solicitud</th>
            <th>Inicio</th>
            <th>Fin</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let r of reservas">
            <td>{{ r.idReserva }}</td>
            <td>{{ r.idSolicitud }}</td>
            <td>{{ r.fechaReservaInicio | date:'short' }}</td>
            <td>{{ r.fechaReservaFin | date:'short' }}</td>
            <td>{{ r.estado }}</td>
          </tr>
        </tbody>
      </table>
      <div *ngIf="!loading && (!reservas || reservas.length === 0)">No hay reservas.</div>
    </section>
  `,
  styles: [
    `
      .reservas { margin-top: 1.5rem; }
      .tabla { width: 100%; border-collapse: collapse; }
      th, td { padding: 8px 12px; border: 1px solid #e0e0e0; text-align: left; }
      .error { color: red; }
    `
  ]
})
export class ReservasListComponent implements OnInit {
  reservas: Reserva[] = [];
  loading = false;
  error: string | null = null;

  constructor(private service: ReservasService, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetch();
  }

  fetch() {
    console.log('[ReservasList] fetch: iniciando petición de reservas');
    this.loading = true;
    this.error = null;
    this.service.getAll().subscribe({
      next: (data) => {
        console.log('[ReservasList] fetch: respuesta recibida', data);
        this.reservas = data || [];
        this.loading = false;
        // asegurar que Angular refresque la vista
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      },
      error: (err) => {
        console.error('[ReservasList] fetch error', err);
        this.error = err?.message || 'Error desconocido';
        this.loading = false;
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      }
    });
  }

  retry() {
    this.fetch();
  }
}
