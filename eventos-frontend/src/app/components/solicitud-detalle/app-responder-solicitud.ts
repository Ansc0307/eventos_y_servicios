import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Solicitud } from '../../models/solicitud.model';
import { SolicitudesService } from '../../services/solicitudes.service';
import { ReservasService } from '../../services/reservas.service';
import { NoDisponibilidadesService } from '../../services/no-disponibilidades.service';
import { NoDisponibilidad } from '../../models/NoDisponibilidad.model';
import { Reserva } from '../../models/reserva.model'; // Asegúrate de importar Reserva si no lo está

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

          // =========================================================================
          // 🚀 CASO APROBADA (Modificar reserva existente a CONFIRMADA)
          // =========================================================================
          if (this.estadoSeleccionado === 'APROBADA') {
            
            // 1. Buscar la reserva existente por ID de Solicitud
            this.reservasService.getByIdSolicitud(this.solicitud!.idSolicitud).subscribe({
              next: (reservas: Reserva[]) => {
                
                if (reservas.length === 0) {
                  this.error = 'No se encontró reserva asociada para la solicitud. La reserva debe existir previamente para ser confirmada.';
                  this.updated.emit(solicitudActualizada);
                  this.loading = false;
                  return;
                }
                
                const reservaExistente = reservas[0];
                const reservaActualizada: Partial<Reserva> = { 
                  ...reservaExistente, 
                  estado: 'APROBADA' 
                };

                console.log('[ResponderSolicitud] Confirmando reserva con payload:', JSON.stringify(reservaActualizada, null, 2));

                // 2. Actualizar la reserva a CONFIRMADA
                this.reservasService.update(reservaExistente.idReserva, reservaActualizada).subscribe({
                  next: (reservaConfirmada) => {
                    console.log('[ResponderSolicitud] Reserva confirmada exitosamente:', reservaConfirmada);

                    // 3. Crear No Disponibilidad con los datos de la reserva confirmada
                    const payloadNoDisp: Omit<NoDisponibilidad, 'idNoDisponibilidad'> = {
                      idOferta: this.solicitud!.idOferta,
                      motivo: 'Reserva aprobada y confirmada',
                      fechaInicio: reservaConfirmada.fechaReservaInicio,
                      fechaFin: reservaConfirmada.fechaReservaFin,
                      idReserva: reservaConfirmada.idReserva
                    };

                    console.log('[ResponderSolicitud] Registrando no disponibilidad con payload:', JSON.stringify(payloadNoDisp, null, 2));

                    this.noDispService.create(payloadNoDisp).subscribe({
                      next: () => {
                        console.log('[ResponderSolicitud] No disponibilidad registrada correctamente');
                        this.success = 'Solicitud aprobada, reserva confirmada y no disponibilidad registrada';
                        this.updated.emit(solicitudActualizada);
                        this.loading = false;
                      },
                      error: (err) => {
                        console.error('[ResponderSolicitud] ❌ Error registrando no disponibilidad:', err);
                        this.error = 'Se aprobó y confirmó la reserva, pero falló la no disponibilidad';
                        this.updated.emit(solicitudActualizada);
                        this.loading = false;
                      }
                    });
                  },
                  error: (err) => {
                    console.error('[ResponderSolicitud] ❌ Error confirmando reserva (UPDATE):', err);
                    this.error = 'Se aprobó la solicitud, pero falló la confirmación de la reserva. Ver consola.';
                    this.updated.emit(solicitudActualizada);
                    this.loading = false;
                  }
                });
              },
              error: (err) => {
                console.error('[ResponderSolicitud] ❌ Error buscando reserva asociada (GET):', err);
                this.error = 'Se aprobó la solicitud, pero falló la búsqueda de la reserva asociada. Ver consola.';
                this.updated.emit(solicitudActualizada);
                this.loading = false;
              }
            });
          } 
          // =========================================================================
          // 🛑 CASO RECHAZADA (Eliminar reserva asociada)
          // =========================================================================
          else if (this.estadoSeleccionado === 'RECHAZADA') {
            console.log('[ResponderSolicitud] Rechazada. Intentando eliminar reserva asociada con idSolicitud:', this.solicitud!.idSolicitud);
            
            // Llama al método encadenado del servicio (GET -> DELETE)
            this.reservasService.eliminarPorSolicitud(this.solicitud!.idSolicitud).subscribe({
              next: () => {
                console.log('[ResponderSolicitud] Proceso de eliminación de reserva asociada completado.');
                this.success = 'Solicitud rechazada. Se verificó y eliminó la reserva asociada.';
                this.updated.emit(solicitudActualizada);
                this.loading = false;
              },
              error: (err) => {
                // Si el error no es 404 (maneja por el service), lo mostramos.
                console.error('[ResponderSolicitud] ❌ Error en el proceso de eliminación de reserva asociada:', err);
                this.error = 'Solicitud rechazada. Error grave al intentar eliminar la reserva asociada.';
                this.updated.emit(solicitudActualizada);
                this.loading = false;
              }
            });
          }
          // =========================================================================
          // ✅ CASO OTROS (PENDIENTE - solo actualización de estado)
          // =========================================================================
          else {
            // Solo actualización de estado (si no es APROBADA o RECHAZADA)
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