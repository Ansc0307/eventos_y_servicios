import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Solicitud } from '../../models/solicitud.model';
import { SolicitudesService } from '../../services/solicitudes.service';
import { ReservasService } from '../../services/reservas.service';
import { NoDisponibilidadesService } from '../../services/no-disponibilidades.service';
import { NoDisponibilidad } from '../../models/NoDisponibilidad.model';

@Component({
  selector: 'app-responder-solicitud',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-xl max-w-md w-full p-6 relative">
        <button (click)="close.emit()"
                class="absolute top-4 right-4 text-gray-500 dark:text-gray-300 hover:text-red-500">
          <span class="material-symbols-outlined">close</span>
        </button>

        <h2 class="text-2xl font-bold text-slate-900 dark:text-white mb-4">
          Responder Solicitud #{{ solicitud?.idSolicitud }}
        </h2>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 dark:text-gray-300 mb-1">
              Estado
            </label>
            <select id="estadoSolicitud"
                    [(ngModel)]="estadoSeleccionado"
                    class="form-select w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white px-3 py-2">
              <option value="">Seleccionar estado</option>
              <option value="APROBADA">Aprobada</option>
              <option value="RECHAZADA">Rechazada</option>
            </select>
          </div>

          <div class="flex justify-end gap-3 mt-6">
            <button (click)="close.emit()"
                    class="px-4 py-2 rounded-lg border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-gray-300 hover:bg-slate-100 dark:hover:bg-slate-800">
              Cancelar
            </button>
            <button (click)="responder()"
                    [disabled]="!estadoSeleccionado || loading"
                    class="bg-primary text-white px-4 py-2 rounded-lg hover:bg-primary/90 disabled:opacity-50">
              {{ loading ? 'Guardando...' : 'Responder' }}
            </button>
          </div>

          <div *ngIf="error" class="text-red-600 mt-2">{{ error }}</div>
          <div *ngIf="success" class="text-green-600 mt-2">{{ success }}</div>
        </div>
      </div>
    </div>
  `
})
export class ResponderSolicitudComponent {
  @Input() solicitud: Solicitud | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() updated = new EventEmitter<Solicitud>();

  estadoSeleccionado: string = '';
  loading = false;
  error: string | null = null;
  success: string | null = null;

  constructor(
    private solicitudesService: SolicitudesService,
    private reservasService: ReservasService,
    private noDispService: NoDisponibilidadesService,
  ) {}

  responder() {
    if (!this.solicitud || !this.estadoSeleccionado) return;

    this.loading = true;
    this.error = null;
    this.success = null;

    console.log('[ResponderSolicitud] Iniciando respuesta. Estado seleccionado:', this.estadoSeleccionado, 'Solicitud:', this.solicitud);
    this.solicitudesService.actualizarEstado(this.solicitud.idSolicitud, this.estadoSeleccionado)
      .subscribe({
        next: (solicitudActualizada) => {
          console.log('[ResponderSolicitud] Estado de solicitud actualizado:', solicitudActualizada);
          // Si se aprueba, crear la reserva y registrar no disponibilidad
          if (this.estadoSeleccionado === 'APROBADA') {
            // Backend espera LocalDateTime en formato ISO sin 'Z' (yyyy-MM-ddTHH:mm:ss)
            const now = new Date();
            const formatLocalDateTime = (date: Date | string) => {
              const d = typeof date === 'string' ? new Date(date) : date;
              const pad = (n: number) => n.toString().padStart(2, '0');
              return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
            };
            const nowLocal = formatLocalDateTime(now);
            // Usar fechas de la solicitud si existen, si no usar fecha actual
            const fechaInicio = this.solicitud!.fechaSolicitud ? formatLocalDateTime(this.solicitud!.fechaSolicitud) : nowLocal;
            const fechaFin = fechaInicio; // Por ahora mismo día, ajustar según lógica de negocio

            const nuevaReserva = {
              idSolicitud: this.solicitud!.idSolicitud,
              fechaReservaInicio: fechaInicio,
              fechaReservaFin: fechaFin,
              estado: 'PENDIENTE'
            } as any;

            console.log('[ResponderSolicitud]  Creando reserva con payload:', JSON.stringify(nuevaReserva, null, 2));
            this.reservasService.create(nuevaReserva).subscribe({
              next: (reservaCreada) => {
                console.log('[ResponderSolicitud]  Reserva creada exitosamente:', reservaCreada);
                // Usar el idReserva devuelto por la BD para registrar no disponibilidad
                const payloadNoDisp: Omit<NoDisponibilidad, 'idNoDisponibilidad'> = {
                  idOferta: this.solicitud!.idOferta,
                  motivo: 'Reserva aprobada',
                  fechaInicio: reservaCreada.fechaReservaInicio,
                  fechaFin: reservaCreada.fechaReservaFin,
                  idReserva: reservaCreada.idReserva
                };

                console.log('[ResponderSolicitud]  Registrando no disponibilidad con payload:', JSON.stringify(payloadNoDisp, null, 2));
                this.noDispService.create(payloadNoDisp).subscribe({
                  next: () => {
                    console.log('[ResponderSolicitud]  No disponibilidad registrada correctamente');
                    this.success = 'Solicitud aprobada, reserva creada y no disponibilidad registrada';
                    this.updated.emit(solicitudActualizada);
                    this.loading = false;
                  },
                  error: (err) => {
                    console.error('[ResponderSolicitud] ❌ Error registrando no disponibilidad:', err);
                    console.error('Status:', err.status);
                    console.error('Error completo:', JSON.stringify(err.error || err, null, 2));
                    this.error = 'Se aprobó y creó la reserva, pero falló la no disponibilidad';
                    this.updated.emit(solicitudActualizada);
                    this.loading = false;
                  }
                });
              },
              error: (err) => {
                console.error('[ResponderSolicitud] ❌ Error creando reserva:', err);
                console.error('HTTP Status:', err.status);
                console.error('Error backend:', JSON.stringify(err.error || {}, null, 2));
                console.error('Headers:', err.headers);
                this.error = 'Se aprobó la solicitud, pero falló la creación de la reserva. Ver consola.';
                this.updated.emit(solicitudActualizada);
                this.loading = false;
              }
            });
          } else {
            // Solo actualización de estado (pendiente/rechazada)
            this.success = 'Estado actualizado correctamente';
            this.updated.emit(solicitudActualizada);
            this.loading = false;
          }
        },
        error: (err) => {
          console.error('[ResponderSolicitud] Error actualizando estado:', err);
          this.error = 'Error al actualizar estado: ' + (err.error?.message || err.message || 'Error desconocido');
          this.loading = false;
        }
      });
  }
}
