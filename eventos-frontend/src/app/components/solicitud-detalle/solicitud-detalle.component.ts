import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { Solicitud } from '../../models/solicitud.model';
import { Reserva } from '../../models/reserva.model';
import { Oferta } from '../../models/oferta.model';
import { OfertasService } from '../../services/ofertas.service';

@Component({
  selector: 'app-solicitud-detalle',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
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

          <button (click)="close.emit()" class="p-2 hover:bg-slate-200 dark:hover:bg-slate-700 rounded-lg transition">
            <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
          </button>
        </div>

        <!-- CUERPO -->
        <div class="p-6 space-y-6">

          <!-- INFORMACIÓN SOLICITUD -->
          <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">description</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Solicitud</h3>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <p class="text-sm font-semibold text-slate-500">ID Solicitud</p>
                <p>#{{ solicitud?.idSolicitud }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">Estado Solicitud</p>
                <span class="inline-flex px-3 py-1 rounded-full text-sm font-medium mt-1"
                      [ngClass]="getEstadoClass(solicitud?.estadoSolicitud)">
                  {{ getEstadoLabel(solicitud?.estadoSolicitud) }}
                </span>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">Fecha Solicitud</p>
                <p>{{ formatDateLong(solicitud?.fechaSolicitud) }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">ID Oferta</p>
                <p>#{{ solicitud?.idOferta || '—' }}</p>
              </div>
            </div>
          </div>

          <!-- INFORMACIÓN OFERTA -->
          <div *ngIf="solicitud?.idOferta"
               class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">

            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">local_offer</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Oferta</h3>
            </div>

            <div *ngIf="loadingOferta" class="flex items-center gap-3">
              <div class="animate-spin h-5 w-5 border-b-2 border-primary rounded-full"></div>
              <span>Cargando oferta...</span>
            </div>

            <div *ngIf="errorOferta" class="text-red-600">
              {{ errorOferta }}
            </div>

            <div *ngIf="oferta" class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <p class="text-sm font-semibold text-slate-500">ID Oferta</p>
                <p>#{{ oferta.id }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">Título</p>
                <p class="font-medium text-primary">{{ oferta.titulo }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">Precio Base</p>
                <p>{{ oferta.precioBase | currency }}</p>
              </div>

              <div>
                <p class="text-sm font-semibold text-slate-500">Estado Oferta</p>
                <span class="inline-flex px-3 py-1 rounded-full text-sm font-medium mt-1"
                      [ngClass]="getEstadoClass(oferta.estado)">
                  {{ getEstadoLabel(oferta.estado) }}
                </span>
              </div>
            </div>
          </div>

          <!-- RESERVA -->
          <div *ngIf="reserva" class="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-6 border mt-6">
  <h3 class="text-xl font-bold mb-4">Reserva Asociada</h3>

  <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
    <div>
      <b>ID Reserva:</b> #{{ reserva.idReserva }}
    </div>

    <div>
      <b>Estado:</b> {{ getEstadoLabel(reserva.estado) }}
    </div>

    <div>
      <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">
        Fecha Inicio Reserva
      </p>
      <p class="text-base text-slate-900 dark:text-white">
        {{ formatDateLong(reserva.fechaReservaInicio) }}
      </p>
    </div>

    <div>
      <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">
        Fecha Fin Reserva
      </p>
      <p class="text-base text-slate-900 dark:text-white">
        {{ formatDateLong(reserva.fechaReservaFin) }}
      </p>
    </div>
  </div>
</div>


        </div>

        <!-- FOOTER -->
        <div class="px-6 py-4 flex justify-end border-t">
          <button (click)="close.emit()" class="bg-primary text-white px-4 py-2 rounded-lg">
            Cerrar
          </button>
        </div>

      </div>
    </div>
  `
})
export class SolicitudDetalleComponent implements OnChanges {

  @Input() solicitud: Solicitud | null = null;
  @Input() reserva: Reserva | null = null;
  @Output() close = new EventEmitter<void>();

  oferta: Oferta | null = null;
  loadingOferta = false;
  errorOferta: string | null = null;

  constructor(
    private ofertasService: OfertasService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['solicitud'] && this.solicitud?.idOferta) {
      this.cargarOferta(this.solicitud.idOferta);
    }
  }

  cargarOferta(idOferta: number) {
    this.loadingOferta = true;
    this.errorOferta = null;

    this.ofertasService.getOfertaById(idOferta).subscribe({
      next: (data) => {
        this.oferta = data;
        this.loadingOferta = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorOferta = 'No se pudo cargar la oferta';
        this.loadingOferta = false;
        this.cdr.detectChanges();
      }
    });
  }

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
    return estado || 'Desconocido';
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
