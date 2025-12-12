import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Solicitud } from '../../models/solicitud.model';
import { Reserva } from '../../models/reserva.model';

@Component({
  selector: 'app-solicitud-detalle',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-2xl max-w-2xl w-full overflow-hidden animate-fadeIn">

        <!-- ENCABEZADO -->
        <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-800 bg-primary/5">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined text-3xl text-primary">description</span>
            <div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white">Detalle de Solicitud</h2>
              <p class="text-sm text-slate-500 dark:text-slate-400">Información completa</p>
            </div>
          </div>

          <button
            (click)="close.emit()"
            class="p-2 hover:bg-slate-200 dark:hover:bg-slate-700 rounded-lg transition"
          >
            <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
          </button>
        </div>

        <!-- CUERPO -->
        <div class="p-6">
          <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">

            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">description</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Solicitud</h3>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ solicitud?.idSolicitud }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado Solicitud</p>
                <span
                  class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1"
                  [ngClass]="getEstadoClass(solicitud?.estadoSolicitud)"
                >
                  {{ getEstadoLabel(solicitud?.estadoSolicitud) }}
                </span>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Solicitud</p>
                <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(solicitud?.fechaSolicitud) }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Organizador</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ solicitud?.idOrganizador }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Proveedor</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ solicitud?.idProovedor }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Oferta</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ solicitud?.idOferta }}</p>
              </div>

            </div>
          </div>

          <!-- Información de la Reserva Asociada -->
          <div *ngIf="reserva" class="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-6 border border-blue-200 dark:border-blue-700 mt-6">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-blue-600 dark:text-blue-400">event</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Reserva Asociada</h3>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ reserva.idReserva }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado Reserva</p>
                <span
                  class="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1"
                  [ngClass]="getEstadoClass(reserva.estado)"
                >
                  {{ getEstadoLabel(reserva.estado) }}
                </span>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Inicio Reserva</p>
                <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(reserva.fechaReservaInicio) }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Fin Reserva</p>
                <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(reserva.fechaReservaFin) }}</p>
              </div>

            </div>
          </div>

          <!-- Mensaje si no hay reserva -->
          <div *ngIf="!reserva" class="bg-gray-50 dark:bg-gray-800/50 rounded-lg p-6 border border-gray-200 dark:border-gray-700 mt-6">
            <div class="flex items-center gap-2">
              <span class="material-symbols-outlined text-gray-500 dark:text-gray-400">info</span>
              <p class="text-base text-slate-600 dark:text-slate-300">Aún no se ha creado una reserva para esta solicitud.</p>
            </div>
          </div>

        <!-- FOOTER -->
        <div class="px-6 py-4 flex justify-end border-t border-slate-200 dark:border-slate-800">
          <button
            (click)="close.emit()"
            class="bg-primary text-white px-4 py-2 rounded-lg hover:bg-primary/90"
          >
            Cerrar
          </button>
        </div>

      </div>
    </div>
  `
})
export class SolicitudDetalleComponent {
  @Input() solicitud: Solicitud | null = null;
  @Input() reserva: Reserva | null = null;
  @Output() close = new EventEmitter<void>();

  getEstadoClass(estado?: string) {
    switch (estado) {
      case 'PENDIENTE': return 'bg-orange-100 text-orange-800';
      case 'EN_NEGOCIACION': return 'bg-blue-100 text-blue-800';
      case 'APROBADA':
      case 'CONFIRMADA': return 'bg-green-100 text-green-800';
      case 'RECHAZADA':
      case 'CANCELADA': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-200 text-gray-700';
    }
  }

  getEstadoLabel(estado?: string) {
    switch (estado) {
      case 'PENDIENTE': return 'Pendiente';
      case 'EN_NEGOCIACION': return 'En Negociación';
      case 'APROBADA': return 'Aprobada';
      case 'CONFIRMADA': return 'Confirmada';
      case 'RECHAZADA': return 'Rechazada';
      case 'CANCELADA': return 'Cancelada';
      default: return 'Desconocido';
    }
  }

  formatDateLong(date?: string) {
    if (!date) return '';
    return new Date(date).toLocaleDateString('es-ES', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
