import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { ReservasService } from '../services/reservas.service';
import { RefreshService } from '../services/refresh.service';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';
import { Reserva } from '../models/reserva.model';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-no-disponibilidades-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="no-disponibilidades">
      <h2>No Disponibilidades</h2>

      <!-- Sección de listado de no disponibilidades -->
      <div class="section-separator">
        <h3>Listado de No Disponibilidades</h3>
        <div *ngIf="loading">Cargando no disponibilidades...</div>
        <div *ngIf="error" class="error">Error cargando no disponibilidades: {{ error }}</div>
        <div class="table-container">
          <table *ngIf="noDisponibilidades?.length" class="tabla">
          <thead>
            <tr>
              <th>ID</th>
              <th>Oferta</th>
              <th>Motivo</th>
              <th>Inicio</th>
              <th>Fin</th>
              <th>Reserva ID</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let nd of noDisponibilidades">
              <td>{{ nd.idNoDisponibilidad }}</td>
              <td>{{ nd.idOferta }}</td>
              <td>{{ nd.motivo }}</td>
              <td>{{ nd.fechaInicio | date:'short' }}</td>
              <td>{{ nd.fechaFin | date:'short' }}</td>
              <td>{{ nd.idReserva || 'N/A' }}</td>
            </tr>
          </tbody>
          </table>
        </div>
        <div *ngIf="!loading && (!noDisponibilidades || noDisponibilidades.length === 0)">No hay no disponibilidades.</div>
      </div>

   
  `,
  styles: [
    `
      .no-disponibilidades { margin-top: 1rem; }
      .section-separator { margin-bottom: 1.25rem; padding-bottom: 1rem; border-bottom: 1px solid #e6e6e6; }
      .section-separator h3 { margin: 0 0 0.75rem 0; color: #2b2b2b; font-size: 1rem; }

      /* Tabla responsiva: permitir scroll interno, celdas que puedan hacer wrap */
      .table-container { width: 100%; overflow-x: auto; }
      .tabla { width: 100%; border-collapse: collapse; margin-top: 0.5rem; table-layout: auto; }
      th, td { padding: 6px 8px; border: 1px solid #ececec; text-align: left; font-size: 0.92rem; white-space: normal; word-break: break-word; min-width: 0; }
      thead th { background: #fafafa; font-weight: 700; }

      /* Mensajes */
      .error { color: #9b1c1c; background: #fff6f6; padding: 8px; border-radius: 6px; margin: 8px 0; }
      .success { color: #0f5132; background: #ecfdf3; padding: 8px; border-radius: 6px; margin: 8px 0; }
      .info { color: #0f172a; background: #f1f5f9; padding: 8px; border-radius: 6px; margin: 8px 0; }

      /* Botones compactos y consistentes */
      .btn { background: #6d28d9; color: white; padding: 6px 10px; border-radius: 6px; border: none; cursor: pointer; font-weight: 600; font-size: 0.92rem; }
      .btn.secondary { background: transparent; border: 1px solid #ddd; color: #333; padding: 5px 9px; }
      .btn:disabled { opacity: 0.6; cursor: not-allowed; }

      /* Form responsive similar a 'solicitudes' */
      .form { display: flex; flex-direction: column; gap: 0.75rem; }
      .form-group { display: flex; flex-direction: column; gap: 0.4rem; }
      /* fields that should span full width of the form grid */
      .form .form-group.full { grid-column: 1 / -1; }
      .form-group label { font-weight: 600; color: #333; }
      .form-group input, .form-group select, .form-group textarea { width: 100%; box-sizing: border-box; padding: 8px 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 0.95rem; }
      .form-group input:focus, .form-group select:focus, .form-group textarea:focus { outline: none; border-color: #6d28d9; box-shadow: 0 0 0 4px rgba(109,40,217,0.08); }
      .form-actions { display:flex; gap:0.5rem; align-items:center; }

      @media (min-width: 900px) {
        .form { display: grid; grid-template-columns: repeat(2, 1fr); gap: 0.75rem; }
        .form .form-actions { grid-column: 1 / -1; }
      }
      @media (min-width: 1200px) {
        .form { grid-template-columns: repeat(3, 1fr); }
      }

      /* Evitar que labels y selects forcen ancho fijo */
      select { max-width: 100%; }
    `
  ]
})
export class NoDisponibilidadesListComponent implements OnInit {
  noDisponibilidades: NoDisponibilidad[] = [];
  reservas: Reserva[] = [];
  loading = false;
  error: string | null = null;

  // Propiedades para crear no disponibilidad
  nuevaNoDisponibilidad = {
    idOferta: null as number | null,
    idReserva: null as number | null,
    motivo: '' as string,
    fechaInicio: '' as string,
    fechaFin: '' as string
  };
  loadingCreate = false;
  errorCreate: string | null = null;
  successMessage: string | null = null;

  constructor(
    private service: NoDisponibilidadesService,
    private reservasService: ReservasService,
    private refreshService: RefreshService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fetch();
    this.cargarReservas();
    // Suscribirse a cambios de solicitudes y reservas desde otros componentes
    this.refreshService.solicitudesRefresh$.subscribe(() => {
      console.log('[NoDisponibilidadesList] Recibida notificación de cambio en solicitudes');
    });
    this.refreshService.reservasRefresh$.subscribe(() => {
      console.log('[NoDisponibilidadesList] Recibida notificación de cambio en reservas, recargando...');
      this.cargarReservas();
    });
  }

  cargarReservas() {
    console.log('[NoDisponibilidadesList] cargarReservas: iniciando petición');
    this.reservasService.getAll().subscribe({
      next: (data) => {
        console.log('[NoDisponibilidadesList] Reservas cargadas (raw):', data);
        const all: Reserva[] = data || [];
        // Filtrar reservas que ya fueron usadas o canceladas para evitar conflictos
        this.reservas = all.filter(r => !this.isReservaUsada(r));
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      },
      error: (err) => {
        console.error('[NoDisponibilidadesList] Error cargando reservas:', err);
        this.reservas = [];
      }
    });
  }

  // Considera una reserva "usada" si su campo `estado` indica que ya fue consumida
  // o no está disponible. Ajustar los valores según la API si es necesario.
  isReservaUsada(reserva: Reserva): boolean {
    if (!reserva || !reserva.estado) return false;
    const estado = reserva.estado.toString().trim().toUpperCase();
    // Estados que NO queremos mostrar en el dropdown
    const estadosExcluidos = new Set(['USADA', 'CONSUMIDA', 'CANCELADA', 'CERRADA', 'FINALIZADA']);
    return estadosExcluidos.has(estado);
  }

  // Devuelve true si la reserva se solapa con las fechas ingresadas en el formulario
  reservaConflictaConNoDisponibilidad(reserva: Reserva): boolean {
    if (!reserva) return false;
    // Si no hay fechas en el formulario, no la consideramos en conflicto
    if (!this.nuevaNoDisponibilidad.fechaInicio || !this.nuevaNoDisponibilidad.fechaFin) return false;

    try {
      const inicioND = new Date(this.nuevaNoDisponibilidad.fechaInicio);
      const finND = new Date(this.nuevaNoDisponibilidad.fechaFin);
      const inicioR = new Date(reserva.fechaReservaInicio);
      const finR = new Date(reserva.fechaReservaFin);

      // Normalizar: si alguna fecha no es válida, no marcar como conflicto
      if (isNaN(inicioND.getTime()) || isNaN(finND.getTime()) || isNaN(inicioR.getTime()) || isNaN(finR.getTime())) {
        return false;
      }

      // Hay solapamiento si los rangos se intersectan: inicioND < finR && inicioR < finND
      return inicioND < finR && inicioR < finND;
    } catch (e) {
      return false;
    }
  }

  // Lista de reservas disponibles para mostrar en el dropdown
  // Comprueba si una reserva ya está referenciada por alguna NoDisponibilidad
  isReservaAsignada(reserva: Reserva): boolean {
    if (!reserva || !this.noDisponibilidades) return false;
    return this.noDisponibilidades.some(nd => nd.idReserva != null && nd.idReserva === reserva.idReserva);
  }

  reservasDisponibles(): Reserva[] {
    return (this.reservas || []).filter(r => 
      !this.isReservaUsada(r) &&
      !this.isReservaAsignada(r) &&
      !this.reservaConflictaConNoDisponibilidad(r)
    );
  }

  fetch() {
    console.log('[NoDisponibilidadesList] fetch: iniciando petición de no disponibilidades');
    this.loading = true;
    this.error = null;
    this.service.getAll().subscribe({
      next: (data) => {
        console.log('[NoDisponibilidadesList] fetch: respuesta recibida', data);
        this.noDisponibilidades = data || [];
        this.loading = false;
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      },
      error: (err) => {
        console.error('[NoDisponibilidadesList] fetch error', err);
        this.error = err?.message || 'Error desconocido';
        this.loading = false;
        try { this.cd.detectChanges(); } catch (e) { /* noop */ }
      }
    });
  }



  limpiarFormulario() {
    this.nuevaNoDisponibilidad = {
      idOferta: null,
      idReserva: null,
      motivo: '',
      fechaInicio: '',
      fechaFin: ''
    };
  }

}
