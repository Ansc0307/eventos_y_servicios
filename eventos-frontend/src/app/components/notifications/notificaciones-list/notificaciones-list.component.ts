import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { SwalService } from '../../../services/swal.service';
import { ModalService, ModalField } from '../../../services/modal.service';
import { Notificacion } from '../../../models/notifications/notification.model';
import { Prioridad } from '../../../models/notifications/prioridad.model';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';

@Component({
  selector: 'app-notificaciones-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificaciones-list.component.html',
  styleUrls: ['./notificaciones-list.component.css']
})
export class NotificacionesListComponent implements OnInit {
  notificaciones$: Observable<Notificacion[]> = of([]);
  prioridades: Prioridad[] = [];
  tiposNotificacion: TipoNotificacion[] = [];
  private notificacionesSubject = new BehaviorSubject<Notificacion[]>([]);
  loading = false;

  constructor(
    private notificacionesService: NotificacionesService,
    private swalService: SwalService,
    private modalService: ModalService
  ) {
    this.notificaciones$ = this.notificacionesSubject.asObservable();
  }

  ngOnInit(): void {
    this.cargarNotificaciones();
    this.cargarMetadata();
  }

  cargarNotificaciones(): void {
    this.loading = true;
    this.notificaciones$ = this.notificacionesService.getNotificaciones().pipe(
      tap(notificaciones => {
        this.notificacionesSubject.next(notificaciones);
        this.loading = false;
      }),
      catchError(error => {
        console.error('Error:', error);
        this.swalService.error('No se pudieron cargar las notificaciones');
        this.loading = false;
        return of([]);
      })
    );
  }

  cargarMetadata(): void {
    // Cargar prioridades y tipos para los selects del modal
    this.notificacionesService.getPrioridades().subscribe({
      next: (prioridades) => this.prioridades = prioridades,
      error: (error) => console.error('Error cargando prioridades:', error)
    });

    this.notificacionesService.getTiposNotificacion().subscribe({
      next: (tipos) => this.tiposNotificacion = tipos,
      error: (error) => console.error('Error cargando tipos:', error)
    });
  }

  abrirModalCrear(): void {
    const fields: ModalField[] = [
      {
        name: 'asunto',
        label: 'Asunto',
        type: 'text',
        required: true
      },
      {
        name: 'mensaje',
        label: 'Mensaje',
        type: 'textarea',
        required: true
      },
      {
        name: 'userId',
        label: 'User ID',
        type: 'number',
        required: true,
        value: 1
      },
      {
        name: 'prioridadId',
        label: 'Prioridad',
        type: 'select',
        required: true,
        options: this.prioridades.map(p => ({ id: p.id, nombre: p.nombre }))
      },
      {
        name: 'tipoId',
        label: 'Tipo de Notificación',
        type: 'select',
        required: true,
        options: this.tiposNotificacion.map(t => ({ id: t.id, nombre: t.nombre }))
      }
    ];

    this.modalService.abrirModal('Crear Nueva Notificación', fields, 'Crear')
      .then((result) => {
        if (result) {
          const notificacionData = {
            asunto: result.asunto,
            mensaje: result.mensaje,
            userId: parseInt(result.userId),
            prioridad: { id: parseInt(result.prioridadId) },
            tipoNotificacion: { id: parseInt(result.tipoId) }
          };
          this.crearNotificacion(notificacionData);
        }
      });
  }

  crearNotificacion(notificacionData: any): void {
    this.notificacionesService.crearNotificacion(notificacionData).subscribe({
      next: (nuevaNotificacion) => {
        const currentNotificaciones = this.notificacionesSubject.value;
        this.notificacionesSubject.next([nuevaNotificacion, ...currentNotificaciones]);
        this.swalService.success('Notificación creada correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo crear la notificación');
      }
    });
  }

  editarNotificacion(notificacion: Notificacion): void {
    const fields: ModalField[] = [
      {
        name: 'asunto',
        label: 'Asunto',
        type: 'text',
        required: true,
        value: notificacion.asunto
      },
      {
        name: 'mensaje',
        label: 'Mensaje',
        type: 'textarea',
        required: true,
        value: notificacion.mensaje
      },
      {
        name: 'userId',
        label: 'User ID',
        type: 'number',
        required: true,
        value: notificacion.userId
      },
      {
        name: 'prioridadId',
        label: 'Prioridad',
        type: 'select',
        required: true,
        value: notificacion.prioridad.id,
        options: this.prioridades.map(p => ({ id: p.id, nombre: p.nombre }))
      },
      {
        name: 'tipoId',
        label: 'Tipo de Notificación',
        type: 'select',
        required: true,
        value: notificacion.tipoNotificacion.id,
        options: this.tiposNotificacion.map(t => ({ id: t.id, nombre: t.nombre }))
      }
    ];

    this.modalService.abrirModal('Editar Notificación', fields, 'Actualizar')
      .then((result) => {
        if (result) {
          const notificacionData = {
            asunto: result.asunto,
            mensaje: result.mensaje,
            userId: parseInt(result.userId),
            prioridad: { id: parseInt(result.prioridadId) },
            tipoNotificacion: { id: parseInt(result.tipoId) }
          };
          this.actualizarNotificacion(notificacion.id, notificacionData);
        }
      });
  }

  actualizarNotificacion(id: number, notificacionData: any): void {
    this.notificacionesService.editarNotificacion(id, notificacionData).subscribe({
      next: (notificacionActualizada) => {
        const currentNotificaciones = this.notificacionesSubject.value;
        const updatedNotificaciones = currentNotificaciones.map(n => 
          n.id === id ? notificacionActualizada : n
        );
        this.notificacionesSubject.next(updatedNotificaciones);
        this.swalService.success('Notificación actualizada correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo actualizar la notificación');
      }
    });
  }

  eliminarNotificacion(id: number): void {
    this.swalService.confirm('¿Estás seguro de eliminar esta notificación?', 'Eliminar Notificación')
      .then((confirmado) => {
        if (confirmado) {
          this.notificacionesService.eliminarNotificacion(id).subscribe({
            next: () => {
              const currentNotificaciones = this.notificacionesSubject.value;
              const filteredNotificaciones = currentNotificaciones.filter(n => n.id !== id);
              this.notificacionesSubject.next(filteredNotificaciones);
              this.swalService.success('Notificación eliminada correctamente');
            },
            error: (error) => {
              console.error('Error:', error);
              this.swalService.error('No se pudo eliminar la notificación');
            }
          });
        }
      });
  }

  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleString('es-ES');
  }

  // Método para obtener nombre de prioridad
  getNombrePrioridad(prioridadId: number): string {
    const prioridad = this.prioridades.find(p => p.id === prioridadId);
    return prioridad?.nombre || `Prioridad ${prioridadId}`;
  }

  // Método para obtener nombre de tipo
  getNombreTipo(tipoId: number): string {
    const tipo = this.tiposNotificacion.find(t => t.id === tipoId);
    return tipo?.nombre || `Tipo ${tipoId}`;
  }

  // Método para obtener clase CSS según prioridad
  getPrioridadClass(prioridadNombre: string): string {
    if (!prioridadNombre) return 'badge badge-secondary';
    
    const nombreLower = prioridadNombre.toLowerCase();
    switch (nombreLower) {
      case 'alta': return 'badge badge-danger';
      case 'media': return 'badge badge-warning';
      case 'baja': return 'badge badge-success';
      default: return 'badge badge-info';
    }
  }

}