import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Notificacion, TIPOS_NOTIFICACION_CONFIG, PRIORIDADES_CONFIG } from '../../../models/notifications/notification.model';

@Component({
  selector: 'app-notification-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-item.component.html',
  //styleUrls: ['./notification-item.component.css']
})
export class NotificationItemComponent {
  @Input() notificacion!: Notificacion;
  @Output() marcarComoLeida = new EventEmitter<number>();
  @Output() eliminar = new EventEmitter<number>();

  get tipoConfig() {
    const tipoNombre = this.notificacion.tipoNotificacion.nombre || 'SISTEMA';
    return TIPOS_NOTIFICACION_CONFIG[tipoNombre] || TIPOS_NOTIFICACION_CONFIG['SISTEMA'];
  }

  get prioridadConfig() {
    const prioridadNombre = this.notificacion.prioridad.nombre || 'MEDIA';
    return PRIORIDADES_CONFIG[prioridadNombre] || PRIORIDADES_CONFIG['MEDIA'];
  }

  getTiempoTranscurrido(): string {
    const fecha = new Date(this.notificacion.fechaCreacion);
    const ahora = new Date();
    const diferencia = ahora.getTime() - fecha.getTime();
    const minutos = Math.floor(diferencia / (1000 * 60));
    const horas = Math.floor(minutos / 60);
    const dias = Math.floor(horas / 24);

    if (minutos < 1) {
      return 'Ahora mismo';
    } else if (minutos < 60) {
      return `Hace ${minutos} min`;
    } else if (horas < 24) {
      return `Hace ${horas} hora${horas !== 1 ? 's' : ''}`;
    } else if (dias === 1) {
      return 'Ayer';
    } else if (dias < 7) {
      return `Hace ${dias} día${dias !== 1 ? 's' : ''}`;
    } else {
      return fecha.toLocaleDateString('es-ES', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      });
    }
  }

  getBackgroundClass(): string {
    return this.notificacion.leido 
      ? 'bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700/50' 
      : 'bg-primary/5 dark:bg-primary/10 hover:bg-primary/10 dark:hover:bg-primary/20';
  }
}