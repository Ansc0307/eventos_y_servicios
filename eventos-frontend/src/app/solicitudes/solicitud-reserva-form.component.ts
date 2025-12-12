import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SolicitudesService } from '../services/solicitudes.service';
import { ReservasService } from '../services/reservas.service';
import { NoDisponibilidadesService } from '../services/no-disponibilidades.service';
import { Solicitud } from '../models/solicitud.model';
import { Reserva } from '../models/reserva.model';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-solicitud-reserva-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&amp;display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&amp;display=swap" rel="stylesheet"/>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <script>
      tailwind.config = {
        darkMode: "class",
        theme: {
          extend: {
            colors: {
              "primary": "#13c8ec",
              "background-light": "#f6f8f8",
              "background-dark": "#101f22",
            },
            fontFamily: {
              "display": ["Manrope", "sans-serif"]
            },
            borderRadius: {"DEFAULT": "0.25rem", "lg": "0.5rem", "xl": "0.75rem", "full": "9999px"},
          },
        },
      }
    </script>
    <style>
      .material-symbols-outlined {
        font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
      }
    </style>
    <div class="relative flex min-h-screen w-full flex-col items-center justify-center p-4 sm:p-6 md:p-8 font-display bg-background-light dark:bg-background-dark">
      <!-- Form View -->
      <div class="w-full max-w-4xl rounded-xl bg-white dark:bg-background-dark dark:border dark:border-white/10 shadow-lg transition-all" *ngIf="!showConfirmation">
        <div class="p-6 sm:p-8">
          <!-- Close Button -->
          <button class="absolute top-4 right-4 text-gray-500 hover:text-gray-800 dark:text-gray-400 dark:hover:text-white">
            <span class="material-symbols-outlined">close</span>
          </button>
          <!-- Page Heading -->
          <div class="flex min-w-72 flex-col gap-1 mb-6">
            <h1 class="text-[#0d191b] dark:text-white tracking-light text-2xl sm:text-3xl font-bold leading-tight">Solicitud de Reserva para Salón de Eventos 'El Mirador'</h1>
            <p class="text-gray-500 dark:text-gray-400 text-sm font-normal leading-normal">Completa los siguientes campos para enviar tu solicitud al proveedor. Si solo es evento de un dia haz double click</p>
          </div>
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <!-- Left Column: Date Range -->
            <div class="flex flex-col gap-4">
              <!-- Title: Select Date Range -->
              <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-2">Selecciona el período del evento</h2>
              <!-- Calendar Picker -->
              <div class="flex min-w-72 flex-1 flex-col gap-0.5">
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
            <!-- Right Column: Time & Attendees -->
            <div class="flex flex-col gap-4">
              <!-- Title: Select Time -->
              <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-2">Elige el horario</h2>
              <!-- Time TextFields -->
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
              <!-- Title: Attendees -->
              <h2 class="text-[#0d191b] dark:text-white text-lg font-bold leading-tight tracking-[-0.015em] pt-4">Número de asistentes</h2>
              <!-- Number Input -->
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
          <!-- Submit Button -->
          <div class="border-t border-gray-200 dark:border-gray-700 mt-8 pt-6 flex justify-end">
            <button class="flex items-center justify-center rounded-lg bg-primary px-6 py-3 text-base font-bold text-[#0d191b] transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:ring-offset-2 dark:focus:ring-offset-background-dark" (click)="submit()" [disabled]="loading">
              Enviar Solicitud
            </button>
          </div>
          <div *ngIf="error" class="text-red-500 mt-4">{{ error }}</div>
        </div>
      </div>
      <!-- Confirmation View -->
      <div class="w-full max-w-lg rounded-xl bg-white dark:bg-background-dark dark:border dark:border-white/10 shadow-lg transition-all mt-10" *ngIf="showConfirmation">
        <div class="flex flex-col items-center justify-center gap-6 p-8 sm:p-12 text-center">
          <div class="flex h-20 w-20 items-center justify-center rounded-full bg-green-100 dark:bg-green-900/50">
            <span class="material-symbols-outlined text-5xl text-green-500 dark:text-green-400">check_circle</span>
          </div>
          <div class="flex flex-col gap-2">
            <h2 class="text-2xl font-bold text-[#0d191b] dark:text-white">¡Solicitud Enviada!</h2>
            <p class="text-gray-600 dark:text-gray-400">Tu solicitud ha sido enviada al proveedor. Recibirás una notificación por correo electrónico cuando respondan.</p>
          </div>
          <button class="w-full flex items-center justify-center rounded-lg bg-primary px-6 py-3 text-base font-bold text-[#0d191b] transition-all hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary/50 focus:ring-offset-2 dark:focus:ring-offset-background-dark" (click)="closeConfirmation()">
            Entendido
          </button>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class SolicitudReservaFormComponent implements OnInit {
  currentMonth: Date = new Date();
  fechaInicio: string = '';
  fechaFin: string = '';
  startTime: string = '09:00';
  endTime: string = '17:00';
  attendees: number = 50;
  noDisponibilidades: NoDisponibilidad[] = [];
  // 🆕 Añadimos lista de reservas para el calendario
  reservas: Reserva[] = []; 
  disabledDates: Set<string> = new Set();
  showConfirmation: boolean = false;
  loading: boolean = false;
  error: string = '';

  constructor(
    private solicitudesService: SolicitudesService,
    private reservasService: ReservasService,
    private noDispService: NoDisponibilidadesService
  ) {}

 ngOnInit(): void {
    this.loadRestrictions();
  }




// ... (dentro de SolicitudReservaFormComponent)

  loadRestrictions() {
    this.loading = true;
    
    forkJoin({
      noDisponibilidades: this.noDispService.getAll(),
      reservas: this.reservasService.getTodasLasReservas()
    }).subscribe({
      next: (results) => {
        this.noDisponibilidades = results.noDisponibilidades.filter(nd => nd.idOferta === 1);
        this.reservas = results.reservas;
        
        // 👈 ESTO ES CRUCIAL: Se llama al finalizar la carga
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



  /**
   * Combina NoDisponibilidades y Reservas para marcar fechas como deshabilitadas.
   */
  calculateDisabledDates() {
    this.disabledDates.clear();
    
    // Función auxiliar para agregar días de un rango al Set
    const addDatesInRange = (startDateStr: string, endDateStr: string) => {
      const start = new Date(startDateStr);
      const end = new Date(endDateStr);
      
      // Iterar día por día
      for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
        // Usar toISOString().split('T')[0] para obtener solo la fecha YYYY-MM-DD
        this.disabledDates.add(d.toISOString().split('T')[0]);
      }
    };

    // 1. Fechas deshabilitadas por NoDisponibilidad (cierre, mantenimiento, etc.)
    this.noDisponibilidades.forEach(nd => {
      addDatesInRange(nd.fechaInicio, nd.fechaFin);
    });
    
    // 2. Fechas deshabilitadas por Reservas existentes (CONFIRMADA, PENDIENTE, etc.)
    this.reservas.forEach(reserva => {
      // Podrías querer filtrar por estado si solo CONFIRMADA bloquea. 
      // Para mayor precaución, bloqueamos todas las que no estén RECHAZADAS.
      if (reserva.estado !== 'RECHAZADA') {
        addDatesInRange(reserva.fechaReservaInicio, reserva.fechaReservaFin);
      }
    });
  }


  loadNoDisponibilidades() {
    this.noDispService.getAll().subscribe({
      next: (data) => {
        // Filter by idOferta=1 (since static)
        this.noDisponibilidades = data.filter(nd => nd.idOferta === 1);
        this.calculateDisabledDates();
      },
      error: (err) => {
        console.error('Error loading no disponibilidades', err);
      }
    });
  }


// En SolicitudReservaFormComponent

  getDaysInMonth() {
    const year = this.currentMonth.getFullYear();
    const month = this.currentMonth.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const days = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Para comparar solo la fecha

    // Add previous month days to fill the grid
    const startDay = firstDay.getDay();
    for (let i = startDay - 1; i >= 0; i--) {
      const date = new Date(year, month, -i);
      days.push({ date, isCurrentMonth: false, isDisabled: true });
    }
    
    // Add current month days
    for (let i = 1; i <= lastDay.getDate(); i++) {
      const date = new Date(year, month, i);
      const dateStr = date.toISOString().split('T')[0];
      
      // ⚠️ Una fecha está deshabilitada si:
      // 1. Está marcada en disabledDates (por Reserva o NoDisp).
      // 2. Es una fecha pasada.
      const isPast = date < today;
      const isDisabled = isPast || this.disabledDates.has(dateStr); // Combina ambas restricciones
      
      days.push({ date, isCurrentMonth: true, isDisabled });
    }
    // Add next month days to fill the grid
    const totalDays = days.length;
    const remaining = 42 - totalDays;
    for (let i = 1; i <= remaining; i++) {
      const date = new Date(year, month + 1, i);
      days.push({ date, isCurrentMonth: false, isDisabled: true });
    }
    return days;
  }

  // ... (dentro de SolicitudReservaFormComponent)

  selectDate(date: Date) {
    const dateStr = date.toISOString().split('T')[0];
    if (this.disabledDates.has(dateStr)) return;

    if (!this.fechaInicio) {
      // Caso 1: Primera selección
      this.fechaInicio = dateStr;
      this.fechaFin = ''; // Asegurar que Fin esté vacío
    } else if (!this.fechaFin) {
      // Caso 2: Segunda selección
      
      if (dateStr === this.fechaInicio) {
        // 🆕 El usuario hizo clic en la misma fecha: SELECCIÓN DE UN SOLO DÍA
        this.fechaFin = dateStr;
      } else if (dateStr < this.fechaInicio) {
        // Selección en orden inverso
        this.fechaFin = this.fechaInicio;
        this.fechaInicio = dateStr;
      } else {
        // Selección normal
        this.fechaFin = dateStr;
      }
    } else {
      // Caso 3: Ya hay un rango seleccionado (tres clics, reiniciar)
      this.fechaInicio = dateStr;
      this.fechaFin = '';
    }
  }



isSelected(date: Date): boolean {
    const dateStr = date.toISOString().split('T')[0];
    // Retorna true si es la fecha de inicio, la fecha de fin, o si ambas son iguales (un solo día)
    return dateStr === this.fechaInicio || dateStr === this.fechaFin;
  }

  isInRange(date: Date): boolean {
    if (!this.fechaInicio || !this.fechaFin) return false;
    const dateStr = date.toISOString().split('T')[0];
    
    // 🆕 Excluye el caso de un solo día para evitar que se pinte de azul claro el "rango" (que es solo ese día)
    if (this.fechaInicio === this.fechaFin) return false;
    
    // Retorna si la fecha está entre el inicio y el fin (excluyendo los días de inicio y fin, que ya cubre isSelected)
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

 // ... (código existente)

  submit() {
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

    // -------------------------------------------------------------------
    // 1. Validación de No Disponibilidades (ya existente)
    // -------------------------------------------------------------------
    const overlapsND = this.noDisponibilidades.some(nd => {
      const ndStart = new Date(nd.fechaInicio);
      const ndEnd = new Date(nd.fechaFin);
      return startDateTime < ndEnd && endDateTime > ndStart;
    });

    if (overlapsND) {
      this.error = 'El período seleccionado ya está marcado como NO DISPONIBLE (cierre, mantenimiento, etc.).';
      return;
    }

    this.loading = true;
    this.error = '';

    // Formato para el backend (ISO string, que el backend puede parsear a LocalDateTime)
    // El backend de Java espera 'yyyy-MM-ddTHH:mm:ss', vamos a asegurarnos de que el formato sea limpio.
    const formatLocalDateTime = (date: Date) => {
      const d = new Date(date);
      const pad = (n: number) => n.toString().padStart(2, '0');
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    };

    const inicioFormatted = formatLocalDateTime(startDateTime);
    const finFormatted = formatLocalDateTime(endDateTime);

    // -------------------------------------------------------------------
    // 2. 🆕 NUEVA: Validación de Reservas Conflictivas (con el backend)
    // -------------------------------------------------------------------
    this.reservasService.getReservasConflictivas(inicioFormatted, finFormatted).subscribe({
      next: (reservasConflictivas: Reserva[]) => {
        
        if (reservasConflictivas.length > 0) {
          this.error = `Ya existe(n) ${reservasConflictivas.length} reserva(s) que se solapa(n) con el período solicitado.`;
          this.loading = false;
          return;
        }

        // Si no hay conflictos, proceder a crear la solicitud y la reserva
        this.createSolicitudAndReserva(inicioFormatted, finFormatted);
      },
      error: (err) => {
        console.error('Error al verificar reservas conflictivas:', err);
        this.loading = false;
        this.error = 'Error de conexión al verificar disponibilidad. Intenta nuevamente.';
      }
    });
  }

  // -------------------------------------------------------------------
  // 🆕 NUEVO: Método helper para encapsular la creación
  // -------------------------------------------------------------------
  private createSolicitudAndReserva(inicioFormatted: string, finFormatted: string) {
    // Create solicitud
    const solicitudPayload = {
      fechaSolicitud: new Date().toISOString(),
      idOrganizador: 14, // Usar IDs estáticos/mockeados
      idProovedor: 1, // Usar IDs estáticos/mockeados
      idOferta: 1, // Usar IDs estáticos/mockeados (asumimos que la restricción es por oferta)
      estadoSolicitud: 'PENDIENTE'
    };

    this.solicitudesService.create(solicitudPayload).subscribe({
      next: (solicitud: Solicitud) => {
        // Create reserva (en estado PENDIENTE)
        const reservaPayload = {
          idSolicitud: solicitud.idSolicitud,
          fechaReservaInicio: inicioFormatted, // Usamos las fechas ya formateadas
          fechaReservaFin: finFormatted,      // Usamos las fechas ya formateadas
          estado: 'PENDIENTE'
        };

        this.reservasService.create(reservaPayload).subscribe({
          next: (reserva: Reserva) => {
            this.loading = false;
            this.showConfirmation = true;
          },
          error: (err) => {
            this.loading = false;
            this.error = 'Error creando reserva: ' + (err?.error?.message || err?.message || 'Error desconocido');
            
            // ⚠️ Consideración: Si falla la creación de la reserva,
            // la Solicitud ya fue creada. Deberías considerar eliminar
            // la Solicitud recién creada para no dejarla "huérfana".
          }
        });
      },
      error: (err) => {
        this.loading = false;
        this.error = 'Error creando solicitud: ' + (err?.error?.message || err?.message || 'Error desconocido');
      }
    });
  }

  closeConfirmation() {
    this.showConfirmation = false;
    this.fechaInicio = '';
    this.fechaFin = '';
    this.startTime = '09:00';
    this.endTime = '17:00';
    this.attendees = 50;
  }
}