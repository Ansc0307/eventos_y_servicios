import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { ReservasService } from '../services/reservas.service';
import { OfertasService } from '../services/ofertas.service'; // 🟢 Importar OfertasService
import { Solicitud } from '../models/solicitud.model';
import { Oferta } from '../models/oferta.model';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { Reserva } from '../models/reserva.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-organizador-reservas-list',
  standalone: true,
  imports: [CommonModule, FormsModule,RouterLink],
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
                <p class="text-primary/80 dark:text-primary/70 text-sm font-normal leading-normal">Organizador Verificado</p>
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
              <h1 class="text-3xl font-black text-slate-900 dark:text-white">Todas mis Reservas</h1>
              <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">Gestiona todas tus reservas</p>
            </div>
          </div>
        </header>

        <div class="p-10">
          <!-- Filtros iguales al proveedor -->
          <div class="mb-6 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
            <div class="flex flex-wrap items-center gap-4">
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
              <div class="flex items-center gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">Filtrar por Estado:</label>
                <select [(ngModel)]="filtroEstado" (change)="aplicarFiltros()"
                        class="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100 text-sm font-medium">
                  <option value="">Todos los Estados</option>
                  <option value="APROBADA">Aprobada</option>
                  <option value="CONFIRMADA">Confirmada</option>
                  <option value="CANCELADA">Cancelada</option>
                </select>
              </div>
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

          <!-- Content (igual al proveedor) -->
          <div *ngIf="!loading && !error" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800">
            <div *ngIf="todasLasReservas.length === 0" class="text-center py-12">
              <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">event_busy</span>
              <p class="mt-4 text-slate-500 dark:text-slate-400">No tienes reservas registradas</p>
            </div>
            <div *ngIf="todasLasReservas.length > 0 && reservasFiltradas.length === 0" class="text-center py-12">
              <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">filter_list_off</span>
              <p class="mt-4 text-slate-500 dark:text-slate-400">No hay reservas que coincidan con los filtros</p>
            </div>
            <div *ngIf="todasLasReservas.length > 0 && reservasFiltradas.length > 0" class="overflow-x-auto">
              <!-- Mensaje informativo -->
              <div class="bg-blue-50 dark:bg-blue-900/20 border-l-4 border-blue-400 p-4 mb-4">
                <p class="text-sm text-blue-800 dark:text-blue-300">
                  <span class="font-semibold">Nota:</span> Si deseas ver tus reservas pendientes, ve a 
                  <a (click)="irASolicitudes()" class="underline hover:text-blue-600 dark:hover:text-blue-400 cursor-pointer font-semibold">Mis Solicitudes</a>.
                </p>
              </div>
              <table class="w-full text-left">
                <thead class="border-b border-slate-200 dark:border-slate-800">
                  <tr>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Inicio</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Fin</th>
                    <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
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
                    <td class="p-6 text-right space-x-2">
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
                <p class="text-base text-slate-900 dark:text-white">{{ formatDate(reservaSeleccionada.fechaReservaInicio) }}</p>
              </div>
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha de Fin</p>
                <p class="text-base text-slate-900 dark:text-white">{{ formatDate(reservaSeleccionada.fechaReservaFin) }}</p>
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
                <p class="text-base text-slate-900 dark:text-white">{{ formatDate(solicitudSeleccionada.fechaSolicitud) }}</p>
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

         <div *ngIf="solicitudSeleccionada?.idOferta" class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 mb-6 border border-slate-200 dark:border-slate-700">
                    <h3 class="text-xl font-bold text-slate-800 dark:text-slate-100 mb-4">Detalles de la Oferta</h3>
                    <div *ngIf="loadingOferta" class="flex items-center justify-center py-6">
                      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
                      <p class="ml-3 text-slate-600 dark:text-slate-300">Cargando oferta...</p>
                    </div>
                    <div *ngIf="errorOferta && !loadingOferta" class="bg-red-100 dark:bg-red-900/40 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 px-4 py-3 rounded">
                      {{ errorOferta }}
                    </div>

                    <div *ngIf="ofertaAsociada" class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Oferta</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ ofertaAsociada.id }}</p>
                      </div>
                      <div class="md:col-span-2">
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Título</p>
                        <a class="text-base text-primary font-medium hover:underline cursor-pointer" 
                            [routerLink]="['/oferta', ofertaAsociada.id]">
                          {{ ofertaAsociada.titulo }}
                        </a>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Precio Base</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ ofertaAsociada.precioBase || 'N/A' }}</p> 
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Estado de Oferta</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ ofertaAsociada.estado }}</p>
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
  `
})
export class OrganizadorReservasListComponent implements OnInit {
  reservas: Reserva[] = [];
  reservasFiltradas: Reserva[] = [];
  loading = true;
  error: string | null = null;
  userName = '';
  idOrganizador = 14;
  filtroFecha: 'futuro' | 'todas' = 'futuro';
  filtroEstado: string = '';
  mostrarModal = false;
  loadingDetalle = false;
  reservaSeleccionada: Reserva | null = null;
  solicitudSeleccionada: Solicitud | null = null;
    ofertaAsociada: Oferta | null = null;
    loadingOferta = false;
    errorOferta: string | null = null;


  get todasLasReservas(): Reserva[] {
    return this.reservas.sort((a, b) => new Date(b.fechaReservaInicio).getTime() - new Date(a.fechaReservaInicio).getTime());
  }

  constructor(
    private router: Router,
    private keycloak: KeycloakService,
    private reservasService: ReservasService,
    private ofertasService: OfertasService,
    private cdr: ChangeDetectorRef,
    private noDispService: NoDisponibilidadesService
  ) {}

  ngOnInit(): void {
    try {
      const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
      this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Organizador';
      this.idOrganizador = 14;

      this.reservasService.getByOrganizador(this.idOrganizador).subscribe({
        next: (reservas: Reserva[]) => {
          // Excluir reservas en estado PENDIENTE
          const filtradas = (Array.isArray(reservas) ? reservas : []).filter(r => (r.estado || '').toUpperCase() !== 'PENDIENTE');
          this.reservas = filtradas;
          this.aplicarFiltros();
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error cargando reservas del organizador:', err);
          this.error = 'Error al cargar las reservas del organizador: ' + (err.message || err.statusText || 'Error desconocido');
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
    this.router.navigate(['/dashboard/organizador']);
  }

  getEstadoClass(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE':
        return 'bg-orange-100 text-orange-800';
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
    return date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }

  aplicarFiltros(): void {
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    this.reservasFiltradas = this.todasLasReservas.filter((reserva) => {
      const fechaInicio = new Date(reserva.fechaReservaInicio);
      fechaInicio.setHours(0, 0, 0, 0);
      const cumpleFecha = this.filtroFecha === 'todas' || fechaInicio >= hoy;
      const cumpleEstado = !this.filtroEstado || reserva.estado?.toUpperCase() === this.filtroEstado.toUpperCase();
      return cumpleFecha && cumpleEstado;
    });
  }

 verDetalle(reserva: Reserva): void {
    this.reservaSeleccionada = reserva;
    this.loadingDetalle = true;
    this.mostrarModal = true;
    
    // Limpiar estados de oferta previos
    this.ofertaAsociada = null;
    this.loadingOferta = false;
    this.errorOferta = null;


    // 1. Obtener la Solicitud asociada a la Reserva
    this.reservasService.getSolicitudByReservaId(reserva.idReserva).subscribe({
      next: (solicitud: Solicitud) => {
        this.solicitudSeleccionada = solicitud;
        
        // 2. Comprobar y cargar la Oferta
        if (solicitud.idOferta) {
          this.loadingOferta = true;
          this.ofertasService.getOfertaById(solicitud.idOferta).subscribe({
            next: (oferta: Oferta) => {
              this.ofertaAsociada = oferta;
              this.loadingOferta = false;
              this.loadingDetalle = false;
              this.cdr.detectChanges();
            },
            error: (err) => {
              console.error('Error cargando oferta:', err);
              this.errorOferta = 'Error al cargar la oferta asociada.';
              this.loadingOferta = false;
              this.loadingDetalle = false;
              this.cdr.detectChanges();
            }
          });
        } else {
          // Si no hay oferta, solo actualizamos el estado de carga del detalle
          this.loadingDetalle = false;
          this.cdr.detectChanges();
        }

      },
      error: (err: any) => {
        console.error('Error cargando solicitud:', err);
        this.loadingDetalle = false;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.reservaSeleccionada = null;
    this.solicitudSeleccionada = null;
    // También limpiamos los estados de la oferta al cerrar el modal
    this.ofertaAsociada = null;
    this.errorOferta = null;
    this.loadingOferta = false;
  }

  irASolicitudes(): void {
    this.router.navigate(['/solicitudes/organizador']);
  }
}
