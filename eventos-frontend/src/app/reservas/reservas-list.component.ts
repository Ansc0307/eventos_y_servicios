import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReservasService } from '../services/reservas.service';
import { Reserva } from '../models/reserva.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-reservas-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="reservas">
      <h2>Reservas</h2>

      <!-- Sección de listado de reservas -->
      <div class="section-separator">
        <h3>Listado de Reservas</h3>
        <div *ngIf="loading">Cargando reservas...</div>
        <div *ngIf="error" class="error">Error cargando reservas: {{ error }}</div>
        <div class="table-container">
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
        </div>
        <div *ngIf="!loading && (!reservas || reservas.length === 0)">No hay reservas.</div>
      </div>

      <!-- Sección de creación de reserva -->
      <div class="section-separator">
        <h3>Crear Nueva Reserva</h3>
        <form (ngSubmit)="crearReserva()" class="form">
          <div class="form-group">
            <label for="idSolicitud">ID Solicitud:</label>
            <input id="idSolicitud" type="number" min="1" [(ngModel)]="nuevaReserva.idSolicitud" name="idSolicitud" required />
            <small class="hint">Introduce el ID numérico de la solicitud</small>
          </div>

          <div class="form-group">
            <label for="fechaReservaInicio">Fecha Inicio Reserva:</label>
            <input id="fechaReservaInicio" type="datetime-local" [(ngModel)]="nuevaReserva.fechaReservaInicio" name="fechaReservaInicio" required />
          </div>

          <div class="form-group">
            <label for="fechaReservaFin">Fecha Fin Reserva:</label>
            <input id="fechaReservaFin" type="datetime-local" [(ngModel)]="nuevaReserva.fechaReservaFin" name="fechaReservaFin" required />
          </div>

          <div class="form-group">
            <label for="estado">Estado:</label>
            <select id="estado" [(ngModel)]="nuevaReserva.estado" name="estado" required>
              <option value="">Seleccionar estado</option>
              <option value="confirmada">Confirmada</option>
              <option value="pendiente">Pendiente</option>
              <option value="cancelada">Cancelada</option>
            </select>
          </div>

          <div *ngIf="loadingCreate" class="info">Creando reserva...</div>
          <div *ngIf="errorCreate" class="error">Error al crear: {{ errorCreate }}</div>
          <div *ngIf="successMessage" class="success">{{ successMessage }}</div>

          <div class="form-actions">
            <button type="submit" class="btn" [disabled]="loadingCreate">Crear Reserva</button>
            <button type="button" class="btn secondary" (click)="limpiarFormulario()">Limpiar</button>
          </div>
        </form>
      </div>
    </section>
  `,
  styles: [
    `
      .reservas { margin-top: 1rem; }
      .section-separator { margin-bottom: 1.25rem; padding-bottom: 1rem; border-bottom: 1px solid #e6e6e6; }
      .section-separator h3 { margin: 0 0 0.75rem 0; color: #2b2b2b; font-size: 1rem; }

      .table-container { width: 100%; overflow-x: auto; }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.5rem; table-layout: auto; }
      th, td { padding: 6px 8px; border: 1px solid #ececec; text-align: left; font-size: 0.92rem; white-space: normal; word-break: break-word; min-width: 0; }
      thead th { background: #fafafa; font-weight: 700; }

      .error { color: #9b1c1c; background: #fff6f6; padding: 8px; border-radius: 6px; margin: 8px 0; }
      .success { color: #0f5132; background: #ecfdf3; padding: 8px; border-radius: 6px; margin: 8px 0; }
      .info { color: #0f172a; background: #f1f5f9; padding: 8px; border-radius: 6px; margin: 8px 0; }

      .btn { background: #6d28d9; color: white; padding: 6px 10px; border-radius: 6px; border: none; cursor: pointer; font-weight: 600; font-size: 0.92rem; }
      .btn.secondary { background: transparent; border: 1px solid #ddd; color: #333; padding: 5px 9px; }
      .btn:disabled { opacity: 0.6; cursor: not-allowed; }

      .form { display: flex; flex-direction: column; gap: 0.75rem; max-width: 1100px; }
      .form-group { display: flex; flex-direction: column; gap: 0.4rem; }
      /* helper to force a field to span full width of the form grid */
      .form-group.wide { grid-column: 1 / -1; }
      .form-group label { font-weight: 600; color: #333; }
      .form-group input, .form-group select { width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.95rem; }
      .form-group input:focus, .form-group select:focus { outline: none; border-color: #6d28d9; box-shadow: 0 0 0 4px rgba(109,40,217,0.08); }
      .form-actions { display:flex; gap:0.5rem; align-items:center; }

      @media (min-width: 900px) {
        .form { display: grid; grid-template-columns: repeat(2, 1fr); gap: 0.75rem; }
        .form .form-actions { grid-column: 1 / -1; }
      }
      /* Keep two columns even on very wide screens to avoid very large single fields */
      @media (min-width: 1400px) {
        .form { grid-template-columns: repeat(2, 1fr); }
      }
    `
  ]
})
 
export class ReservasListComponent implements OnInit {
  reservas: Reserva[] = [];
  loading = false;
  error: string | null = null;

  // Propiedades para crear reserva
  nuevaReserva = {
    idSolicitud: null as number | null,
    fechaReservaInicio: '' as string,
    fechaReservaFin: '' as string,
    estado: '' as string
  };
  loadingCreate = false;
  errorCreate: string | null = null;
  successMessage: string | null = null;

  constructor(
    private service: ReservasService,
    private cd: ChangeDetectorRef
  ) {}

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

  // Ya no se cargan automaticamente las solicitudes desde aquí. El campo
  // `ID Solicitud` es un input numérico donde el usuario introduce un ID
  // manualmente. Si se necesita refrescar desde otro componente, se puede
  // usar `SolicitudesService.refresh()` (si se implementa).

  crearReserva() {
    if (!this.nuevaReserva.idSolicitud || !this.nuevaReserva.fechaReservaInicio || !this.nuevaReserva.fechaReservaFin || !this.nuevaReserva.estado) {
      this.errorCreate = 'Por favor completa todos los campos';
      return;
    }

    // Validar que fecha inicio sea menor que fecha fin
    const inicio = new Date(this.nuevaReserva.fechaReservaInicio);
    const fin = new Date(this.nuevaReserva.fechaReservaFin);
    if (inicio >= fin) {
      this.errorCreate = 'La fecha de inicio debe ser menor que la fecha de fin';
      return;
    }

    this.loadingCreate = true;
    this.errorCreate = null;
    this.successMessage = null;

    const payload: any = {
      idSolicitud: this.nuevaReserva.idSolicitud,
      fechaReservaInicio: new Date(this.nuevaReserva.fechaReservaInicio).toISOString(),
      fechaReservaFin: new Date(this.nuevaReserva.fechaReservaFin).toISOString(),
      estado: this.nuevaReserva.estado.toUpperCase()
    };

    console.log('[ReservasList] Creando reserva:', payload);

    this.service.create(payload).subscribe({
      next: (data: Reserva) => {
        console.log('[ReservasList] Reserva creada:', data);
        this.successMessage = `✓ Reserva creada exitosamente (ID: ${data.idReserva})`;
        this.limpiarFormulario();
        this.loadingCreate = false;
        this.fetch(); // Recargar lista
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
        setTimeout(() => {
          this.successMessage = null;
          try { this.cd.detectChanges(); } catch (e) { /* noop */ }
        }, 3000);
      },
      error: (err: any) => {
        console.error('[ReservasList] Error creando reserva:', err);
        this.errorCreate = err?.error?.message || err?.message || 'Error al crear la reserva';
        this.loadingCreate = false;
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      }
    });
  }

  limpiarFormulario() {
    this.nuevaReserva = {
      idSolicitud: null,
      fechaReservaInicio: '',
      fechaReservaFin: '',
      estado: ''
    };
  }

  retry() {
    this.fetch();
  }
}
