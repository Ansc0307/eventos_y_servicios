import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';
import { RefreshService } from '../services/refresh.service';

interface CalendarDay {
  date: Date;
  selected: boolean;
  status: 'disponible' | 'no-disponible' | 'evento' | null;
  note?: string;
}

@Component({
  selector: 'app-calendario-detallado',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template:  `
  <div class="font-display bg-background-light dark:bg-background-dark text-[#18181B] dark:text-gray-200 min-h-screen">
  <div class="relative flex min-h-screen w-full">

    <!-- ASIDE REUTILIZADO -->
    <aside class="flex h-screen w-64 flex-col border-r border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 sticky top-0">
      <div class="flex h-full flex-col justify-between p-4">
        <div class="flex flex-col gap-4">
          <div class="flex items-center gap-4 text-slate-900 dark:text-white px-3 py-2">
            <span class="material-symbols-outlined text-3xl text-primary">hub</span>
            <h2 class="text-lg font-bold tracking-[-0.015em]">EvenPro</h2>
          </div>

          <!-- NAV -->
          <nav class="flex flex-col gap-2 mt-4">
            <a (click)="volverDashboard()" class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-primary/10 cursor-pointer text-slate-600 dark:text-slate-300">
                <span class="material-symbols-outlined">dashboard</span>
                <p class="text-sm font-medium">Dashboard</p>
              </a>

            <!-- Opción activa -->
            <div class="flex items-center gap-3 px-3 py-2 rounded-lg bg-primary/20">
              <span class="material-symbols-outlined text-slate-900 dark:text-white">calendar_month</span>
              <p class="text-slate-900 dark:text-white text-sm font-medium">Calendario</p>
            </div>
          </nav>
        </div>

        <!-- PERFIL -->
        <div class="flex flex-col gap-4">
          <div class="flex gap-3 items-center">
            <div class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10"
              style="background-image: url('https://lh3.googleusercontent.com/aida-public/AB6AXuAYQ3Xv_YuY339LzWOL4jYfKwpp_Xk9EQPeGlaPTZUaCbWibigjj_YB-aGxvhwg8F6DZMvP78IzouOQH3-QD04rKwZu0qAV4ksMNwLhpVskYFEt4FmVucm_mFLxLxPTX8hDHUjR_Z9oMgFc_G87oiDiH7JpnVMSiQivqyiCyL3FHFneBsNk31-5d9q8uvRmqI_l6FgX35MdysNRvagVfmucr0CWN1v_HLjU_aiWNcTSeh51R5rwoZnxazDlwLlmCDHhNO9UufJdhm1M');">
            </div>

            <div class="flex flex-col">
              <h1 class="text-slate-900 dark:text-white text-base font-medium">{{ userName }}</h1>
              <p class="text-primary/80 dark:text-primary/70 text-sm">Proveedor Verificado</p>
            </div>
          </div>
        </div>
      </div>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="flex-1 p-10">

      <!-- HEADER -->
      <header class="flex items-center justify-between mb-10">
        <div>
          <h1 class="text-3xl font-black text-slate-900 dark:text-white">Calendario del Proveedor</h1>
          <p class="text-sm text-slate-600 dark:text-slate-400 mt-1">Gestiona tus días disponibles</p>
        </div>
      </header>

      <!-- EL CALENDARIO ORIGINAL AQUÍ 🔽 -->
      <div class="max-w-7xl mx-auto flex flex-col gap-8">
        
        <!-- CALENDARIO COMPLETO (tu código original) -->
        <!-- LO PEGO COMPLETO AQUÍ SIN MODIFICAR NADA EXCEPTO EL LAYOUT -->

        <div class="grid grid-cols-1 xl:grid-cols-3 gap-8 items-start">

          <!-- CALENDARIO -->
          <div class="xl:col-span-2 bg-white dark:bg-[#1A2C2F] rounded-xl shadow-sm border border-[#e7f1f3] p-6">
            
            <div class="flex items-center justify-between pb-4 border-b border-[#e7f1f3] mb-4">

              <button (click)="prevMonth()" class="p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700">
                <span class="material-symbols-outlined text-xl">chevron_left</span>
              </button>

              <p class="text-[#0d191b] dark:text-white text-xl font-bold">
                {{ currentMonth | date:'MMMM yyyy' }}
              </p>

              <button (click)="nextMonth()" class="p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700">
                <span class="material-symbols-outlined text-xl">chevron_right</span>
              </button>
            </div>

            <!-- DIAS SEMANA -->
            <div class="grid grid-cols-7 gap-1">
              <p *ngFor="let d of weekDays"
                 class="text-[#4c8d9a] text-sm font-bold flex h-10 items-center justify-center">
                {{ d }}
              </p>
            </div>

            <!-- DIAS MES -->
            <div class="grid grid-cols-7 gap-1">
              <div *ngFor="let day of calendarDays"
                   (click)="toggleDay(day)"
                   class="h-28 text-sm font-medium rounded-lg p-2 cursor-pointer"
                   [ngClass]="{
                     'bg-primary text-black': day.selected,
                     'bg-rose-500/20 text-rose-800': day.status==='no-disponible',
                     'hover:bg-primary/10': day.status==='disponible',
                     'text-[#0d191b] dark:text-white': day.status!=='no-disponible'
                   }">
                {{ day.date.getDate() }}

                <span *ngIf="day.note" class="text-xs mt-1 block">{{ day.note }}</span>
              </div>
            </div>

          </div>

          <!-- PANEL LATERAL -->
          <div class="xl:col-span-1 bg-white dark:bg-[#1A2C2F] rounded-xl shadow-sm border border-[#e7f1f3] p-6 space-y-6 sticky top-24">

            <p class="text-base font-bold">Agregar No Disponibilidad</p>

            <div class="flex flex-col gap-2">
              <label class="text-sm font-medium">Motivo</label>
              <textarea [(ngModel)]="noteText"
                        class="h-28 w-full rounded-lg border p-3 text-sm"></textarea>
            </div>

            <div class="flex gap-2">
              <button (click)="guardarSeleccion()"
                class="h-10 px-5 rounded-lg bg-primary text-white font-bold">
                Actualizar Día
              </button>

              <button (click)="limpiar()" class="h-10 px-5 rounded-lg text-primary font-medium">
                Limpiar
              </button>
            </div>

            <div class="pt-4 border-t border-[#e7f1f3]">
              <p class="font-bold mb-3">No Disponibilidades</p>

              <div *ngIf="noDisponibilidades.length === 0"
                   class="text-sm text-gray-500">No existen días bloqueados.</div>

              <div *ngFor="let nd of noDisponibilidades"
                   class="p-3 rounded-lg mb-2 bg-rose-500/10 text-rose-800 flex justify-between">

                <div class="flex flex-col">
                  <p class="font-bold text-sm">No Disponible</p>
                  <p class="text-xs">
                    {{ nd.fechaInicio | date:'dd MMM yyyy' }} -
                    {{ nd.fechaFin | date:'dd MMM yyyy' }}
                  </p>
                  <p class="text-xs italic" *ngIf="nd.motivo">{{ nd.motivo }}</p>
                </div>

               <button *ngIf="!nd.idReserva"
        (click)="eliminarNoDisponibilidad(nd.idNoDisponibilidad)">
  <span class="material-symbols-outlined text-xl">delete</span>
</button>

<!-- Si tiene reserva → mostramos un ícono bloqueado -->
<span *ngIf="nd.idReserva"
      class="material-symbols-outlined text-xl text-gray-400 cursor-not-allowed">
  lock
</span>

              </div>
            </div>

          </div>

        </div>

      </div>
    </main>

  </div>
</div>

  `,
})
export class CalendarioDetalladoComponent implements OnInit {
  userName: string = '';
idProveedor = 1;
  //userName: string = 'Proveedor';


  noDisponibilidades: NoDisponibilidad[] = [];
  currentMonth: Date = new Date();
  calendarDays: CalendarDay[] = [];

  noteText: string = '';
  weekDays = ['DOM','LUN','MAR','MIE','JUE','VIE','SAB'];


constructor(
    private router: Router,
    private keycloak: KeycloakService,
    private service: NoDisponibilidadesService,
    private cd: ChangeDetectorRef
  ) {}
ngOnInit() {
  try {
    const tokenParsed = this.keycloak.getKeycloakInstance().tokenParsed;
    this.userName = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'Proveedor';

    // ID fijo o dinámico según tu caso
    this.idProveedor = 1; 

    // Carga inicial
    this.fetchNoDisponibilidades();
    this.buildCalendar();
  } catch (err) {
    console.error('Error al inicializar calendario:', err);
    this.userName = 'Proveedor';
    this.noDisponibilidades = [];
    this.calendarDays = [];
    this.cd.detectChanges();
  }
}


volverDashboard() {
  // Más seguro que navigate() con rutas absolutas
  this.router.navigateByUrl('/dashboard/proveedor');
}

  /** ============================================
   *      CARGAR LISTA Y ORDENAR POR CREACIÓN
   *  ============================================ */
  fetchNoDisponibilidades() {
    this.service.getAll().subscribe({
      next: (data) => {
        this.noDisponibilidades = (data || []).sort((a, b) => a.idNoDisponibilidad - b.idNoDisponibilidad);
        this.buildCalendar();
      }
    });
  }

  /** ============================================
   *                ARMAR CALENDARIO
   *  ============================================ */
  buildCalendar() {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    this.calendarDays = [];

    const normalize = (d: Date) =>
      new Date(d.getFullYear(), d.getMonth(), d.getDate());

    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day);

      const match = this.noDisponibilidades.find(nd => {
        const start = normalize(new Date(nd.fechaInicio));
        const end = normalize(new Date(nd.fechaFin));
        const current = normalize(date);
        return current >= start && current <= end;
      });

      this.calendarDays.push({
        date,
        selected: false,
        status: match ? 'no-disponible' : 'disponible',
        note: match?.motivo || ''
      });
    }

    this.cd.detectChanges();
  }

  /** Seleccionar un día */
  toggleDay(day: CalendarDay) {
    if (day.status === 'no-disponible') return;
    day.selected = !day.selected;
  }

  /** ============================================
   *            GUARDAR → UN SOLO RANGO
   *  ============================================ */
  guardarSeleccion() {
    const selected = this.calendarDays.filter(d => d.selected);

    if (!selected.length)
      return alert('Seleccione al menos un día');

    // Ordenar por fecha seleccionada
    const sorted = selected.sort((a, b) => a.date.getTime() - b.date.getTime());

    const start = sorted[0].date;
    const end = sorted[sorted.length - 1].date;

    const fechaInicio = new Date(start.getFullYear(), start.getMonth(), start.getDate()).toISOString();
    const fechaFin = new Date(end.getFullYear(), end.getMonth(), end.getDate()).toISOString();

    const payload = {
      fechaInicio,
      fechaFin,
      motivo: this.noteText,
      idOferta: 1
    };

    this.service.create(payload).subscribe({
      next: () => {
        this.noteText = '';
        this.calendarDays.forEach(d => d.selected = false);
        this.fetchNoDisponibilidades(); // Refresca sin recargar
      }
    });
  }

  /** ============================================
   *            ELIMINAR SIN RECARGAR
   *  ============================================ */
  eliminarNoDisponibilidad(id: number) {
    this.service.delete(id).subscribe({
      next: () => {
        this.noDisponibilidades = this.noDisponibilidades.filter(nd => nd.idNoDisponibilidad !== id);
        this.buildCalendar(); // Refresca directamente
      }
    });
  }

  /** Cambiar mes */
  prevMonth() {
  this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, 1);
  this.buildCalendar();
}

nextMonth() {
  this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
  this.buildCalendar();
}


  limpiar() {
    this.calendarDays.forEach(d => (d.selected = false));
    this.noteText = '';
  }
  
}