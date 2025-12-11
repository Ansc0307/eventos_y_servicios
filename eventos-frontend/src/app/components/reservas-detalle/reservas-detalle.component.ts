import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Reserva } from '../../models/reserva.model';

@Component({
  selector: 'app-reserva-detalle',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-xl max-w-lg w-full p-6 relative">
        <button
          (click)="close.emit()"
          class="absolute top-4 right-4 text-gray-500 dark:text-gray-300 hover:text-red-500"
        >
          <span class="material-symbols-outlined">close</span>
        </button>

        <h2 class="text-2xl font-bold text-slate-900 dark:text-white mb-4">Detalle de Reserva</h2>

        <div class="space-y-3 text-slate-700 dark:text-gray-300">
          <div class="flex justify-between">
            <span class="font-medium">ID Reserva:</span>
            <span>{{ reserva?.idReserva }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">ID Solicitud:</span>
            <span>{{ reserva?.idSolicitud }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Fecha Inicio:</span>
            <span>{{ formatDate(reserva?.fechaReservaInicio) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Fecha Fin:</span>
            <span>{{ formatDate(reserva?.fechaReservaFin) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="font-medium">Estado:</span>
            <span
              [ngClass]="{
                'px-2 py-0.5 rounded-full text-xs font-semibold': true,
                'bg-orange-100 text-orange-800': reserva?.estado === 'PENDIENTE',
                'bg-blue-100 text-blue-800': reserva?.estado === 'EN_NEGOCIACION',
                'bg-green-100 text-green-800': reserva?.estado === 'CONFIRMADA',
                'bg-red-100 text-red-800': reserva?.estado === 'CANCELADA' || reserva?.estado === 'RECHAZADA'
              }"
            >
              {{ reserva?.estado || 'Desconocido' }}
            </span>
          </div>
        </div>

        <div class="mt-6 flex justify-end">
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
export class ReservaDetalleComponent {
  @Input() reserva: Reserva | null = null;
  @Output() close = new EventEmitter<void>();

  formatDate(fecha?: string): string {
    if (!fecha) return '';
    return new Date(fecha).toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }
}
