import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SolicitudesService } from '../services/solicitudes.service';
import { ReservasService } from '../services/reservas.service';
import { Solicitud } from '../models/solicitud.model';
import { Reserva } from '../models/reserva.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-organizador-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex min-h-screen w-full">
      <aside class="flex h-screen w-64 flex-col border-r border-gray-200 dark:border-gray-800 bg-white dark:bg-background-dark sticky top-0">
        <div class="flex h-full flex-col justify-between p-4">
          <div class="flex flex-col gap-4">
            <div class="flex items-center gap-3 px-3 py-2">
              <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuD_N3byYGxORJKe6osQbW1Xxw6vzhzXjs3TJx8tRXVmZdjYqe2fItmel82Up-7FUXsIfbFBW7meG0OGKfttIlkFtIiRiopVGuYyfVPoKU8wX6h_k_U2A39Ml82ZoCRwugKxQ1JuutjFQ-dh6AH1-ilk3IRGng6Hf2vIm3hBctSmTeH9s6E1fekH7Vc428WIa9i3qy0n0fS22j_ZZ0CTVtpLU8A6qxaamZj8hQM_59CbvGMerIIoBSO0rhXTXVaXoG7UF1X0Hc4qUGJw');"></div>
              <div class="flex flex-col">
                <h1 class="text-base font-bold text-gray-900 dark:text-white">EventPro</h1>
                <p class="text-sm text-gray-500 dark:text-gray-400">Workspace</p>
              </div>
            </div>
            <nav class="flex flex-col gap-2 mt-4">
              <a class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20 text-primary" href="#">
                <span class="material-symbols-outlined">dashboard</span>
                <p class="text-sm font-medium">Dashboard</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" (click)="verMisSolicitudes()">
                <span class="material-symbols-outlined">description</span>
                <p class="text-sm font-medium">Mis Solicitudes</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" (click)="verMisReservas()">
                <span class="material-symbols-outlined">event</span>
                <p class="text-sm font-medium">Mis Reservas</p>
              </a>
              <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
                <span class="material-symbols-outlined">settings</span>
                <p class="text-sm font-medium">Ajustes</p>
              </a>
            </nav>
          </div>
          <div class="flex flex-col gap-1">
            <a class="flex items-center gap-3 px-3 py-2 text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg" href="#">
              <span class="material-symbols-outlined">logout</span>
              <p class="text-sm font-medium">Cerrar Sesión</p>
            </a>
          </div>
        </div>
      </aside>

      <main class="flex-1 flex flex-col">
        <header class="flex h-16 items-center justify-end gap-4 border-b border-gray-200 dark:border-gray-800 bg-white dark:bg-background-dark px-6 sticky top-0 z-10">
          <button class="relative p-2 text-gray-600 dark:text-gray-300 hover:text-gray-900 dark:hover:text-white">
            <span class="material-symbols-outlined">notifications</span>
            <span class="absolute top-2 right-2 flex h-2 w-2">
              <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-primary opacity-75"></span>
              <span class="relative inline-flex rounded-full h-2 w-2 bg-primary"></span>
            </span>
          </button>
        </header>

        <div class="flex-1 overflow-y-auto p-6 md:p-8 lg:p-10">
          <div class="mx-auto max-w-7xl">
            <!-- Loading -->
            <div *ngIf="loading" class="flex items-center justify-center py-12">
              <div class="text-center">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
                <p class="mt-4 text-gray-600 dark:text-gray-400">Cargando datos...</p>
              </div>
            </div>

            <!-- Error -->
            <div *ngIf="error && !loading" class="bg-red-100 dark:bg-red-900/50 border border-red-400 dark:border-red-700 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
              {{ error }}
            </div>

            <!-- Content -->
            <div *ngIf="!loading && !error">
              <div class="flex flex-wrap items-center justify-between gap-4 mb-8">
                <h1 class="text-3xl font-bold tracking-tight text-gray-900 dark:text-white">¡Bienvenido de nuevo, {{ userName }}!</h1>
                <button (click)="buscarOfertas()" class="flex min-w-[84px] items-center justify-center gap-2 overflow-hidden rounded-lg h-10 px-5 bg-primary text-white text-sm font-bold shadow-sm hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 dark:focus:ring-offset-background-dark">
                  <span class="material-symbols-outlined !text-xl">search</span>
                  <span>Buscar Ofertas</span>
                </button>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                  <p class="text-base font-medium text-gray-500 dark:text-gray-400">Solicitudes Enviadas</p>
                  <p class="text-3xl font-bold text-gray-900 dark:text-white">{{ solicitudesEnviadas }}</p>
                </div>
                <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                  <p class="text-base font-medium text-gray-500 dark:text-gray-400">Reservas Activas</p>
                  <p class="text-3xl font-bold text-gray-900 dark:text-white">{{ reservasConfirmadas }}</p>
                </div>
                <div class="flex flex-col gap-2 rounded-xl p-6 border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                  <p class="text-base font-medium text-gray-500 dark:text-gray-400">Acciones Pendientes</p>
                  <p class="text-3xl font-bold text-gray-900 dark:text-white">{{ accionesPendientes }}</p>
                </div>
              </div>

              <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div class="lg:col-span-2">
                  <div class="rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                    <div class="px-6 py-4 border-b border-gray-200 dark:border-gray-800">
                      <h2 class="text-lg font-bold text-gray-900 dark:text-white">Solicitudes Recientes</h2>
                    </div>
                    <div class="p-2 sm:p-4">
                      <!-- Sin solicitudes -->
                      <div *ngIf="solicitudesRecientes.length === 0" class="text-center py-12">
                        <span class="material-symbols-outlined text-6xl text-gray-300 dark:text-gray-600">inbox</span>
                        <p class="mt-4 text-gray-500 dark:text-gray-400">Aún no tienes solicitudes enviadas</p>
                      </div>

                      <!-- Con solicitudes -->
                      <div *ngIf="solicitudesRecientes.length > 0" class="flow-root">
                        <div class="divide-y divide-gray-200 dark:divide-gray-800">
                          <div *ngFor="let solicitud of solicitudesRecientes" 
                               class="flex flex-wrap items-center justify-between gap-4 p-4 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                            <div class="flex items-center gap-4">
                              <div class="flex items-center justify-center size-12 rounded-lg bg-primary/20 text-primary font-bold">
                                #{{ solicitud.idSolicitud }}
                              </div>
                              <div>
                                <p class="font-semibold text-gray-800 dark:text-gray-100">Solicitud #{{ solicitud.idSolicitud }}</p>
                                <p class="text-sm text-gray-500 dark:text-gray-400">{{ formatDate(solicitud.fechaSolicitud) }}</p>
                              </div>
                            </div>
                            <div [class]="'flex items-center gap-2 text-sm font-medium px-3 py-1 rounded-full ' + getEstadoClass(solicitud.estadoSolicitud)">
                              <span class="material-symbols-outlined !text-base">{{ getEstadoIcon(solicitud.estadoSolicitud) }}</span>
                              <span>{{ solicitud.estadoSolicitud }}</span>
                            </div>
                            <div class="flex items-center gap-2 w-full sm:w-auto">
                              <button (click)="verDetalleSolicitud(solicitud)"
                                      class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">
                                Ver Detalle
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="lg:col-span-1">
                  <div class="rounded-xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900/50">
                    <div class="px-6 py-4 border-b border-gray-200 dark:border-gray-800">
                      <h2 class="text-lg font-bold text-gray-900 dark:text-white">Próximas Reservas</h2>
                    </div>
                    <div class="p-4">
                      <!-- Sin reservas -->
                      <div *ngIf="proximasReservas.length === 0" class="text-center py-12">
                        <span class="material-symbols-outlined text-6xl text-gray-300 dark:text-gray-600">event_busy</span>
                        <p class="mt-4 text-gray-500 dark:text-gray-400">Aún no tienes reservas activas</p>
                      </div>

                      <!-- Con reservas -->
                      <div *ngIf="proximasReservas.length > 0" class="flex flex-col gap-4">
                        <div *ngFor="let reserva of proximasReservas" 
                             class="flex items-start gap-4 p-3 hover:bg-gray-50 dark:hover:bg-gray-800/50 rounded-lg">
                          <div class="flex flex-col items-center justify-center p-3 w-16 h-16 bg-primary/20 text-primary rounded-lg">
                            <span class="text-sm font-bold uppercase">{{ getMonthName(reserva.fechaReservaInicio) }}</span>
                            <span class="text-2xl font-black">{{ getDay(reserva.fechaReservaInicio) }}</span>
                          </div>
                          <div>
                            <p class="font-semibold text-gray-800 dark:text-gray-100">Reserva #{{ reserva.idReserva }}</p>
                            <p class="text-sm text-gray-500 dark:text-gray-400">{{ formatDate(reserva.fechaReservaInicio) }}</p>
                            <p class="text-xs text-gray-400 dark:text-gray-500 mt-1">Solicitud #{{ reserva.idSolicitud }}</p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </main>
    </div>
  </div>

  <!-- Modal Detalle Solicitud (simple, reusa estilo del proveedor) -->
  <div *ngIf="modalVisible" (click)="cerrarModal()" 
       class="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
    <div (click)="$event.stopPropagation()" 
         class="bg-white dark:bg-slate-900 rounded-xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-hidden flex flex-col">
      <!-- Header -->
      <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-800 bg-primary/5">
        <div class="flex items-center gap-3">
          <span class="material-symbols-outlined text-3xl text-primary">description</span>
          <div>
            <h2 class="text-2xl font-bold text-slate-900 dark:text-white">Detalle de Solicitud</h2>
            <p class="text-sm text-slate-500 dark:text-slate-400">Información completa</p>
          </div>
        </div>
        <button (click)="cerrarModal()" 
                class="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg transition-colors">
          <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
        </button>
      </div>

      <!-- Content -->
      <div class="flex-1 overflow-y-auto p-6" *ngIf="selectedSolicitud">
        <div class="space-y-6">
          <!-- Información de la Solicitud -->
          <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">description</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Información de la Solicitud</h3>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ selectedSolicitud.idSolicitud }}</p>
              </div>
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</p>
                <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(selectedSolicitud.estadoSolicitud)">
                  {{ selectedSolicitud.estadoSolicitud }}
                </span>
              </div>
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Solicitud</p>
                <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(selectedSolicitud.fechaSolicitud) }}</p>
              </div>
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Organizador</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ selectedSolicitud.idOrganizador }}</p>
              </div>
              <div>
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Proveedor</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ selectedSolicitud.idProovedor }}</p>
              </div>
              <div *ngIf="selectedSolicitud.idOferta">
                <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Oferta</p>
                <p class="text-base text-slate-900 dark:text-white">#{{ selectedSolicitud.idOferta }}</p>
              </div>
            </div>
          </div>

          <!-- Información de la Reserva asociada (si existe en memoria) -->
          <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
            <div class="flex items-center gap-2 mb-4">
              <span class="material-symbols-outlined text-primary">event</span>
              <h3 class="text-xl font-bold text-slate-900 dark:text-white">Reserva Asociada</h3>
            </div>
            <ng-container *ngIf="selectedReserva; else sinReservaDash">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ selectedReserva.idReserva }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</p>
                  <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(selectedReserva.estado)">
                    {{ getEstadoLabel(selectedReserva.estado) }}
                  </span>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Inicio</p>
                  <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(selectedReserva.fechaReservaInicio) }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Fin</p>
                  <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(selectedReserva.fechaReservaFin) }}</p>
                </div>
              </div>
            </ng-container>
            <ng-template #sinReservaDash>
              <p class="text-sm text-slate-600 dark:text-slate-400">No hay una reserva asociada cargada.</p>
            </ng-template>
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
export class OrganizadorDashboardComponent implements OnInit {
  solicitudes: Solicitud[] = [];
  reservas: Reserva[] = [];
  loading = true;
  error: string | null = null;
  userName = '';
  idOrganizador = 14; // Por defecto, se puede obtener del backend más adelante

  // Estadísticas
  get solicitudesEnviadas(): number {
    return this.solicitudes.length;
  }

  get reservasConfirmadas(): number {
    // Contar reservas confirmadas y pendientes (todas las reservas activas)
    return this.reservas.filter(r => 
      r.estado?.toUpperCase() === 'CONFIRMADA' || r.estado?.toUpperCase() === 'PENDIENTE'
    ).length;
  }

  get accionesPendientes(): number {
    return this.solicitudes.filter(s => s.estadoSolicitud === 'PENDIENTE').length;
  }

  // Solicitudes recientes (últimas 3)
  get solicitudesRecientes(): Solicitud[] {
    return this.solicitudes
      .slice() // copia para no mutar
      .sort((a, b) => new Date(b.fechaSolicitud).getTime() - new Date(a.fechaSolicitud).getTime())
      .slice(0, 3);
  }

  // Próximas reservas (ordenadas por fecha de inicio) - incluye CONFIRMADA y PENDIENTE
  get proximasReservas(): Reserva[] {
    return this.reservas
      .filter(r => {
        const estadoUpper = r.estado?.toUpperCase() || '';
        return estadoUpper === 'CONFIRMADA' || estadoUpper === 'PENDIENTE';
      })
      .sort((a, b) => new Date(a.fechaReservaInicio).getTime() - new Date(b.fechaReservaInicio).getTime())
      .slice(0, 3);
  }

  private normalizeReserva(r: any): Reserva {
    const toNum = (v: any) => (v === null || v === undefined ? v : Number(v));
    return {
      ...r,
      idReserva: toNum(r.idReserva),
      idSolicitud: toNum(r.idSolicitud || r.id_solicitud || r.solicitudId),
      estado: r.estado || r.estadoReserva || '',
      fechaReservaInicio: r.fechaReservaInicio || r.fechaReserva || r.fechaInicio || '',
      fechaReservaFin: r.fechaReservaFin || r.fechaReserva || r.fechaFin || '',
      fechaCreacion: r.fechaCreacion || r.fecha_creacion || '',
      fechaActualizacion: r.fechaActualizacion || r.fecha_actualizacion || ''
    } as Reserva;
  }

  constructor(
    private keycloak: KeycloakService,
    private solicitudesService: SolicitudesService,
    private reservasService: ReservasService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    try {
      // Obtener nombre de usuario desde Keycloak (sin cargar perfil completo)
      const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
      this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Usuario';
      
      // Por ahora usamos un ID fijo. TODO: Obtener del backend según el usuario autenticado
      this.idOrganizador = 14;

      // Cargar reservas del organizador directamente (nuevo endpoint)
      console.log('Cargando reservas del organizador en dashboard (endpoint directo):', this.idOrganizador);
      this.reservasService.getByOrganizador(this.idOrganizador).subscribe({
        next: (reservas) => {
          this.reservas = Array.isArray(reservas) ? reservas.map((r: any) => this.normalizeReserva(r)) : [];
          // Opcional: cargar solicitudes del organizador para métricas del dashboard
          this.solicitudesService.getByOrganizador(this.idOrganizador).subscribe({
            next: (solicitudes) => {
              this.solicitudes = Array.isArray(solicitudes) ? solicitudes : [];
              this.loading = false;
              this.cdr.detectChanges();
            },
            error: () => {
              // Si falla la carga de solicitudes, seguimos mostrando reservas
              this.loading = false;
              this.cdr.detectChanges();
            }
          });
        },
        error: (err) => {
          console.error('Error cargando reservas del organizador en dashboard:', err);
          this.error = 'Error al cargar las reservas: ' + (err.message || err.statusText || 'Error desconocido');
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } catch (err) {
      console.error('Error en ngOnInit:', err);
      this.error = 'Error al inicializar el dashboard';
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  getEstadoClass(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE':
        return 'text-yellow-600 dark:text-yellow-400 bg-yellow-100 dark:bg-yellow-900/50';
      case 'APROBADA':
      case 'CONFIRMADA':
        return 'text-green-600 dark:text-green-400 bg-green-100 dark:bg-green-900/50';
      case 'RECHAZADA':
      case 'CANCELADA':
        return 'text-red-600 dark:text-red-400 bg-red-100 dark:bg-red-900/50';
      default:
        return 'text-gray-600 dark:text-gray-400 bg-gray-100 dark:bg-gray-900/50';
    }
  }

  getEstadoIcon(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE':
        return 'hourglass_top';
      case 'APROBADA':
      case 'CONFIRMADA':
        return 'check_circle';
      case 'RECHAZADA':
      case 'CANCELADA':
        return 'cancel';
      default:
        return 'info';
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
      month: 'short',
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

  getMonthName(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { month: 'short' }).toUpperCase();
  }

  getDay(dateString: string): string {
    const date = new Date(dateString);
    return date.getDate().toString();
  }

  buscarOfertas() {
    this.router.navigate(['/solicitud-reserva']);
  }

  // Modal de detalle de solicitud (simple)
  modalVisible = false;
  selectedSolicitud: Solicitud | null = null;
  selectedReserva: Reserva | null = null;

  verDetalleSolicitud(solicitud: Solicitud) {
    this.selectedSolicitud = solicitud;
    const idSolNum = Number(solicitud.idSolicitud);
    // Busca la reserva asociada si ya está cargada
    this.selectedReserva = this.reservas.find(r => Number(r.idSolicitud) === idSolNum) || null;
    this.modalVisible = true;
  }

  cerrarModal() {
    this.modalVisible = false;
    this.selectedSolicitud = null;
    this.selectedReserva = null;
  }

  // Navegación a listados completos
  verMisSolicitudes(): void {
    this.router.navigate(['/solicitudes/organizador']);
  }

  verMisReservas(): void {
    this.router.navigate(['/reservas/organizador']);
  }
}
