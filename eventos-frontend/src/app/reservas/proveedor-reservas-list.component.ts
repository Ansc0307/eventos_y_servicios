import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SolicitudesService } from '../services/solicitudes.service';
import { ReservasService } from '../services/reservas.service';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { Reserva } from '../models/reserva.model';
import { Solicitud } from '../models/solicitud.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-proveedor-reservas-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex min-h-screen w-full">
      <aside class="flex h-screen w-64 flex-col border-r border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 sticky top-0">
        <div class="flex h-full flex-col justify-between p-4">
          <div class="flex flex-col gap-4">
            <div class="flex items-center gap-4 text-slate-900 dark:text-white px-3 py-2">
              <span class="material-symbols-outlined text-3xl text-primary">hub</span>
              <h2 class="text-lg font-bold tracking-[-0.015em]">EvenPro</h2>
            </div>
            <nav class="flex flex-col gap-2 mt-4">
              <a (click)="volverDashboard()" class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer text-slate-600 dark:text-slate-300">
                <span class="material-symbols-outlined">dashboard</span>
                <p class="text-sm font-medium">Dashboard</p>
              </a>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20">
                <span class="material-symbols-outlined text-slate-900 dark:text-white">event</span>
                <p class="text-slate-900 dark:text-white text-sm font-medium">Reservas</p>
              </div>
            </nav>
          </div>
          <div class="flex flex-col gap-4">
            <div class="flex gap-3 items-center">
              <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuAYQ3Xv_YuY339LzWOL4jYfKwpp_Xk9EQPeGlaPTZUaCbWibigjj_YB-aGxvhwg8F6DZMvP78IzouOQH3-QD04rKwZu0qAV4ksMNwLhpVskYFEt4FmVucm_mFLxLxPTX8hDHUjR_Z9oMgFc_G87oiDiH7JpnVMSiQivqyiCyL3FHFneBsNk31-5d9q8uvRmqI_l6FgX35MdysNRvagVfmucr0CWN1v_HLjU_aiWNcTSeh51R5rwoZnxazDlwLlmCDHhNO9UufJdhm1M');"></div>
              <div class="flex flex-col">
                <h1 class="text-slate-900 dark:text-white text-base font-medium leading-normal">{{ userName }}</h1>
                <p class="text-primary/80 dark:text-primary/70 text-sm font-normal leading-normal">Proveedor Verificado</p>
              </div>
            </div>
          </div>
        </div>
      </aside>

      <main class="flex-1">
        <header class="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 px-10 py-6 bg-white dark:bg-slate-900">
          <div class="flex items-center gap-4">
            <button (click)="volverDashboard()" class="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg transition-colors">
              <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">arrow_back</span>
            </button>
            <div>
              <h1 class="text-3xl font-black text-slate-900 dark:text-white">Todas las Reservas</h1>
              <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">Gestiona todas tus reservas</p>
            </div>
          </div>
        </header>

        <div class="p-10">
          <!-- Filtros -->
          <div class="mb-6 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
            <div class="flex flex-wrap items-center gap-4">
              <!-- Botones de Fecha -->
              <div class="flex gap-2">
                <button (click)="filtroFecha = 'futuro'; aplicarFiltros()" 
                        [class]="'px-4 py-2 rounded-lg text-sm font-semibold transition-colors ' + (filtroFecha === 'futuro' ? 'bg-primary text-white' : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700')">
                  Solo Futuras
                </button>
                <button (click)="filtroFecha = 'todas'; aplicarFiltros()" 
                        [class]="'px-4 py-2 rounded-lg text-sm font-semibold transition-colors ' + (filtroFecha === 'todas' ? 'bg-primary text-white' : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700')">
                  Ver Todas
                </button>
              </div>

              <!-- Selector de Estado -->
              <div class="flex items-center gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">Filtrar por Estado:</label>
                <select [(ngModel)]="filtroEstado" (change)="aplicarFiltros()"
                        class="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100 text-sm font-medium">
                  <option value="">Todos los Estados</option>
                  <option value="PENDIENTE">Pendiente</option>
                  <option value="APROBADA">Aprobada</option>
                  <option value="CONFIRMADA">Confirmada</option>
                  <option value="CANCELADA">Cancelada</option>
                </select>
              </div>

              <!-- Info del filtro -->
              <div class="text-sm text-slate-600 dark:text-slate-400 ml-auto">
                Mostrando <span class="font-semibold">{{ reservasFiltradas.length }}</span> de {{ todasLasReservas.length }} reservas
              </div>
            </div>
          </div>

          <!-- Loading -->
          <div *ngIf="loading" class="flex items-center justify-center py-12">
            <div class="text-center">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
              <p class="mt-4 text-gray-600 dark:text-gray-400">Cargando reservas...</p>
            </div>
          </div>

          <!-- Error -->
          <div *ngIf="error && !loading" class="bg-red-100 dark:bg-red-900/50 border border-red-400 dark:border-red-700 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
            {{ error }}
          </div>

          <!-- Content -->
          <div *ngIf="!loading && !error" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800">
            <!-- Sin reservas -->
            <div *ngIf="todasLasReservas.length === 0" class="text-center py-12">
              <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">event_busy</span>
              <p class="mt-4 text-slate-500 dark:text-slate-400">No tienes reservas registradas</p>
            </div>

            <!-- Sin reservas después del filtro -->
            <div *ngIf="todasLasReservas.length > 0 && reservasFiltradas.length === 0" class="text-center py-12">
              <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">filter_list_off</span>
              <p class="mt-4 text-slate-500 dark:text-slate-400">No hay reservas que coincidan con los filtros</p>
            </div>

            <!-- Con reservas -->
            <div *ngIf="todasLasReservas.length > 0 && reservasFiltradas.length > 0" class="overflow-x-auto">
              <table class="w-full text-left">
                <thead class="border-b border-slate-200 dark:border-slate-800">
                  <tr>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Inicio</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Fin</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400 text-right">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  <tr *ngFor="let reserva of reservasFiltradas" 
                      class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">#{{ reserva.idReserva }}</td>
                    <td class="p-6 text-sm text-slate-600 dark:text-slate-300">#{{ reserva.idSolicitud }}</td>
                    <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(reserva.fechaReservaInicio) }}</td>
                    <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(reserva.fechaReservaFin) }}</td>
                    <td class="p-6 text-sm">
                      <span [class]="'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ' + getEstadoClass(reserva.estado)">
                        {{ getEstadoLabel(reserva.estado) }}
                      </span>
                    </td>
                    <td class="p-6 text-right space-x-2 flex items-center justify-end">
                      <button *ngIf="isPendiente(reserva.estado)" title="Solo se puede cambiar el estado de la solicitud" class="inline-flex items-center justify-center text-orange-600 dark:text-orange-400 h-10 w-10 rounded-lg border border-orange-300 dark:border-orange-600 hover:bg-orange-50 dark:hover:bg-orange-900/20 transition-colors cursor-help">
                        <span class="material-symbols-outlined">help</span>
                      </button>
                      <button *ngIf="isEditable(reserva.estado)" (click)="abrirEditar(reserva)" class="inline-flex items-center justify-center text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 h-10 w-10 rounded-lg border border-blue-300 dark:border-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 transition-colors">
                        <span class="material-symbols-outlined">edit</span>
                      </button>
                      <button (click)="verDetalle(reserva)" class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">
                        Ver Detalle
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- Modal de Detalle -->
    <div *ngIf="mostrarModal" (click)="cerrarModal()" 
         class="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div (click)="$event.stopPropagation()" 
           class="bg-white dark:bg-slate-900 rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-hidden flex flex-col">
        <!-- Header -->
        <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-800 bg-primary/5">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined text-3xl text-primary">event_note</span>
            <div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white">Detalle de Reserva</h2>
              <p class="text-sm text-slate-500 dark:text-slate-400">Información completa</p>
            </div>
          </div>
          <button (click)="cerrarModal()" 
                  class="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg transition-colors">
            <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
          </button>
        </div>

        <!-- Content -->
        <div class="flex-1 overflow-y-auto p-6">
          <div *ngIf="reservaSeleccionada && solicitudSeleccionada" class="space-y-6">
            <!-- Información de la Reserva -->
            <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
              <div class="flex items-center gap-2 mb-4">
                <span class="material-symbols-outlined text-primary">event</span>
                <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Reserva</h3>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ reservaSeleccionada.idReserva }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</p>
                  <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(reservaSeleccionada.estado)">
                    {{ getEstadoLabel(reservaSeleccionada.estado) }}
                  </span>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha de Inicio</p>
                  <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(reservaSeleccionada.fechaReservaInicio) }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha de Fin</p>
                  <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(reservaSeleccionada.fechaReservaFin) }}</p>
                </div>
                
              </div>
            </div>

            <!-- Información de la Solicitud -->
            <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
              <div class="flex items-center gap-2 mb-4">
                <span class="material-symbols-outlined text-primary">description</span>
                <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Solicitud</h3>
              </div>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idSolicitud }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado Solicitud</p>
                  <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(solicitudSeleccionada.estadoSolicitud)">
                    {{ getEstadoLabel(solicitudSeleccionada.estadoSolicitud) }}
                  </span>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha de Solicitud</p>
                  <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(solicitudSeleccionada.fechaSolicitud) }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Organizador</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idOrganizador }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Proveedor</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idProovedor }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Oferta</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idOferta }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Loading del modal -->
          <div *ngIf="loadingDetalle" class="flex items-center justify-center py-12">
            <div class="text-center">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
              <p class="mt-4 text-gray-600 dark:text-gray-400">Cargando detalles...</p>
            </div>
          </div>
        </div>

        <!-- Footer -->
        <div class="flex items-center justify-end gap-3 px-6 py-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/30">
          <button (click)="cerrarModal()" 
                  class="px-6 py-2.5 rounded-lg text-slate-700 dark:text-slate-200 bg-white dark:bg-slate-800 border border-slate-300 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 font-semibold transition-colors">
            Cerrar
          </button>
        </div>
      </div>
    </div>

    <!-- Modal de Editar Estado -->
    <div *ngIf="mostrarEditar" (click)="cerrarEditar()"
         class="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div (click)="$event.stopPropagation()"
           class="bg-white dark:bg-slate-900 rounded-xl shadow-2xl max-w-xl w-full overflow-hidden">
        <!-- Header -->
        <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-800 bg-primary/5">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined text-3xl text-primary">edit</span>
            <div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white">Editar Estado de Reserva</h2>
              <p class="text-sm text-slate-500 dark:text-slate-400">Reserva #{{ reservaSeleccionada?.idReserva }}</p>
            </div>
          </div>
          <button (click)="cerrarEditar()"
                  class="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg transition-colors">
            <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
          </button>
        </div>

        <!-- Content -->
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Nuevo Estado</label>
            <div class="flex gap-3">
              <button (click)="nuevoEstado = 'CONFIRMADA'"
                      [class]="'px-4 py-2 rounded-lg border text-sm font-semibold ' + (nuevoEstado === 'CONFIRMADA' ? 'bg-green-100 border-green-300 text-green-800' : 'bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-200')">
                Confirmada
              </button>
              <button (click)="nuevoEstado = 'CANCELADA'"
                      [class]="'px-4 py-2 rounded-lg border text-sm font-semibold ' + (nuevoEstado === 'CANCELADA' ? 'bg-red-100 border-red-300 text-red-800' : 'bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-200')">
                Cancelada
              </button>
            </div>
          </div>

          <div class="bg-yellow-50 dark:bg-yellow-900/30 border border-yellow-200 dark:border-yellow-800 rounded-lg p-4" *ngIf="nuevoEstado === 'CANCELADA'">
            <p class="text-sm text-yellow-800 dark:text-yellow-200">
              Al cancelar la reserva se eliminarán todas las no disponibilidades asociadas.
            </p>
          </div>
        </div>

        <!-- Footer -->
        <div class="flex items-center justify-end gap-3 px-6 py-4 border-t border-slate-200 dark:border-slate-800 bg-slate-50 dark:bg-slate-800/30">
          <button (click)="cerrarEditar()"
                  class="px-6 py-2.5 rounded-lg text-slate-700 dark:text-slate-200 bg-white dark:bg-slate-800 border border-slate-300 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 font-semibold transition-colors">
            Cancelar
          </button>
          <button (click)="guardarEstado()" [disabled]="guardandoEstado || !nuevoEstado"
                  class="px-6 py-2.5 rounded-lg text-white bg-primary hover:bg-primary/90 disabled:opacity-50 font-semibold transition-colors">
            {{ guardandoEstado ? 'Guardando...' : 'Guardar' }}
          </button>
        </div>
      </div>
    </div>
  </div>
  `
})
export class ProveedorReservasListComponent implements OnInit {
  reservas: Reserva[] = [];
  solicitudes: Solicitud[] = [];
  loading = true;
  error: string | null = null;
  userName = '';
  idProveedor = 1;
  mostrarModal = false;
  loadingDetalle = false;
  reservaSeleccionada: Reserva | null = null;
  solicitudSeleccionada: Solicitud | null = null;
  mostrarEditar = false;
  nuevoEstado: 'CONFIRMADA' | 'CANCELADA' | '' = '';
  guardandoEstado = false;

  // Filtros
  filtroFecha: 'futuro' | 'todas' = 'futuro';
  filtroEstado: string = '';
  reservasFiltradas: Reserva[] = [];

  get todasLasReservas(): Reserva[] {
    return this.reservas
      .sort((a, b) => new Date(b.fechaReservaInicio).getTime() - new Date(a.fechaReservaInicio).getTime());
  }

  constructor(
    private router: Router,
    private keycloak: KeycloakService,
    private solicitudesService: SolicitudesService,
    private reservasService: ReservasService,
    private cdr: ChangeDetectorRef,
    private noDispService: NoDisponibilidadesService
  ) {}

  ngOnInit(): void {
    try {
      const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
      this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Proveedor';
      
      this.idProveedor = 1;

      console.log('Cargando solicitudes para obtener reservas del proveedor:', this.idProveedor);
      this.solicitudesService.getByProveedor(this.idProveedor).subscribe({
        next: (solicitudes) => {
          this.solicitudes = solicitudes;
          if (solicitudes.length > 0) {
            const reservaRequests = solicitudes.map(s => 
              this.reservasService.getByIdSolicitud(s.idSolicitud)
            );
            
            forkJoin(reservaRequests).subscribe({
              next: (reservasArrays) => {
                this.reservas = reservasArrays.flat();
                this.aplicarFiltros();
                this.loading = false;
                this.cdr.detectChanges();
              },
              error: (err) => {
                console.error('Error cargando reservas:', err);
                this.error = 'Error al cargar las reservas';
                this.loading = false;
                this.cdr.detectChanges();
              }
            });
          } else {
            this.loading = false;
            this.cdr.detectChanges();
          }
        },
        error: (err) => {
          console.error('Error cargando solicitudes:', err);
          this.error = 'Error al cargar las solicitudes: ' + (err.message || err.statusText || 'Error desconocido');
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } catch (err) {
      console.error('Error en ngOnInit:', err);
      this.error = 'Error al inicializar';
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  volverDashboard(): void {
    this.router.navigate(['/dashboard/proveedor']);
  }

  getEstadoClass(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE':
        return 'bg-orange-100 text-orange-800';
      case 'EN_NEGOCIACION':
        return 'bg-blue-100 text-blue-800';
      case 'APROBADA':
      case 'CONFIRMADA':
        return 'bg-green-100 text-green-800';
      case 'RECHAZADA':
      case 'CANCELADA':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  getEstadoLabel(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE':
        return 'Pendiente';
      case 'EN_NEGOCIACION':
        return 'En Negociación';
      case 'APROBADA':
        return 'Aprobada';
      case 'CONFIRMADA':
        return 'Confirmada';
      case 'RECHAZADA':
        return 'Rechazada';
      case 'CANCELADA':
        return 'Cancelada';
      default:
        return estado || 'Desconocido';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { 
      day: '2-digit', 
      month: '2-digit',
      year: 'numeric'
    });
  }

  formatDateLong(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { 
      day: '2-digit', 
      month: 'long',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  verDetalle(reserva: Reserva): void {
    console.log('verDetalle llamado con reserva:', reserva);
    this.reservaSeleccionada = reserva;
    this.loadingDetalle = true;
    this.mostrarModal = true;
    console.log('mostrarModal = true');
    
    // Cargar la solicitud asociada desde el endpoint de reservas
    console.log('Cargando solicitud del servidor usando endpoint de reservas, ID reserva:', reserva.idReserva);
    this.reservasService.getSolicitudByReservaId(reserva.idReserva).subscribe({
      next: (solicitud) => {
        console.log('Solicitud cargada del servidor:', solicitud);
        this.solicitudSeleccionada = solicitud;
        this.loadingDetalle = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error cargando solicitud:', err);
        // Intentar con el método alternativo si falla
        const solicitudMemoria = this.solicitudes.find(s => s.idSolicitud === reserva.idSolicitud);
        if (solicitudMemoria) {
          console.log('Usando solicitud de memoria como fallback');
          this.solicitudSeleccionada = solicitudMemoria;
        }
        this.loadingDetalle = false;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.reservaSeleccionada = null;
    this.solicitudSeleccionada = null;
  }

  abrirEditar(reserva: Reserva): void {
    this.reservaSeleccionada = reserva;
    this.nuevoEstado = '';
    this.mostrarEditar = true;
  }

  cerrarEditar(): void {
    this.mostrarEditar = false;
    this.nuevoEstado = '';
    this.reservaSeleccionada = null;
  }

  // Mostrar botón de ayuda para PENDIENTE
  isPendiente(estado: string): boolean {
    return (estado || '').toUpperCase() === 'PENDIENTE';
  }

  // Mostrar botón Editar solo para APROBADA
  isEditable(estado: string): boolean {
    return (estado || '').toUpperCase() === 'APROBADA';
  }

  // Aplicar filtros de fecha y estado
  aplicarFiltros(): void {
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    this.reservasFiltradas = this.todasLasReservas.filter((reserva) => {
      // Filtro por fecha
      const fechaInicio = new Date(reserva.fechaReservaInicio);
      fechaInicio.setHours(0, 0, 0, 0);
      
      const cumpleFecha = this.filtroFecha === 'todas' || fechaInicio >= hoy;

      // Filtro por estado
      const cumpleEstado = !this.filtroEstado || reserva.estado?.toUpperCase() === this.filtroEstado.toUpperCase();

      return cumpleFecha && cumpleEstado;
    });
  }

  // Permitir confirmar solo cuando está PENDIENTE
  canConfirm(estado: string): boolean {
    return (estado || '').toUpperCase() === 'PENDIENTE';
  }

  // Permitir cancelar cuando está PENDIENTE o CONFIRMADA
  canCancel(estado: string): boolean {
    const e = (estado || '').toUpperCase();
    return e === 'PENDIENTE' || e === 'CONFIRMADA';
  }

  guardarEstado(): void {
    if (!this.reservaSeleccionada || !this.nuevoEstado) return;
    this.guardandoEstado = true;
    const id = this.reservaSeleccionada.idReserva;
    const payload = { ...this.reservaSeleccionada, estado: this.nuevoEstado, fechaActualizacion: new Date().toISOString() } as any;

    this.reservasService.update(id, payload).subscribe({
      next: (resActualizada) => {
        const idx = this.reservas.findIndex(r => r.idReserva === id);
        if (idx !== -1) this.reservas[idx] = resActualizada;

        if (this.nuevoEstado === 'CANCELADA') {
          // eliminar no disponibilidad asociada via GET /v1/reservas/{id}/no-disponibilidad
          this.reservasService.getNoDisponibilidadByReserva(id).subscribe({
            next: (noDisp: any) => {
              if (noDisp?.idNoDisponibilidad) {
                this.noDispService.delete(noDisp.idNoDisponibilidad).subscribe({
                  next: () => {
                    this.guardandoEstado = false;
                    this.aplicarFiltros();
                    this.cerrarEditar();
                    this.cdr.detectChanges();
                  },
                  error: (err) => {
                    console.error('Error eliminando no disponibilidad:', err);
                    this.guardandoEstado = false;
                    this.cerrarEditar();
                  }
                });
              } else {
                // No hay no disponibilidad asociada, continuar
                this.guardandoEstado = false;
                this.aplicarFiltros();
                this.cerrarEditar();
                this.cdr.detectChanges();
              }
            },
            error: (err) => {
              console.error('Error obteniendo no disponibilidad de la reserva:', err);
              // Continuar aunque falle (puede no haber no disponibilidad)
              this.guardandoEstado = false;
              this.cerrarEditar();
            }
          });
        } else {
          this.guardandoEstado = false;
          this.aplicarFiltros();
          this.cerrarEditar();
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('Error actualizando reserva:', err);
        this.guardandoEstado = false;
      }
    });
  }
}
