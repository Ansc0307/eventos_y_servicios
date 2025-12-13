import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudesService } from '../services/solicitudes.service';
import { RefreshService } from '../services/refresh.service';
import { Solicitud } from '../models/solicitud.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-solicitudes-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="solicitudes">
      <h2>Solicitudes</h2>

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
      .section-separator { margin-bottom: 1.25rem; padding-bottom: 1rem; border-bottom: 1px solid #e6e6e6; }
      .section-separator h3 { margin: 0 0 0.75rem 0; color: #2b2b2b; font-size: 1rem; }
      .table-container { width: 100%; overflow-x: auto; }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.5rem; table-layout: auto; }
      th, td { padding: 6px 8px; border: 1px solid #ececec; text-align: left; font-size: 0.92rem; white-space: normal; word-break: break-word; min-width: 0; }
      thead th { background: #fafafa; font-weight: 700; }
      .error { color: #9b1c1c; background: #fff6f6; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .success { color: #15803d; background: #efe; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .info { color: #1e40af; background: #eff; padding: 8px; border-radius: 4px; margin: 8px 0; }
      .btn { background: #6d28d9; color: white; padding: 6px 10px; border-radius: 6px; border: none; cursor: pointer; font-weight: 600; font-size: 0.92rem; }
      .btn:disabled { opacity: 0.6; cursor: not-allowed; }
      .btn.secondary { background: transparent; border: 1px solid #ddd; color: #333; padding: 5px 9px; }
      .form { display: flex; flex-direction: column; gap: 0.75rem; max-width: 1100px; }
      .form-group { display: flex; flex-direction: column; gap: 0.4rem; }
      .form-group.wide { grid-column: 1 / -1; }
      .form-group label { font-weight: 600; color: #333; }
      .form-group input, .form-group select { width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.95rem; }
      .form-group input:focus, .form-group select:focus { outline: none; border-color: #6d28d9; box-shadow: 0 0 0 4px rgba(109,40,217,0.08); }
      .controls { display:flex; gap:8px; align-items:center; margin-bottom:0.75rem; }
      .empty { color: #999; font-style: italic; }
      .form-actions { display:flex; gap:0.5rem; align-items:center; }
      @media (min-width: 900px) {
        .form { display: grid; grid-template-columns: repeat(2, 1fr); gap: 0.75rem; }
        .form .form-actions { grid-column: 1 / -1; }
      }
      @media (min-width: 1400px) {
        .form { grid-template-columns: repeat(2, 1fr); }
      }
    `
  ]
})
export class SolicitudesListComponent {
  solicitudId: number | null = null;
  solicitud: Solicitud | null = null;
  loading = false;
  error: string | null = null;

  // Propiedades para listado completo
  solicitudes: Solicitud[] = [];
  loadingList = false;
  errorList: string | null = null;

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

  constructor(private service: SolicitudesService, private refreshService: RefreshService, private cd: ChangeDetectorRef) {}

 

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
        this.loadingCreate = false;// Recargar listado completo de solicitudes
        this.refreshService.notificaSolicitudesChanged(); // Notificar a otros componentes que cambió solicitudes
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
