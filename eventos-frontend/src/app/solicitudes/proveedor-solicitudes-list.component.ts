import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common'; // 🟢 Añadir CurrencyPipe
import { Router, RouterLink } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { SolicitudesService } from '../services/solicitudes.service';
// 🟢 Servicios y Tipos del detalle avanzado
import { ReservasService } from '../services/reservas.service';
import { OfertasService } from '../services/ofertas.service';
import { Oferta } from '../models/oferta.model'; 
import { Solicitud } from '../models/solicitud.model';
// -----------------------------------------------------
import { FormsModule } from '@angular/forms';
import { ResponderSolicitudComponent } from '../components/solicitud-detalle/app-responder-solicitud';
import { finalize, timeout } from 'rxjs/operators';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-proveedor-solicitudes-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ResponderSolicitudComponent, RouterLink, CurrencyPipe],
  // ... (Template pegado arriba) ...
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
        
        <div class="mb-6 bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
          <div class="flex flex-wrap items-center gap-4">
            
          
            
            
            <div class="flex items-center gap-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">Filtrar por Estado:</label>
              <select [(ngModel)]="filtroEstado" (change)="aplicarFiltros()"
                      class="px-3 py-2 rounded-lg border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100 text-sm font-medium">
                <option value="">Todos los Estados</option>
                <option value="PENDIENTE">Pendiente</option>
                <option value="APROBADA">Aprobada</option>
                <option value="RECHAZADA">Rechazada</option>
              </select>
            </div>

            <div class="text-sm text-slate-600 dark:text-slate-400 ml-auto">
              Mostrando <span class="font-semibold">{{ solicitudesFiltradas.length }}</span> de {{ solicitudesOriginal.length }} solicitudes
            </div>
          </div>
        </div>
        
        <div *ngIf="loading" class="flex items-center justify-center py-12">
          <div class="text-center">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
            <p class="mt-4 text-gray-600 dark:text-gray-400">Cargando solicitudes...</p>
          </div>
        </div>

        <div *ngIf="error && !loading" class="bg-red-100 dark:bg-red-900/50 border border-red-400 dark:border-red-700 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
          {{ error }}
        </div>

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
              <tr *ngFor="let s of solicitudesFiltradas" class="border-b border-slate-200 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
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
                  <button
                    *ngIf="s.estadoSolicitud?.toUpperCase() === 'PENDIENTE'"
                    (click)="abrirResponder(s)"
                    class="bg-primary hover:bg-primary/90 text-white px-3 py-1.5 rounded-lg text-sm font-semibold"
                  >
                    Responder
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div *ngIf="mostrarModal" (click)="cerrarModal()" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
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

                <div *ngIf="loadingDetalle" class="flex items-center justify-center py-6">
                  <div class="animate-spin rounded-full h-10 w-10 border-b-2 border-primary"></div>
                </div>
                
                <div *ngIf="errorDetalle && !loadingDetalle" class="mb-4 bg-red-100 dark:bg-red-900/40 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 px-4 py-3 rounded">
                  {{ errorDetalle }}
                </div>

                <div *ngIf="!loadingDetalle">
                  
                  <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 mb-6 border border-slate-200 dark:border-slate-700">
                    <h3 class="text-xl font-bold text-slate-800 dark:text-slate-100 mb-4">Información de la Solicitud</h3>
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idSolicitud }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Estado</p>
                        <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(solicitudSeleccionada.estadoSolicitud)">
                          {{ getEstadoLabel(solicitudSeleccionada.estadoSolicitud) }}
                        </span>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Fecha de Solicitud</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ formatDateLong(solicitudSeleccionada.fechaSolicitud) }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Organizador</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idOrganizador }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Proveedor</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idProovedor }}</p>
                      </div>
                      
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Oferta (en Solicitud)</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ solicitudSeleccionada.idOferta }}</p>
                      </div>
                    </div>
                  </div>

                  <div *ngIf="solicitudSeleccionada.idOferta" class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 mb-6 border border-slate-200 dark:border-slate-700">
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
                        <p class="text-base text-slate-900 dark:text-white">{{ ofertaAsociada.precioBase | currency:'USD':'symbol':'1.0-0' }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Estado de Oferta</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ ofertaAsociada.estado }}</p>
                      </div>
                    </div>
                  </div>

                  <div class="bg-slate-50 dark:bg-slate-800/50 rounded-lg p-6 mb-6 border border-slate-200 dark:border-slate-700">
                    <h3 class="text-xl font-bold text-slate-800 dark:text-slate-100 mb-4">Reserva Asociada</h3>
                    <div *ngIf="reservaAsociada; else sinReserva" class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Reserva</p>
                        <p class="text-base text-slate-900 dark:text-white">#{{ reservaAsociada?.idReserva }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Estado</p>
                        <span [class]="'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium mt-1 ' + getEstadoClass(reservaAsociada?.estadoReserva || '')">
                          {{ getEstadoLabel(reservaAsociada?.estadoReserva || '') || 'No informado' }}
                        </span>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Solicitud</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.idSolicitud ? ('#' + reservaAsociada.idSolicitud) : 'No informado' }}</p>
                      </div>
                      
                      <div class="md:col-span-3 h-px bg-slate-200 dark:bg-slate-700 my-2"></div>

                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Fecha de Inicio</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.fechaReservaInicio ? formatDateLong(reservaAsociada.fechaReservaInicio) : 'No informado' }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Fecha de Fin</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.fechaReservaFin ? formatDateLong(reservaAsociada.fechaReservaFin) : 'No informado' }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Proveedor</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.idProveedor ? ('#' + reservaAsociada.idProveedor) : 'No informado' }}</p>
                      </div>

                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">ID Organizador</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.idOrganizador ? ('#' + reservaAsociada.idOrganizador) : 'No informado' }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Fecha de Creación</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.fechaCreacion ? formatDateLong(reservaAsociada.fechaCreacion) : 'No informado' }}</p>
                      </div>
                      <div>
                        <p class="font-semibold text-slate-500 dark:text-slate-400">Última Actualización</p>
                        <p class="text-base text-slate-900 dark:text-white">{{ reservaAsociada?.fechaActualizacion ? formatDateLong(reservaAsociada.fechaActualizacion) : 'No informado' }}</p>
                      </div>
                    </div>
                    <ng-template #sinReserva>
                      <p class="text-sm text-slate-600 dark:text-slate-400">No hay una reserva asociada a esta solicitud.</p>
                    </ng-template>
                  </div>
                </div>
              </div>
            </div>             <div class="flex justify-end gap-3 px-6 py-4 border-t border-slate-200 dark:border-slate-800">
              <button (click)="cerrarModal()" class="px-4 py-2 rounded-lg text-sm font-semibold bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700">Cerrar</button>
            </div>
          </div>
        </div>
        
        <app-responder-solicitud
          *ngIf="modalResponderVisible"
          [solicitud]="selectedSolicitudResponder"
          (close)="cerrarResponder()"
          (updated)="actualizarSolicitud($event)"
        >
        </app-responder-solicitud>
      </div>
    </main>
  </div>
</div>
`
})
export class ProveedorSolicitudesListComponent implements OnInit {
  solicitudes: Solicitud[] = [];
  // 🟢 Propiedades de filtrado
  solicitudesFiltradas: Solicitud[] = [];
  solicitudesOriginal: Solicitud[] = [];
  filtroEstado: string = '';
  mesSeleccionado = 0; // 0 = todos
  meses = [
    { value: 1, name: 'Enero' },
    { value: 2, name: 'Febrero' },
    { value: 3, name: 'Marzo' },
    { value: 4, name: 'Abril' },
    { value: 5, name: 'Mayo' },
    { value: 6, name: 'Junio' },
    { value: 7, name: 'Julio' },
    { value: 8, name: 'Agosto' },
    { value: 9, name: 'Septiembre' },
    { value: 10, name: 'Octubre' },
    { value: 11, name: 'Noviembre' },
    { value: 12, name: 'Diciembre' }
  ];
  // ------------------------------------

  loading = true;
  error: string | null = null;
  userName = '';
  idProveedor = 1;

  // 🟢 Propiedades del modal de detalle
  mostrarModal = false;
  loadingDetalle = false;
  errorDetalle: string | null = null;
  solicitudSeleccionada: Solicitud | null = null;
  reservaAsociada: any = null;
  
  // 🟢 Propiedades de Oferta
  ofertaAsociada: Oferta | null = null;
  loadingOferta = false;
  errorOferta: string | null = null;
  // ------------------------------------

  // Propiedades para Responder Solicitud
  modalResponderVisible = false;
  selectedSolicitudResponder: Solicitud | null = null;

  constructor(
    private router: Router,
    private keycloak: KeycloakService,
    private solicitudesService: SolicitudesService,
    // 🟢 Inyectar servicios de detalle
    private reservasService: ReservasService,
    private ofertasService: OfertasService,
    // ------------------------------------
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

  fetchSolicitudes() {
      this.loading = true;
      this.error = null;

      this.solicitudesService.getByProveedor(this.idProveedor).subscribe({
        next: (data) => {
          this.solicitudesOriginal = data || [];
          this.solicitudesFiltradas = [...this.solicitudesOriginal]; // 🟢 Inicializar filtradas
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


  // 🟢 Método de filtrado
  aplicarFiltros(): void {
    let tempSolicitudes = [...this.solicitudesOriginal];

    // 1. Filtrar por Mes
    if (this.mesSeleccionado !== 0) {
      tempSolicitudes = tempSolicitudes.filter(solicitud => {
        const fecha = new Date(solicitud.fechaSolicitud);
        return fecha.getMonth() + 1 === this.mesSeleccionado;
      });
    }

    // 2. Filtrar por Estado
    if (this.filtroEstado) {
      tempSolicitudes = tempSolicitudes.filter(s => 
        s.estadoSolicitud?.toUpperCase() === this.filtroEstado.toUpperCase()
      );
    }

    // 3. Ordenar (por fecha de solicitud descendente)
    this.solicitudesFiltradas = tempSolicitudes.sort((a, b) => 
        new Date(b.fechaSolicitud).getTime() - new Date(a.fechaSolicitud).getTime()
    );
  }

  // 🟢 Método de filtrado por Mes (Llamado desde el HTML, llama al método principal)
  filtrarPorMes() {
      this.aplicarFiltros();
  }


  // 🟢 Método para ver Detalle (Actualizado para cargar Reserva y Oferta)
  verDetalle(solicitud: Solicitud): void {
      this.solicitudSeleccionada = solicitud;
      this.mostrarModal = true;
      this.errorDetalle = null;
      this.reservaAsociada = null;
      this.ofertaAsociada = null; 
      this.loadingOferta = false;
      this.errorOferta = null;

      // 🟢 PREPARAR LLAMADAS ASÍNCRONAS
      const calls: any = {
          reserva: this.reservasService.getByIdSolicitud(solicitud.idSolicitud).pipe(timeout(10000)),
      };

      if (solicitud.idOferta) {
          calls.oferta = this.ofertasService.getOfertaById(solicitud.idOferta).pipe(timeout(10000));
      }

      this.loadingDetalle = true;

      // 🟢 Ejecutar llamadas
      forkJoin(calls).pipe(
          finalize(() => {
              this.loadingDetalle = false;
              this.cdr.detectChanges();
          })
      ).subscribe({
          next: (results: any) => {
              // 1. Manejo de Reserva
              const reservas = results.reserva;
              if (Array.isArray(reservas) && reservas.length > 0) {
                  const r: any = reservas[0];
                  this.reservaAsociada = { 
                      ...r,
                      estadoReserva: r.estado || r.estadoReserva || '',
                      fechaReservaInicio: r.fechaReservaInicio || r.fechaReserva || '',
                      fechaReservaFin: r.fechaReservaFin || r.fechaReserva || '',
                      fechaCreacion: r.fechaCreacion || '',
                      fechaActualizacion: r.fechaActualizacion || '',
                      idSolicitud: r.idSolicitud || solicitud.idSolicitud,
                      idProveedor: r.idProveedor || r.idProovedor || solicitud.idProovedor || '',
                      idOrganizador: r.idOrganizador || r.id_organizador || solicitud.idOrganizador || ''
                  };
              }

              // 2. Manejo de Oferta
              if (results.oferta) {
                  this.ofertaAsociada = results.oferta;
              }
          },
          error: (err: any) => {
              console.error('Error cargando detalles del modal:', err);
              // Determinar el error
              this.errorDetalle = 'Ocurrió un error al cargar los detalles.';
          }
      });
  }

  // 🟢 Método para cerrar Modal
  cerrarModal(): void {
    this.mostrarModal = false;
    this.solicitudSeleccionada = null;
    this.reservaAsociada = null;
    this.ofertaAsociada = null;
    this.errorDetalle = null;
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
    this.aplicarFiltros(); // Refresca la lista filtrada
  }

}