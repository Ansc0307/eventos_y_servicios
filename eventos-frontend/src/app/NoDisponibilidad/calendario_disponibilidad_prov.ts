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
  <div class="max-w-7xl mx-auto flex flex-col gap-8 px-4 sm:px-6 lg:px-8 py-8 lg:py-12">

    <!-- ENCABEZADO -->
    <div class="flex flex-wrap justify-between items-start gap-4">



    
      <div class="flex flex-col gap-2">
        <p class="text-[#0d191b] dark:text-white text-4xl font-black leading-tight tracking-[-0.033em]">Calendario Detallado del Proveedor</p>
       
      </div>
    </div>

    <!-- GRID PRINCIPAL -->
    <div class="grid grid-cols-1 xl:grid-cols-3 gap-8 items-start">

      <!-- CALENDARIO -->
      <div class="xl:col-span-2 bg-white dark:bg-[#1A2C2F] rounded-xl shadow-sm border border-[#e7f1f3] p-4 sm:p-6">
        <div class="flex flex-col">

          <!-- TITULO MES -->
<div class="flex items-center justify-between pb-4 border-b border-[#e7f1f3] mb-4">

  <!-- Flecha anterior -->
  <button
    (click)="prevMonth()"
    class="p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition"
  >
    <span class="material-symbols-outlined text-xl">chevron_left</span>
  </button>

  <!-- Nombre del mes -->
  <p class="text-[#0d191b] dark:text-white text-xl font-bold">
    {{ currentMonth | date:'MMMM yyyy' }}
  </p>

  <!-- Flecha siguiente -->
  <button
    (click)="nextMonth()"
    class="p-2 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-700 transition"
  >
    <span class="material-symbols-outlined text-xl">chevron_right</span>
  </button>

</div>


          <!-- DIAS SEMANA -->
          <div class="grid grid-cols-7 gap-1">
            <p *ngFor="let d of weekDays" class="text-[#4c8d9a] text-sm font-bold flex h-10 items-center justify-center">
              {{ d }}
            </p>
          </div>

          <!-- DIAS DEL MES -->
          <div class="grid grid-cols-7 gap-1">
            <div *ngFor="let day of calendarDays"
                 (click)="toggleDay(day)"
                 class="h-28 text-sm font-medium rounded-lg flex flex-col p-2 border border-transparent cursor-pointer"
                 [ngClass]="{
                    'bg-primary text-black': day.selected,
                    'bg-rose-500/20 text-rose-800': day.status==='no-disponible',
                    'hover:bg-primary/10': day.status==='disponible',
                    'text-[#0d191b] dark:text-white': day.status!=='no-disponible'
                 }">
              {{ day.date.getDate() }}

              <span *ngIf="day.note" class="text-xs mt-1 line-clamp-3">
                {{ day.note }}
              </span>
            </div>
          </div>

        </div>
      </div>

      <!-- PANEL LATERAL -->
      <div class="xl:col-span-1 bg-white dark:bg-[#1A2C2F] rounded-xl shadow-sm border border-[#e7f1f3] p-6 space-y-6 sticky top-24">

        <p class="text-base font-bold text-[#0d191b] dark:text-white">Agregar No Disponibilidad</p>

         

        <div class="flex flex-col gap-2">
          <label class="text-sm font-medium">Motivo de no disponibilidad</label>
          <textarea [(ngModel)]="noteText" class="h-28 w-full rounded-lg border p-3 text-sm"></textarea>
        </div>

        <div class="flex gap-2">
         <button 
  (click)="guardarSeleccion()" 
  class="flex min-w-[84px] items-center justify-center overflow-hidden rounded-lg h-10 px-5 bg-primary text-white text-sm font-bold shadow-sm hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 dark:focus:ring-offset-background-dark"
>
  Actualizar Día
</button>


          <button (click)="limpiar()" class="rounded-lg h-11 px-5 text-[#4c8d9a] hover:text-primary">
            Limpiar
          </button>
        </div>

        <!-- LISTADO DE NO DISPONIBILIDADES -->
        <div class="pt-4 border-t border-[#e7f1f3]">
          <p class="text-base font-bold text-[#0d191b] dark:text-white mb-3">
            No Disponibilidades
          </p>

          <div *ngIf="noDisponibilidades.length === 0" class="text-sm text-gray-500">
            No existen días bloqueados.
          </div>

          <div *ngFor="let nd of noDisponibilidades"
               class="flex justify-between items-center p-3 rounded-lg mb-2"
               [ngClass]="{
                 'bg-rose-500/10 text-rose-800': true
               }">

            <div class="flex flex-col">
              <p class="text-sm font-bold">No Disponible</p>
              <p class="text-xs">
                {{ nd.fechaInicio | date:'dd MMM yyyy' }} - {{ nd.fechaFin | date:'dd MMM yyyy' }}
              </p>
              <p class="text-xs italic" *ngIf="nd.motivo">{{ nd.motivo }}</p>
            </div>

            <button (click)="eliminarNoDisponibilidad(nd.idNoDisponibilidad)"
                    class="hover:text-red-500">
              <span class="material-symbols-outlined text-xl">delete</span>
            </button>

          </div>
        </div>

      </div>

    </div>
  </div>
  `,
})
export class CalendarioDetalladoComponent implements OnInit {

  noDisponibilidades: NoDisponibilidad[] = [];
  currentMonth: Date = new Date();
  calendarDays: CalendarDay[] = [];

  noteText: string = '';
  weekDays = ['DOM','LUN','MAR','MIE','JUE','VIE','SAB'];

  constructor(
    private service: NoDisponibilidadesService,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.fetchNoDisponibilidades();
    this.buildCalendar();
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

    const payload = {
      fechaInicio: new Date(start.getFullYear(), start.getMonth(), start.getDate()),
      fechaFin: new Date(end.getFullYear(), end.getMonth(), end.getDate()),
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