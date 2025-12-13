import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SolicitudesService } from '../services/solicitudes.service';
import { ReservasService } from '../services/reservas.service';
import { Solicitud } from '../models/solicitud.model';
import { Reserva } from '../models/reserva.model';
import { forkJoin } from 'rxjs';
import { SolicitudDetalleComponent } from '../components/solicitud-detalle/solicitud-detalle.component';
import { ResponderSolicitudComponent } from '../components/solicitud-detalle/app-responder-solicitud';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model';
import { OfertaCardComponent } from '../components/oferta-card/oferta-card.component';


@Component({
  selector: 'app-proveedor-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, SolicitudDetalleComponent, ResponderSolicitudComponent, OfertaCardComponent],

  template: `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
    <div class="relative flex h-auto min-h-screen w-full flex-col bg-background-light dark:bg-background-dark overflow-x-hidden">
      <div class="flex min-h-screen">
        <aside class="flex w-64 flex-col gap-y-4 border-r border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 p-4">
          <div class="flex items-center gap-4 text-slate-900 dark:text-white px-3 py-2">
            <span class="material-symbols-outlined text-3xl text-primary">hub</span>
            <h2 class="text-lg font-bold tracking-[-0.015em]">EvenPro</h2>
          </div>
          <div class="flex flex-col justify-between grow">
            <div class="flex flex-col gap-2 mt-4">
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20">
                <span class="material-symbols-outlined text-slate-900 dark:text-white">dashboard</span>
                <p class="text-slate-900 dark:text-white text-sm font-medium leading-normal">Dashboard</p>
              </div>
              <a routerLink="/proveedor/solicitudes" class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">event</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Solicitudes</p>
              </a>


              <a routerLink="/proveedor/no-disponibilidades" class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">calendar_today</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Calendario</p>
              </a>
              <a routerLink="/proveedor/reservas" class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">event</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Reservas</p>
              </a>
              <a routerLink="/proveedor/ofertas" 
                class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">sell</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">
                  Mis Ofertas
                </p>
              </a>
              <div class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer">
                <span class="material-symbols-outlined text-slate-600 dark:text-slate-300">person</span>
                <p class="text-slate-600 dark:text-slate-300 text-sm font-medium leading-normal">Perfil</p>
              </div>
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
          <header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-slate-200 dark:border-slate-800 px-10 py-3 bg-white dark:bg-slate-900">
            <div class="flex items-center gap-8">
              <label class="relative flex flex-col min-w-40 !h-10 max-w-64">
                <div class="absolute inset-y-0 left-0 flex items-center pl-4 text-slate-500 dark:text-slate-400">
                  <span class="material-symbols-outlined">search</span>
                </div>
                <input class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-slate-900 dark:text-white focus:outline-0 focus:ring-0 border-none bg-background-light dark:bg-background-dark focus:border-none h-full placeholder:text-slate-500 dark:placeholder:text-slate-400 pl-12 text-base font-normal leading-normal" placeholder="Search..." />
              </label>
            </div>
            <div class="flex flex-1 justify-end gap-4 items-center">
              <button class="flex cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 w-10 bg-background-light dark:bg-background-dark text-slate-600 dark:text-slate-300 relative">
                <span class="material-symbols-outlined">notifications</span>
                <div class="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-red-500 rounded-full border-2 border-white dark:border-slate-900"></div>
              </button>
              <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10" style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuBcMde1moEravtwv8XA6MOPpaHD_wujI5iVhwujYmnKWi0y51eW-US7EBTEZniLaxw9mmLZqQiYcXk6cYA0VKkOAd-nnbn4OaioFUhPadX-OGcvy6Ijl1tqCV3gmp1qthr_xa2L6HlCkd78w_IkyM1gzDocIbeT5aAwunNgyPXGjNidMKE9ePJTTkODravc8tQ4-mFwCjxgiW7Mq8Nfv4De5YF8thrxFMYFvWhG-ZYmVHPHEJMJ8Mox28ZQ9-BbAf8pzfsYnGQrOenQ');"></div>
            </div>
          </header>

          <div class="p-10">
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
              <div class="flex flex-wrap justify-between gap-3 items-center">
                <p class="text-slate-900 dark:text-white text-4xl font-black leading-tight tracking-[-0.033em] min-w-72">Dashboard</p>
                <button 
                  routerLink="/proveedor/ofertas/crear"
                  class="bg-primary text-white font-bold py-2.5 px-6 rounded-lg flex items-center gap-2">
                  <span class="material-symbols-outlined">add_circle</span>
                  Crear Nueva Oferta
                </button>
              </div>

              <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mt-8">
                <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                  <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Solicitudes Pendientes</p>
                  <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">{{ solicitudesPendientes }}</p>
                </div>
                <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                  <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Reservas Activas</p>
                  <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">{{ reservasConfirmadas }}</p>
                </div>
                <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                  <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Mensajes Sin Leer</p>
                  <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">{{ mensajesSinLeer }}</p>
                </div>
                <div class="flex flex-col gap-2 rounded-xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800">
                  <p class="text-slate-600 dark:text-slate-300 text-base font-medium leading-normal">Ingresos del Mes</p>
                  <p class="text-slate-900 dark:text-white tracking-light text-3xl font-bold leading-tight">{{ ingresosDelMes === 0 ? '$0' : '$' + ingresosDelMes.toLocaleString() }}</p>
                </div>
              </div>

            <div class="mt-8 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800">
              <div class="pb-3 pt-2">
                <div class="flex border-b border-slate-200 dark:border-slate-800 px-6 gap-8">
                  <button (click)="setActiveTab('solicitudes')" 
                          [class]="'flex flex-col items-center justify-center border-b-[3px] pb-[13px] pt-4 ' + (activeTab === 'solicitudes' ? 'border-b-primary text-slate-900 dark:text-white' : 'border-b-transparent text-slate-500 dark:text-slate-400')">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">Nuevas Solicitudes</p>
                  </button>
                  <button (click)="setActiveTab('reservas')" 
                          [class]="'flex flex-col items-center justify-center border-b-[3px] pb-[13px] pt-4 ' + (activeTab === 'reservas' ? 'border-b-primary text-slate-900 dark:text-white' : 'border-b-transparent text-slate-500 dark:text-slate-400')">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">Proximas Reservas</p>
                  </button>
                  <button (click)="setActiveTab('historial')" 
                          [class]="'flex flex-col items-center justify-center border-b-[3px] pb-[13px] pt-4 ' + (activeTab === 'historial' ? 'border-b-primary text-slate-900 dark:text-white' : 'border-b-transparent text-slate-500 dark:text-slate-400')">
                    <p class="text-sm font-bold leading-normal tracking-[0.015em]">Historial</p>
                  </button>
                </div>
              </div>

              <!-- Tab Nuevas Solicitudes -->
              <div *ngIf="activeTab === 'solicitudes'" class="overflow-x-auto">
                <!-- Sin solicitudes -->
                <div *ngIf="nuevasSolicitudes.length === 0" class="text-center py-12">
                  <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">inbox</span>
                  <p class="mt-4 text-slate-500 dark:text-slate-400">No tienes solicitudes nuevas</p>
                </div>

                <!-- Con solicitudes -->
                <table *ngIf="nuevasSolicitudes.length > 0" class="w-full text-left">
                  <thead class="border-b border-slate-200 dark:border-slate-800">
                    <tr>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Solicitud</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400 text-right">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let solicitud of nuevasSolicitudes" 
                        class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                      <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">#{{ solicitud.idSolicitud }}</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(solicitud.fechaSolicitud) }}</td>
                      <td class="p-6 text-sm">
                        <span [class]="'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ' + getEstadoClass(solicitud.estadoSolicitud)">
                          {{ getEstadoLabel(solicitud.estadoSolicitud) }}
                        </span>
                      </td>
                      <td class="p-6 text-right space-x-2">
                        <button 
  (click)="verDetalle(solicitud)"
  class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">
  Ver Detalle
</button>


                        <button
  (click)="abrirResponder(solicitud)"
  class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
>
  Responder
</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Tab Reservas -->
              <div *ngIf="activeTab === 'reservas'" class="overflow-x-auto">
                <!-- Sin reservas -->
                <div *ngIf="proximasReservas.length === 0" class="text-center py-12">
                  <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">event_busy</span>
                  <p class="mt-4 text-slate-500 dark:text-slate-400">No tienes reservas próximas</p>
                </div>

                <!-- Con reservas -->
                <table *ngIf="proximasReservas.length > 0" class="w-full text-left">
                  <thead class="border-b border-slate-200 dark:border-slate-800">
                    <tr>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">ID Reserva</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Inicio</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Fecha Fin</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400">Estado</th>
                      <th class="p-6 text-sm font-semibold text-slate-500 dark:text-slate-400 text-right">Acciones</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr *ngFor="let reserva of proximasReservas" 
                        class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                      <td class="p-6 text-sm font-medium text-slate-800 dark:text-slate-100">#{{ reserva.idReserva }}</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(reserva.fechaReservaInicio) }}</td>
                      <td class="p-6 text-sm text-slate-600 dark:text-slate-300">{{ formatDate(reserva.fechaReservaFin) }}</td>
                      <td class="p-6 text-sm">
                        <span [class]="'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ' + getEstadoClass(reserva.estado)">
                          {{ getEstadoLabel(reserva.estado) }}
                        </span>
                      </td>
                      <td class="p-6 text-right space-x-2">
                        <td class="p-6 text-right space-x-2">
                          <button 
                            (click)="verDetalleReserva(reserva)"
                            class="text-slate-600 dark:text-slate-300 hover:text-primary dark:hover:text-primary text-sm font-bold py-2 px-4 rounded-lg border border-slate-300 dark:border-slate-700">
                            Ver Detalle
                          </button>
                        </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Tab Historial -->
              <div *ngIf="activeTab === 'historial'" class="overflow-x-auto">
                <div class="text-center py-12">
                  <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">history</span>
                  <p class="mt-4 text-slate-500 dark:text-slate-400">Historial en desarrollo</p>
                </div>
              </div>
            </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
  <app-solicitud-detalle 
  *ngIf="modalVisible" 
  [solicitud]="selectedSolicitud" 
  [reserva]="selectedReserva"
  (close)="cerrarModal()">
</app-solicitud-detalle>
<app-responder-solicitud
  *ngIf="modalResponderVisible"
  [solicitud]="selectedSolicitudResponder"
  (close)="cerrarResponder()"
  (updated)="actualizarSolicitud($event)"
>
</app-responder-solicitud>


  <!-- Modal de Detalle (mismo estilo que reservas list) -->
  <div *ngIf="mostrarModal" (click)="cerrarModalReserva()" 
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
        <button (click)="cerrarModalReserva()" 
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
        <button (click)="cerrarModalReserva()" 
                class="px-6 py-2.5 rounded-lg text-slate-700 dark:text-slate-200 bg-white dark:bg-slate-800 border border-slate-300 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 font-semibold transition-colors">
          Cerrar
        </button>
      </div>
    </div>
  </div>

  `
})
export class ProveedorDashboardComponent implements OnInit {
  solicitudes: Solicitud[] = [];
  reservas: Reserva[] = [];
  loading = true;
  error: string | null = null;
  userName = '';
  idProveedor = 1; // Por defecto
  misOfertas: Oferta[] = [];


    ngOnInit(): void {
    try {
      // Obtener nombre de usuario desde Keycloak
      const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
      this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Proveedor';
      
      this.idProveedor = 1;

      // Cargar solicitudes del proveedor
      console.log('Cargando solicitudes para proveedor:', this.idProveedor);
      this.solicitudesService.getByProveedor(this.idProveedor).subscribe({
        //this.solicitudesService.getAll().subscribe({
        next: (solicitudes) => {
          console.log('Solicitudes recibidas:', solicitudes);
          this.solicitudes = solicitudes;
          
          // Cargar reservas para cada solicitud
          if (solicitudes.length > 0) {
            const reservaRequests = solicitudes.map(s => 
              this.reservasService.getByIdSolicitud(s.idSolicitud)
            );
            
            forkJoin(reservaRequests).subscribe({
              next: (reservasArrays) => {
                this.reservas = reservasArrays.flat();
                this.loading = false;
                this.cdr.detectChanges();
              },
              error: (err) => {
                console.error('Error cargando reservas:', err);
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
      this.error = 'Error al inicializar el dashboard';
      this.loading = false;
      this.cdr.detectChanges();
    }
    //MIS OFERTAS
    this.ofertasService.getOfertasPorProveedor(this.idProveedor).subscribe({
      next: (ofertas) => {
        this.misOfertas = ofertas;  // Cambié 'ofertas' por 'misOfertas'
        console.log('Ofertas del proveedor:', this.misOfertas);
      },
      error: (err) => console.error('Error cargando ofertas del proveedor', err)
    });
  }
  // Estadísticas
  get solicitudesPendientes(): number {
    return this.solicitudes.filter(s => s.estadoSolicitud?.toUpperCase() === 'PENDIENTE').length;
  }

  get reservasConfirmadas(): number {
    const ahora = new Date();
    return this.reservas.filter(r => {
      const estadoUpper = r.estado?.toUpperCase() || '';
      const esFutura = new Date(r.fechaReservaInicio) >= ahora;
      return (estadoUpper === 'CONFIRMADA' || estadoUpper === 'PENDIENTE') && esFutura;
    }).length;
  }

  get mensajesSinLeer(): number {
    // TODO: Implementar cuando se tenga el servicio de mensajes
    return 0;
  }

  get ingresosDelMes(): number {
    // TODO: Calcular desde las reservas confirmadas
    return 0;
  }

  // Las 3 solicitudes pendientes más recientes (ordenadas por fecha descendente)
  get nuevasSolicitudes(): Solicitud[] {
    return this.solicitudes
      .filter(s => s.estadoSolicitud?.toUpperCase() === 'PENDIENTE')
      .sort((a, b) => new Date(b.fechaSolicitud).getTime() - new Date(a.fechaSolicitud).getTime())
      .slice(0, 3);
  }

  // Próximas reservas desde la fecha actual (solo futuras, hasta 3)
  get proximasReservas(): Reserva[] {
    const ahora = new Date();
    return this.reservas
      .filter(r => {
        const estadoUpper = r.estado?.toUpperCase() || '';
        const esFutura = new Date(r.fechaReservaInicio) >= ahora;
        return (estadoUpper === 'CONFIRMADA' || estadoUpper === 'PENDIENTE') && esFutura;
      })
      .sort((a, b) => new Date(a.fechaReservaInicio).getTime() - new Date(b.fechaReservaInicio).getTime())
      .slice(0, 3);
  }

  // Estado de la pestaña activa
  activeTab: 'solicitudes' | 'reservas' | 'historial' = 'solicitudes';

  setActiveTab(tab: 'solicitudes' | 'reservas' | 'historial'): void {
    this.activeTab = tab;
  }

  constructor(
    private keycloak: KeycloakService,
    private solicitudesService: SolicitudesService,
    private reservasService: ReservasService,
    private ofertasService: OfertasService, 
    private cdr: ChangeDetectorRef
  ) {}



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

  // Modal
modalVisible = false;
selectedSolicitud: Solicitud | null = null;
selectedReserva: Reserva | null = null;

verDetalle(solicitud: Solicitud) {
  this.selectedSolicitud = solicitud;
  // Buscar reserva asociada
  this.selectedReserva = this.reservas.find(r => r.idSolicitud === solicitud.idSolicitud) || null;
  this.modalVisible = true;
}

cerrarModal() {
  this.modalVisible = false;
  this.selectedSolicitud = null;
  this.selectedReserva = null;
}

modalResponderVisible = false;
selectedSolicitudResponder: Solicitud | null = null;

abrirResponder(solicitud: Solicitud) {
  this.selectedSolicitudResponder = solicitud;
  this.modalResponderVisible = true;
}

cerrarResponder() {
  this.modalResponderVisible = false;
  this.selectedSolicitudResponder = null;
}

actualizarSolicitud(solicitudActualizada: Solicitud) {
  // Actualizar la lista de solicitudes localmente
  const index = this.solicitudes.findIndex(s => s.idSolicitud === solicitudActualizada.idSolicitud);
  if (index !== -1) this.solicitudes[index] = solicitudActualizada;
}

// Modal reservas con solicitud (mismo estilo que lista)
mostrarModal = false;
loadingDetalle = false;
reservaSeleccionada: Reserva | null = null;
solicitudSeleccionada: Solicitud | null = null;

verDetalleReserva(reserva: Reserva) {
  this.reservaSeleccionada = reserva;
  this.loadingDetalle = true;
  this.mostrarModal = true;
  // Cargar solicitud asociada vía endpoint de reservas
  this.reservasService.getSolicitudByReservaId(reserva.idReserva).subscribe({
    next: (solicitud) => {
      this.solicitudSeleccionada = solicitud;
      this.loadingDetalle = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error('Error cargando solicitud de reserva en dashboard:', err);
      // Fallback: buscar en solicitudes cargadas
      const s = this.solicitudes.find(x => x.idSolicitud === reserva.idSolicitud);
      if (s) this.solicitudSeleccionada = s;
      this.loadingDetalle = false;
      this.cdr.detectChanges();
    }
  });
}

cerrarModalReserva() {
  this.mostrarModal = false;
  this.reservaSeleccionada = null;
  this.solicitudSeleccionada = null;
}


}
