import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SolicitudesService } from '../services/solicitudes.service';
import { Solicitud } from '../models/solicitud.model';

@Component({
  selector: 'app-proveedor-solicitudes-list',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex min-h-screen w-full">
      <!-- Sidebar -->
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
                <span class="material-symbols-outlined text-slate-900 dark:text-white">description</span>
                <p class="text-slate-900 dark:text-white text-sm font-medium">Solicitudes</p>
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
              <h1 class="text-3xl font-black text-slate-900 dark:text-white">Todas las Solicitudes</h1>
              <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">Gestiona todas tus solicitudes</p>
            </div>
          </div>
        </header>

        <div class="p-10">
          <!-- Loading -->
          <div *ngIf="loading" class="flex items-center justify-center py-12">
            <div class="text-center">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
              <p class="mt-4 text-gray-600 dark:text-gray-400">Cargando solicitudes...</p>
            </div>
          </div>

          <!-- Error -->
          <div *ngIf="error && !loading" class="bg-red-100 dark:bg-red-900/50 border border-red-400 dark:border-red-700 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
            {{ error }}
          </div>

          <!-- Tabla de Solicitudes -->
          <div *ngIf="!loading && !error" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 overflow-x-auto">
            <table class="w-full text-left">
              <thead class="border-b border-slate-200 dark:border-slate-800">
                <tr>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</th>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha</th>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Organizador</th>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Proveedor</th>
                  <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400 text-right">Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let s of solicitudes" class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                  <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">#{{ s.idSolicitud }}</td>
                  <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(s.fechaSolicitud) }}</td>
                  <td class="p-6 text-sm">
                    <span [class]="'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ' + getEstadoClass(s.estadoSolicitud)">
                      {{ getEstadoLabel(s.estadoSolicitud) }}
                    </span>
                  </td>
                  <td class="p-6 text-sm text-slate-600 dark:text-slate-300">#{{ s.idOrganizador }}</td>
                  <td class="p-6 text-sm text-slate-600 dark:text-slate-300">#{{ s.idProovedor }}</td>
                  <td class="p-6 text-right space-x-2">
                    <button (click)="verDetalle(s)" class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">
                      Ver Detalle
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>

    <!-- Modal de Detalle -->
    <div *ngIf="mostrarModal" (click)="cerrarModal()" class="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div (click)="$event.stopPropagation()" class="bg-white dark:bg-slate-900 rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-auto flex flex-col">
        <div class="flex items-center justify-between px-6 py-4 border-b border-slate-200 dark:border-slate-800 bg-primary/5">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined text-3xl text-primary">description</span>
            <div>
              <h2 class="text-2xl font-bold text-slate-900 dark:text-white">Detalle de Solicitud</h2>
              <p class="text-sm text-slate-500 dark:text-slate-400">Información completa</p>
            </div>
          </div>
          <button (click)="cerrarModal()" class="p-2 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg transition-colors">
            <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">close</span>
          </button>
        </div>

        <div class="flex-1 p-6">
          <div *ngIf="solicitudSeleccionada">
            <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 border border-slate-200 dark:border-slate-700">
              <h3 class="text-xl font-bold mb-4">Información de la Solicitud</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                  <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idSolicitud }}</p>
                </div>
                <div>
                  <p class="text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</p>
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
          <div *ngIf="loadingDetalle" class="text-center mt-6">Cargando detalle...</div>
        </div>
      </div>
    </div>
  </div>
  `
})
export class ProveedorSolicitudesListComponent implements OnInit {
  solicitudes: Solicitud[] = [];
  loading = true;
  error: string | null = null;
  userName = '';
  idProveedor = 1;

  mostrarModal = false;
  loadingDetalle = false;
  solicitudSeleccionada: Solicitud | null = null;

  constructor(
    private router: Router,
    private keycloak: KeycloakService,
    private solicitudesService: SolicitudesService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    try {
      const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
      this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Proveedor';
      this.idProveedor = 1;

      this.fetchSolicitudes();
    } catch (err) {
      console.error(err);
      this.error = 'Error al inicializar';
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

 /* fetchSolicitudes() {
    this.loading = true;
    this.error = null;
    this.solicitudesService.getByProveedor(this.idProveedor).subscribe({
      next: (data) => {
        this.solicitudes = data || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.error = 'Error cargando solicitudes';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }*/

    fetchSolicitudes() {
  this.loading = true;
  this.error = null;
  this.solicitudesService.getAll().subscribe({
    next: (data) => {
      this.solicitudes = data || [];
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error(err);
      this.error = 'Error cargando solicitudes';
      this.loading = false;
      this.cdr.detectChanges();
    }
  });
}


  verDetalle(s: Solicitud) {
    this.solicitudSeleccionada = s;
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
    this.solicitudSeleccionada = null;
  }

  volverDashboard() {
    this.router.navigate(['/dashboard/proveedor']);
  }

  getEstadoClass(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE': return 'bg-orange-100 text-orange-800';
      case 'EN_NEGOCIACION': return 'bg-blue-100 text-blue-800';
      case 'APROBADA': case 'CONFIRMADA': return 'bg-green-100 text-green-800';
      case 'RECHAZADA': case 'CANCELADA': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getEstadoLabel(estado: string): string {
    const estadoUpper = estado?.toUpperCase() || '';
    switch (estadoUpper) {
      case 'PENDIENTE': return 'Pendiente';
      case 'EN_NEGOCIACION': return 'En Negociación';
      case 'APROBADA': return 'Aprobada';
      case 'CONFIRMADA': return 'Confirmada';
      case 'RECHAZADA': return 'Rechazada';
      case 'CANCELADA': return 'Cancelada';
      default: return estado || 'Desconocido';
    }
  }

  formatDate(dateString: string) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }

  formatDateLong(dateString: string) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', { day: '2-digit', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  }
}
