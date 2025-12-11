import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Solicitud } from '../../models/solicitud.model';
import { SolicitudesService } from '../../services/solicitudes.service';

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
              <option value="PENDIENTE">Pendiente</option>
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

  constructor(private solicitudesService: SolicitudesService) {}

  responder() {
    if (!this.solicitud || !this.estadoSeleccionado) return;

    this.loading = true;
    this.error = null;
    this.success = null;

    this.solicitudesService.actualizarEstado(this.solicitud.idSolicitud, this.estadoSeleccionado)
      .subscribe({
        next: (resp) => {
          this.success = 'Estado actualizado correctamente';
          this.updated.emit(resp); // para refrescar la lista
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al actualizar estado: ' + (err.error?.message || err.message || 'Error desconocido');
          this.loading = false;
        }
      });
  }
}
