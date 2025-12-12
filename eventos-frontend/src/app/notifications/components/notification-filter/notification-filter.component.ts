import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { Prioridad } from '../../../models/notifications/prioridad.model';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';

@Component({
  selector: 'app-notification-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notification-filter.component.html',
  //styleUrls: ['./notification-filter.component.css']
})
export class NotificationFilterComponent implements OnInit {
  @Input() filtros: any;
  @Output() filtrosCambiados = new EventEmitter<any>();

  prioridades: Prioridad[] = [];
  tipos: TipoNotificacion[] = [];
  
  estadoOpciones = [
    { value: null, label: 'Todos' },
    { value: true, label: 'Leídas' },
    { value: false, label: 'No leídas' }
  ];

  mostrarFiltros = false;

  constructor(private notificacionesService: NotificacionesService) {}

  ngOnInit(): void {
    this.cargarFiltros();
  }

  cargarFiltros(): void {
    this.notificacionesService.getPrioridades().subscribe({
      next: (data) => {
        this.prioridades = data;
      },
      error: (err) => {
        console.error('Error al cargar prioridades:', err);
      }
    });

    this.notificacionesService.getTiposNotificacion().subscribe({
      next: (data) => {
        this.tipos = data;
      },
      error: (err) => {
        console.error('Error al cargar tipos:', err);
      }
    });
  }

  onFiltroChange(): void {
    this.filtrosCambiados.emit(this.filtros);
  }

  limpiarFiltros(): void {
    this.filtros = {
      leido: null,
      prioridad: null,
      tipo: null
    };
    this.filtrosCambiados.emit(this.filtros);
  }

  getPrioridadClass(nombre: string | undefined, activo: boolean): string {
    if (activo) {
      return 'bg-primary text-white';
    }
    
    const nombreLower = nombre?.toLowerCase();
    switch(nombreLower) {
      case 'alta': case 'urgente':
        return 'bg-red-500/10 text-red-700 dark:text-red-400 hover:bg-red-500/20';
      case 'media': case 'normal':
        return 'bg-yellow-500/10 text-yellow-700 dark:text-yellow-400 hover:bg-yellow-500/20';
      case 'baja':
        return 'bg-green-500/10 text-green-700 dark:text-green-400 hover:bg-green-500/20';
      default:
        return 'bg-slate-100 dark:bg-slate-700 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-600';
    }
  }
}