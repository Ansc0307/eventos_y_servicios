import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Prioridad } from '../../../models/notifications/prioridad.model';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';

@Component({
  selector: 'app-modal-notificacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-notificacion.component.html',
  //styleUrls: ['./modal-notificacion.component.css']
})
export class ModalNotificacionComponent {
  @Input() titulo: string = 'Nueva Notificación';
  @Input() prioridades: Prioridad[] = [];
  @Input() tipos: TipoNotificacion[] = [];
  @Input() loading: boolean = false;
  
  @Output() cerrar = new EventEmitter<void>();
  @Output() guardar = new EventEmitter<any>();

  formData = {
    asunto: '',
    mensaje: '',
    prioridadId: null as number | null,
    tipoId: null as number | null
  };

  getPrioridadColor(nombre: string | undefined): string {
    switch(nombre?.toLowerCase()) {
      case 'alta': return 'border-red-200 bg-red-50 text-red-700 dark:bg-red-900/20 dark:border-red-800 dark:text-red-400';
      case 'media': return 'border-yellow-200 bg-yellow-50 text-yellow-700 dark:bg-yellow-900/20 dark:border-yellow-800 dark:text-yellow-400';
      case 'baja': return 'border-green-200 bg-green-50 text-green-700 dark:bg-green-900/20 dark:border-green-800 dark:text-green-400';
      default: return 'border-slate-200 bg-slate-50 text-slate-700 dark:bg-slate-800 dark:border-slate-700 dark:text-slate-400';
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

  onGuardar(): void {
    if (this.validarFormulario()) {
      this.guardar.emit(this.formData);
    }
  }

  validarFormulario(): boolean {
    return !!(
      this.formData.asunto.trim() && 
      this.formData.mensaje.trim() && 
      this.formData.prioridadId && 
      this.formData.tipoId
    );
  }

  limpiarFormulario(): void {
    this.formData = {
      asunto: '',
      mensaje: '',
      prioridadId: null,
      tipoId: null
    };
  }

  onCerrar(): void {
    this.limpiarFormulario();
    this.cerrar.emit();
  }
}