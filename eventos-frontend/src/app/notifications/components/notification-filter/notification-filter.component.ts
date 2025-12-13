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
  styleUrls: ['./notification-filter.component.css']
})
export class NotificationFilterComponent implements OnInit {
  @Input() filtros: any;
  @Output() filtrosCambiados = new EventEmitter<any>();

  prioridades: Prioridad[] = [];
  tipos: TipoNotificacion[] = [];
  
  estadoOpciones = [
    { value: null, label: 'Todos', icon: 'all_inbox' },
    { value: true, label: 'Leídas', icon: 'mark_email_read' },
    { value: false, label: 'No leídas', icon: 'mark_email_unread' }
  ];

  mostrarPanelFiltros = false;

  constructor(private notificacionesService: NotificacionesService) {}

  ngOnInit(): void {
    this.cargarFiltros();
  }

  cargarFiltros(): void {
    this.notificacionesService.getPrioridades().subscribe({
      next: (data) => {
        this.prioridades = data || [];
      },
      error: (err) => {
        console.error('Error al cargar prioridades:', err);
      }
    });

    this.notificacionesService.getTiposNotificacion().subscribe({
      next: (data) => {
        this.tipos = data || [];
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

  getPrioridadColor(nombre: string | undefined): string {
    const nombreLower = nombre?.toLowerCase();
    switch(nombreLower) {
      case 'alta': return 'border-red-500 text-red-700 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20';
      case 'media': return 'border-yellow-500 text-yellow-700 dark:text-yellow-400 hover:bg-yellow-50 dark:hover:bg-yellow-900/20';
      case 'baja': return 'border-green-500 text-green-700 dark:text-green-400 hover:bg-green-50 dark:hover:bg-green-900/20';
      default: return 'border-slate-300 text-slate-700 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700';
    }
  }

  getTipoIcon(nombre: string | undefined): string {
    switch(nombre) {
      case 'ALERTA': return 'warning';
      case 'INFORMATIVA': return 'info';
      case 'PROMOCION': return 'local_offer';
      case 'SISTEMA': return 'settings';
      case 'RECORDATORIO': return 'notifications';
      default: return 'notifications';
    }
  }
}