import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// 🟢 Importar Router para la redirección
import { Router } from '@angular/router'; 
import { SolicitudesService } from '../services/solicitudes.service';
import { ReservasService } from '../services/reservas.service';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { Solicitud } from '../models/solicitud.model';
import { Reserva } from '../models/reserva.model';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';
import { forkJoin } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-solicitud-reserva-form',
  standalone: true,
  imports: [CommonModule, FormsModule],

  template: `
    <div class="w-full bg-white dark:bg-background-dark/50 p-6 rounded-xl shadow-lg border border-gray-200 dark:border-gray-800">
        <div *ngIf="!showConfirmation">
            
            <div class="flex flex-col gap-1 mb-6">
                <h1 class="text-[#0d191b] dark:text-white tracking-light text-2xl font-bold leading-tight">Completa tu Reserva</h1>
                <p class="text-gray-500 dark:text-gray-400 text-sm font-normal leading-normal">Selecciona las fechas y la cantidad de asistentes.</p>
            </div>
            
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                
                <div class="flex flex-col gap-4">
                    <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-2">Selecciona el período del evento</h2>
                    <div class="flex flex-col gap-0.5">
                        <div class="flex items-center p-1 justify-between">
                            <button class="text-[#0d191b] dark:text-white hover:bg-gray-100 dark:hover:bg-white/10 rounded-full" (click)="prevMonth()">
                                <div class="flex size-10 items-center justify-center">
                                    <span class="material-symbols-outlined text-lg">chevron_left</span>
                                </div>
                            </button>
                            <p class="text-[#0d191b] dark:text-white text-base font-bold leading-tight flex-1 text-center">{{ currentMonth | date:'MMMM yyyy' }}</p>
                            <button class="text-[#0d191b] dark:text-white hover:bg-gray-100 dark:hover:bg-white/10 rounded-full" (click)="nextMonth()">
                                <div class="flex size-10 items-center justify-center">
                                    <span class="material-symbols-outlined text-lg">chevron_right</span>
                                </div>
                            </button>
                        </div>
                        <div class="grid grid-cols-7">
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">D</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">L</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">M</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">X</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">J</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">V</p>
                            <p class="text-gray-500 dark:text-gray-400 text-[13px] font-bold leading-normal tracking-[0.015em] flex h-10 w-full items-center justify-center pb-0.5">S</p>
                            <button *ngFor="let day of getDaysInMonth()" class="h-10 w-full text-[#0d191b] dark:text-white text-sm font-medium leading-normal hover:bg-gray-100 dark:hover:bg-white/10" [class.text-gray-400]="!day.isCurrentMonth" [class.dark:text-gray-600]="!day.isCurrentMonth" [class.text-red-500]="day.isDisabled" [class.line-through]="day.isDisabled" [disabled]="day.isDisabled || !day.isCurrentMonth" (click)="selectDate(day.date)">
                                <div class="flex size-full items-center justify-center rounded-full" [class.bg-primary]="isSelected(day.date)" [class.text-white]="isSelected(day.date)" [class.bg-blue-200]="isInRange(day.date) && !isSelected(day.date)">{{ day.date.getDate() }}</div>
                            </button>
                        </div>
                    </div>
                </div>
                
                <div class="flex flex-col gap-4">
                    <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-2">Elige el horario</h2>
                    <div class="flex flex-col sm:flex-row items-end gap-4">
                        <label class="flex flex-col min-w-40 flex-1">
                            <p class="text-[#0d191b] dark:text-gray-300 text-base font-medium leading-normal pb-2">Hora de inicio</p>
                            <input class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-[#0d191b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary/50 border border-[#cfe3e7] dark:border-gray-600 bg-white dark:bg-gray-800 focus:border-primary h-14 p-[15px] text-base font-normal leading-normal" type="time" [(ngModel)]="startTime" />
                        </label>
                        <label class="flex flex-col min-w-40 flex-1">
                            <p class="text-[#0d191b] dark:text-gray-300 text-base font-medium leading-normal pb-2">Hora de fin</p>
                            <input class="form-input flex w-full min-w-0 flex-1 resize-none overflow-hidden rounded-lg text-[#0d191b] dark:text-white focus:outline-0 focus:ring-2 focus:ring-primary/50 border border-[#cfe3e7] dark:border-gray-600 bg-white dark:bg-gray-800 focus:border-primary h-14 p-[15px] text-base font-normal leading-normal" type="time" [(ngModel)]="endTime" />
                        </label>
                    </div>
                    <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-4">Número de asistentes</h2>
                    <div class="flex flex-col">
                        <div class="relative flex items-center max-w-[200px]">
                            <button class="flex-shrink-0 bg-gray-100 dark:bg-gray-700 dark:hover:bg-gray-600 hover:bg-gray-200 inline-flex items-center justify-center border border-gray-300 dark:border-gray-600 rounded-l-lg h-14 w-14 focus:ring-gray-100 dark:focus:ring-gray-700 focus:ring-2 focus:outline-none" type="button" (click)="decrementAttendees()">
                                <span class="material-symbols-outlined text-xl text-[#0d191b] dark:text-white">remove</span>
                            </button>
                            <input class="flex-shrink-0 text-gray-900 dark:text-white border-x-0 border-gray-300 dark:border-gray-600 h-14 text-center text-base font-medium w-full bg-white dark:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-primary/50" type="number" [(ngModel)]="attendees" min="1" />
                            <button class="flex-shrink-0 bg-gray-100 dark:bg-gray-700 dark:hover:bg-gray-600 hover:bg-gray-200 inline-flex items-center justify-center border border-gray-300 dark:border-gray-600 rounded-r-lg h-14 w-14 focus:ring-gray-100 dark:focus:ring-gray-700 focus:ring-2 focus:outline-none" type="button" (click)="incrementAttendees()">
                                <span class="material-symbols-outlined text-xl text-[#0d191b] dark:text-white">add</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="border-t border-gray-200 dark:border-gray-700 mt-8 pt-6 flex justify-end">
                <button class="flex items-center justify-center rounded-lg bg-primary px-6 py-3 text-base font-bold text-[#0d191b] transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:ring-offset-2 dark:focus:ring-offset-background-dark" (click)="submit()" [disabled]="loading">
                    Enviar Solicitud
                </button>
            </div>
            <div *ngIf="error" class="text-red-500 mt-4">{{ error }}</div>
        </div>
        
        <div class="flex flex-col items-center justify-center text-center p-8" *ngIf="showConfirmation">
            <div class="flex h-20 w-20 items-center justify-center rounded-full bg-green-100 dark:bg-green-900/50 mb-4">
                <span class="material-symbols-outlined text-5xl text-green-500 dark:text-green-400">check_circle</span>
            </div>
            <div class="flex flex-col gap-2 mb-6">
                <h2 class="text-2xl font-bold text-[#0d191b] dark:text-white">¡Solicitud Enviada!</h2>
                <p class="text-gray-600 dark:text-gray-400">Tu solicitud ha sido enviada al proveedor.</p>
            </div>
            <button class="w-full flex items-center justify-center rounded-lg bg-primary px-6 py-3 text-base font-bold text-[#0d191b] transition-all hover:bg-primary/90" (click)="closeConfirmation()">
                Entendido
            </button>
        </div>
    </div>
  `,
  styles: []
})
export class SolicitudReservaFormComponent implements OnInit {
  @Input() idOferta: number | undefined; 
  
  currentMonth: Date = new Date();
  fechaInicio: string = '';
  fechaFin: string = '';
  startTime: string = '09:00';
  endTime: string = '17:00';
  attendees: number = 50;
  noDisponibilidades: NoDisponibilidad[] = [];
  reservas: Reserva[] = []; 
  disabledDates: Set<string> = new Set();
  showConfirmation: boolean = false;
  loading: boolean = false;
  error: string = '';

  constructor(
  private router: Router,
  private solicitudesService: SolicitudesService,
  private reservasService: ReservasService,
  private noDispService: NoDisponibilidadesService,
  private cdr: ChangeDetectorRef
) {}


ngOnInit(): void {
    if (this.idOferta) { 
      this.loadRestrictions();
    } else {
      console.error('El ID de la oferta no ha sido proporcionado al formulario de solicitud.');
      this.error = 'No se pudo cargar la información de la oferta.';
    }
}

  loadRestrictions() {
    this.loading = true;
    
    if (!this.idOferta) {
        this.loading = false;
        return;
    }

    forkJoin({
      noDisponibilidades: this.noDispService.getAll(),
      reservas: this.reservasService.getTodasLasReservas()
    }).subscribe({
      next: (results) => {
        this.noDisponibilidades = results.noDisponibilidades.filter(nd => nd.idOferta === this.idOferta);
        this.reservas = results.reservas;
        
        this.calculateDisabledDates(); 
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading restrictions', err);
        this.error = 'Error al cargar las restricciones de disponibilidad.';
        this.loading = false;
      }
    });
  }

  calculateDisabledDates() {
    this.disabledDates.clear();
    
    const addDatesInRange = (startDateStr: string, endDateStr: string) => {
      const start = new Date(startDateStr);
      const end = new Date(endDateStr);
      
      for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
        this.disabledDates.add(d.toISOString().split('T')[0]);
      }
    };

    this.noDisponibilidades.forEach(nd => {
      addDatesInRange(nd.fechaInicio, nd.fechaFin);
    });
    
    this.reservas.forEach(reserva => {
      if (reserva.estado !== 'RECHAZADA') {
        addDatesInRange(reserva.fechaReservaInicio, reserva.fechaReservaFin);
      }
    });
  }

  getDaysInMonth() {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const days = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0); 

    const startDay = firstDay.getDay();
    for (let i = startDay - 1; i >= 0; i--) {
      const date = new Date(year, month, -i);
      days.push({ date, isCurrentMonth: false, isDisabled: true });
    }
    
    for (let i = 1; i <= lastDay.getDate(); i++) {
      const date = new Date(year, month, i);
      const dateStr = date.toISOString().split('T')[0];
      
      const isPast = date < today;
      const isDisabled = isPast || this.disabledDates.has(dateStr); 
      
      days.push({ date, isCurrentMonth: true, isDisabled });
    }
    
    const totalDays = days.length;
    const remaining = 42 - totalDays;
    for (let i = 1; i <= remaining; i++) {
      const date = new Date(year, month + 1, i);
      days.push({ date, isCurrentMonth: false, isDisabled: true });
    }
    return days;
  }

  selectDate(date: Date) {
    const dateStr = date.toISOString().split('T')[0];
    if (this.disabledDates.has(dateStr)) return;

    if (!this.fechaInicio) {
      this.fechaInicio = dateStr;
      this.fechaFin = ''; 
    } else if (!this.fechaFin) {
      
      if (dateStr === this.fechaInicio) {
        this.fechaFin = dateStr;
      } else if (dateStr < this.fechaInicio) {
        this.fechaFin = this.fechaInicio;
        this.fechaInicio = dateStr;
      } else {
        this.fechaFin = dateStr;
      }
    } else {
      this.fechaInicio = dateStr;
      this.fechaFin = '';
    }
  }

    // ... (rest of date handling methods)

  isSelected(date: Date): boolean {
    const dateStr = date.toISOString().split('T')[0];
    return dateStr === this.fechaInicio || dateStr === this.fechaFin;
  }

  isInRange(date: Date): boolean {
    if (!this.fechaInicio || !this.fechaFin) return false;
    const dateStr = date.toISOString().split('T')[0];
    
    if (this.fechaInicio === this.fechaFin) return false;
    
    return dateStr > this.fechaInicio && dateStr < this.fechaFin;
  }


  prevMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() - 1, 1);
  }

  nextMonth() {
    this.currentMonth = new Date(this.currentMonth.getFullYear(), this.currentMonth.getMonth() + 1, 1);
  }

  incrementAttendees() {
    this.attendees++;
  }

  decrementAttendees() {
    if (this.attendees > 1) this.attendees--;
  }

  submit() {
    // ... (Validaciones de fecha/hora) ...
    if (!this.fechaInicio || !this.fechaFin || !this.startTime || !this.endTime) {
      this.error = 'Selecciona fecha de inicio, fecha de fin, hora de inicio y fin';
      return;
    }

    const startDateTime = new Date(this.fechaInicio + 'T' + this.startTime);
    const endDateTime = new Date(this.fechaFin + 'T' + this.endTime);

    if (startDateTime >= endDateTime) {
      this.error = 'La fecha/hora de inicio debe ser menor que la fecha/hora de fin';
      return;
    }

    const overlapsND = this.noDisponibilidades.some(nd => {
      const ndStart = new Date(nd.fechaInicio);
      const ndEnd = new Date(nd.fechaFin);
      return startDateTime < ndEnd && endDateTime > ndStart;
    });

    if (overlapsND) {
      this.error = 'El período seleccionado ya está marcado como NO DISPONIBLE (cierre, mantenimiento, etc.).';
      return;
    }
    // ... (Fin de validaciones de fecha/hora/disponibilidad) ...

    this.loading = true;
    this.error = '';

    const formatLocalDateTime = (date: Date) => {
      const pad = (n: number) => n.toString().padStart(2, '0');
      return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
    };

    const inicioFormatted = formatLocalDateTime(startDateTime);
    const finFormatted = formatLocalDateTime(endDateTime);
    
    // 2. Validación de Reservas Conflictivas
    this.reservasService.getReservasConflictivas(inicioFormatted, finFormatted).subscribe({
      next: (reservasConflictivas: Reserva[]) => {
        
        if (reservasConflictivas.length > 0) {
          this.error = `Ya existe(n) ${reservasConflictivas.length} reserva(s) que se solapa(n) con el período solicitado.`;
          this.loading = false;
          return;
        }

        this.createSolicitudAndReserva(inicioFormatted, finFormatted);
      },
      error: (err) => {
        console.error('Error al verificar reservas conflictivas:', err);
        this.loading = false;
        this.error = 'Error de conexión al verificar disponibilidad. Intenta nuevamente.';
      }
    });
  }

  private createSolicitudAndReserva(inicioFormatted: string, finFormatted: string) {
    
    if (!this.idOferta) {
        this.error = 'El ID de la oferta es requerido para crear la solicitud.';
        this.loading = false;
        return;
    }
    
    const solicitudPayload = {
      fechaSolicitud: new Date().toISOString(),
      idOrganizador: 14, 
      idProovedor: 1, 
      idOferta: this.idOferta, 
      estadoSolicitud: 'PENDIENTE'
    };

    this.solicitudesService.create(solicitudPayload).subscribe({
      next: (solicitud: Solicitud) => {
        const reservaPayload = {
          idSolicitud: solicitud.idSolicitud,
          fechaReservaInicio: inicioFormatted, 
          fechaReservaFin: finFormatted,      
          estado: 'PENDIENTE'
        };

        this.reservasService.create(reservaPayload).subscribe({
          next: (reserva: Reserva) => {
            this.loading = false;
            // 🟢 Mostrar la confirmación SÓLO después de que la Reserva se creó con éxito
            this.showConfirmation = true;
this.cdr.detectChanges();

          },
          error: (err) => {
            this.loading = false;
            this.error = 'Error creando reserva: ' + (err?.error?.message || err?.message || 'Error desconocido');
          }
        });
      },





      error: (err) => {
        this.loading = false;
        this.error = 'Error creando solicitud: ' + (err?.error?.message || err?.message || 'Error desconocido');
      }
    });
  }

  // 🟢 Redirige al dashboard al cerrar la confirmación
  closeConfirmation() {
    this.router.navigate(['/dashboard/organizador']);

    // Se mantiene la limpieza de estado
    this.showConfirmation = false;
    this.fechaInicio = '';
    this.fechaFin = '';
    this.startTime = '09:00';
    this.endTime = '17:00';
    this.attendees = 50;
  }
}