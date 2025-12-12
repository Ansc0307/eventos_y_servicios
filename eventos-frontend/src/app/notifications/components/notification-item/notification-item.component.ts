import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Notificacion } from '../../../models/notifications/notification.model';

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

  getColorPorPrioridad(): string {
    const prioridad = this.notificacion.prioridad.nombre?.toLowerCase();
    switch(prioridad) {
      case 'alta': case 'urgente':
        return 'bg-red-500/10 text-red-500 ring-red-500/20';
      case 'media': case 'normal':
        return 'bg-yellow-500/10 text-yellow-500 ring-yellow-500/20';
      case 'baja':
        return 'bg-green-500/10 text-green-500 ring-green-500/20';
      default:
        return 'bg-slate-500/10 text-slate-500 ring-slate-500/20';
    }
  }

  getIconoTipo(): { icon: string; color: string } {
    const tipo = this.notificacion.tipoNotificacion.nombre?.toLowerCase();
    switch(tipo) {
      case 'alerta':
        return { icon: 'campaign', color: 'text-blue-500' };
      case 'informacion':
        return { icon: 'info', color: 'text-blue-500' };
      case 'recordatorio':
        return { icon: 'notifications', color: 'text-yellow-500' };
      case 'promocion':
        return { icon: 'local_offer', color: 'text-purple-500' };
      case 'sistema':
        return { icon: 'settings', color: 'text-slate-500' };
      default:
        return { icon: 'notifications', color: 'text-primary' };
    }
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
      return `Hace ${minutos} minuto${minutos !== 1 ? 's' : ''}`;
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
      ? 'bg-background-light dark:bg-background-dark hover:bg-black/5 dark:hover:bg-white/5' 
      : 'bg-primary/5 dark:bg-primary/10 hover:bg-primary/10 dark:hover:bg-primary/20';
  }
}