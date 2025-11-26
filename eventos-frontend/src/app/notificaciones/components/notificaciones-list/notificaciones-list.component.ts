import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { Notificacion, NotificacionCreate } from '../../../models/notifications/notification.model';

@Component({
  selector: 'app-notificaciones-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notificaciones-list.component.html',
  styleUrls: ['./notificaciones-list.component.css']
})
export class NotificacionesListComponent implements OnInit {
  notificaciones: Notificacion[] = [];
  loading = true;
  guardando = false;
  mostrarFormulario = false;
  notificacionEditando: Notificacion | null = null;

  nuevaNotificacion: NotificacionCreate = {
    asunto: '',
    mensaje: '',
    userId: 1,
    prioridad: { id: 1 },
    tipoNotificacion: { id: 1 }
  };

  constructor(private notificacionesService: NotificacionesService) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
  }

  cargarNotificaciones(): void {
    this.loading = true;
    this.notificacionesService.getNotificaciones().subscribe({
      next: (data) => {
        this.notificaciones = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.loading = false;
      }
    });
  }

  mostrarFormularioCrear(): void {
    this.mostrarFormulario = true;
    this.notificacionEditando = null;
    this.nuevaNotificacion = {
      asunto: '',
      mensaje: '',
      userId: 1,
      prioridad: { id: 1 },
      tipoNotificacion: { id: 1 }
    };
  }

  editarNotificacion(notificacion: Notificacion): void {
    this.mostrarFormulario = true;
    this.notificacionEditando = notificacion;
    this.nuevaNotificacion = {
      asunto: notificacion.asunto,
      mensaje: notificacion.mensaje,
      userId: notificacion.userId,
      prioridad: { id: notificacion.prioridad.id },
      tipoNotificacion: { id: notificacion.tipoNotificacion.id }
    };
  }

  guardarNotificacion(): void {
    this.guardando = true;

    if (this.notificacionEditando) {
      // Editar
      this.notificacionesService.editarNotificacion(this.notificacionEditando.id, this.nuevaNotificacion)
        .subscribe({
          next: (notificacionActualizada) => {
            const index = this.notificaciones.findIndex(n => n.id === this.notificacionEditando!.id);
            if (index !== -1) {
              this.notificaciones[index] = notificacionActualizada;
            }
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al editar:', error);
            this.guardando = false;
          }
        });
    } else {
      // Crear
      this.notificacionesService.crearNotificacion(this.nuevaNotificacion)
        .subscribe({
          next: (nuevaNotif) => {
            this.notificaciones.unshift(nuevaNotif);
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al crear:', error);
            this.guardando = false;
          }
        });
    }
  }

  eliminarNotificacion(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta notificación?')) {
      this.notificacionesService.eliminarNotificacion(id).subscribe({
        next: () => {
          this.notificaciones = this.notificaciones.filter(n => n.id !== id);
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
        }
      });
    }
  }

  cancelarEdicion(): void {
    this.mostrarFormulario = false;
    this.notificacionEditando = null;
    this.nuevaNotificacion = {
      asunto: '',
      mensaje: '',
      userId: 1,
      prioridad: { id: 1 },
      tipoNotificacion: { id: 1 }
    };
  }

  getPrioridadClass(prioridadNombre: string): string {
    if (!prioridadNombre) return 'bg-secondary text-white';
    
    switch (prioridadNombre.toLowerCase()) {
      case 'alta': return 'bg-danger text-white';
      case 'media': return 'bg-warning text-dark';
      case 'baja': return 'bg-success text-white';
      default: return 'bg-secondary text-white';
    }
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleString('es-ES');
  }
}