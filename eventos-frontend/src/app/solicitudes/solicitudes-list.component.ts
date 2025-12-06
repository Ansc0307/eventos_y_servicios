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

      <!-- Sección de búsqueda por ID -->
      <div class="section-separator">
        <h3>Buscar Solicitud</h3>
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
      </div>

      <!-- Sección de creación de solicitud -->
      <div class="section-separator">
        <h3>Crear Nueva Solicitud</h3>
        <p style="color: #666; font-size: 0.9rem; margin: 0 0 1rem 0;">La fecha y hora se establecerán automáticamente en el momento de la creación</p>
        <form (ngSubmit)="crearSolicitud()" class="form">
          <div class="form-group">
            <label for="idOrganizador">ID Organizador:</label>
            <input id="idOrganizador" type="number" [(ngModel)]="nuevaSolicitud.idOrganizador" name="idOrganizador" placeholder="Ej: 1" required />
          </div>

          <div class="form-group">
            <label for="idProovedor">ID Proveedor:</label>
            <input id="idProovedor" type="number" [(ngModel)]="nuevaSolicitud.idProovedor" name="idProovedor" placeholder="Ej: 1" required />
          </div>

          <div class="form-group">
            <label for="idOferta">ID Oferta:</label>
            <input id="idOferta" type="number" [(ngModel)]="nuevaSolicitud.idOferta" name="idOferta" placeholder="Ej: 1" required />
          </div>

          <div class="form-group">
            <label for="estadoSolicitud">Estado Solicitud:</label>
            <select id="estadoSolicitud" [(ngModel)]="nuevaSolicitud.estadoSolicitud" name="estadoSolicitud" required>
              <option value="">Seleccionar estado</option>
              <option value="pendiente">Pendiente</option>
              <option value="aprobada">Aprobada</option>
              <option value="rechazada">Rechazada</option>
            </select>
          </div>

          <div *ngIf="loadingCreate" class="info">Creando solicitud...</div>
          <div *ngIf="errorCreate" class="error">Error al crear: {{ errorCreate }}</div>
          <div *ngIf="successMessage" class="success">{{ successMessage }}</div>

          <button type="submit" class="btn" [disabled]="loadingCreate">Crear Solicitud</button>
          <button type="button" class="btn secondary" (click)="limpiarFormulario()">Limpiar</button>
        </form>
      </div>
    </section>
  `,
  styles: [
    `
      .solicitudes { margin-top: 1rem; }
      .section-separator { margin-bottom: 2rem; padding-bottom: 1.5rem; border-bottom: 2px solid #e0e0e0; }
      .section-separator:last-child { border-bottom: none; }
      .section-separator h3 { margin: 0 0 1rem 0; color: #333; }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.75rem }
      th, td { padding: 8px 12px; border: 1px solid #e0e0e0; text-align: left; }
      .error { color: #9b1c1c; background: #fee; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .success { color: #15803d; background: #efe; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .info { color: #1e40af; background: #eff; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .btn { background: #7c3aed; color: white; padding:8px 12px; border-radius:6px; border:none; cursor:pointer; font-weight: 600; }
      .btn:disabled { opacity: 0.6; cursor: not-allowed; }
      .btn.secondary { background: transparent; border:1px solid #ddd; color: #333 }
      .form { display: flex; flex-direction: column; gap: 1rem; }
      .form-group { display: flex; flex-direction: column; gap: 0.5rem; }
      .form-group label { font-weight: 600; color: #333; }
      .form-group input, .form-group select { padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 0.95rem; }
      .form-group input:focus, .form-group select:focus { outline: none; border-color: #7c3aed; box-shadow: 0 0 0 3px rgba(124, 58, 237, 0.1); }
      .controls { display:flex; gap:8px; align-items:center; margin-bottom:0.75rem; }
      .empty { color: #999; font-style: italic; }
    `
  ]
})
export class SolicitudesListComponent {
  solicitudId: number | null = null;
  solicitud: Solicitud | null = null;
  loading = false;
  error: string | null = null;

  // Propiedades para crear solicitud
  nuevaSolicitud = {
    idOrganizador: null as number | null,
    idProovedor: null as number | null,
    idOferta: null as number | null,
    estadoSolicitud: '' as string
  };
  loadingCreate = false;
  errorCreate: string | null = null;
  successMessage: string | null = null;

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

  crearSolicitud() {
    if (!this.nuevaSolicitud.idOrganizador || !this.nuevaSolicitud.idProovedor || !this.nuevaSolicitud.idOferta || !this.nuevaSolicitud.estadoSolicitud) {
      this.errorCreate = 'Por favor completa todos los campos';
      return;
    }

    this.loadingCreate = true;
    this.errorCreate = null;
    this.successMessage = null;

    // Usar fecha y hora actual automáticamente
    const fechaISO = new Date().toISOString();

    const payload: any = {
      fechaSolicitud: fechaISO,
      idOrganizador: this.nuevaSolicitud.idOrganizador,
      idProovedor: this.nuevaSolicitud.idProovedor,
      idOferta: this.nuevaSolicitud.idOferta,
      estadoSolicitud: this.nuevaSolicitud.estadoSolicitud.toUpperCase()
    };

    console.log('[SolicitudesList] Creando solicitud:', payload);

    this.service.create(payload).subscribe({
      next: (data: Solicitud) => {
        console.log('[SolicitudesList] Solicitud creada:', data);
        this.successMessage = `✓ Solicitud creada exitosamente (ID: ${data.idSolicitud})`;
        this.limpiarFormulario();
        this.loadingCreate = false;
        try { this.cd.detectChanges(); } catch(e) {}
        // Limpiar mensaje después de 3 segundos
        setTimeout(() => {
          this.successMessage = null;
          try { this.cd.detectChanges(); } catch(e) {}
        }, 3000);
      },
      error: (err: any) => {
        console.error('[SolicitudesList] Error creando solicitud:', err);
        this.errorCreate = err?.error?.message || err?.message || 'Error al crear la solicitud';
        this.loadingCreate = false;
        try { this.cd.detectChanges(); } catch(e) {}
      }
    });
  }

  limpiarFormulario() {
    this.nuevaSolicitud = {
      idOrganizador: null,
      idProovedor: null,
      idOferta: null,
      estadoSolicitud: ''
    };
  }

  clear() {
    this.solicitudId = null;
    this.solicitud = null;
    this.error = null;
  }
}
