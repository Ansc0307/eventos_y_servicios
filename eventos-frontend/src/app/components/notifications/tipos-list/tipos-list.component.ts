import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { SwalService } from '../../../services/swal.service';
import { ModalService, ModalField } from '../../../services/modal.service';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';

@Component({
  selector: 'app-tipos-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tipos-list.component.html',
  styleUrls: ['./tipos-list.component.css']
})
export class TiposListComponent implements OnInit {
  tipos$: Observable<TipoNotificacion[]> = of([]);
  private tiposSubject = new BehaviorSubject<TipoNotificacion[]>([]);
  loading = false;

  constructor(
    private notificacionesService: NotificacionesService,
    private swalService: SwalService,
    private modalService: ModalService
  ) {
    this.tipos$ = this.tiposSubject.asObservable();
  }

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos(): void {
    this.loading = true;
    this.tipos$ = this.notificacionesService.getTiposNotificacion().pipe(
      tap(tipos => {
        this.tiposSubject.next(tipos);
        this.loading = false;
      }),
      catchError(error => {
        console.error('Error:', error);
        this.swalService.error('No se pudieron cargar los tipos de notificación');
        this.loading = false;
        return of([]);
      })
    );
  }

  abrirModalCrear(): void {
    const fields: ModalField[] = [
      {
        name: 'nombre',
        label: 'Nombre',
        type: 'text',
        required: true
      },
      {
        name: 'descripcion', 
        label: 'Descripción',
        type: 'textarea',
        required: false
      }
    ];

    this.modalService.abrirModal('Crear Nuevo Tipo', fields, 'Crear')
      .then((result) => {
        if (result) {
          this.crearTipo(result);
        }
      });
  }

  crearTipo(tipoData: any): void {
    this.notificacionesService.crearTipoNotificacion(tipoData).subscribe({
      next: (nuevoTipo) => {
        const currentTipos = this.tiposSubject.value;
        this.tiposSubject.next([...currentTipos, nuevoTipo]);
        this.swalService.success('Tipo creado correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo crear el tipo');
      }
    });
  }

  editarTipo(tipo: TipoNotificacion): void {
    const fields: ModalField[] = [
      {
        name: 'nombre',
        label: 'Nombre',
        type: 'text',
        required: true,
        value: tipo.nombre
      },
      {
        name: 'descripcion',
        label: 'Descripción', 
        type: 'textarea',
        required: false,
        value: tipo.descripcion || ''
      }
    ];

    this.modalService.abrirModal('Editar Tipo', fields, 'Actualizar')
      .then((result) => {
        if (result) {
          this.actualizarTipo(tipo.id, result);
        }
      });
  }

  actualizarTipo(id: number, tipoData: any): void {
    this.notificacionesService.editarTipoNotificacion(id, tipoData).subscribe({
      next: (tipoActualizado) => {
        const currentTipos = this.tiposSubject.value;
        const updatedTipos = currentTipos.map(t => 
          t.id === id ? tipoActualizado : t
        );
        this.tiposSubject.next(updatedTipos);
        this.swalService.success('Tipo actualizado correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo actualizar el tipo');
      }
    });
  }

  eliminarTipo(id: number): void {
    this.swalService.confirm('¿Estás seguro de eliminar este tipo?', 'Eliminar Tipo')
      .then((confirmado) => {
        if (confirmado) {
          this.notificacionesService.eliminarTipoNotificacion(id).subscribe({
            next: () => {
              const currentTipos = this.tiposSubject.value;
              const filteredTipos = currentTipos.filter(t => t.id !== id);
              this.tiposSubject.next(filteredTipos);
              this.swalService.success('Tipo eliminado correctamente');
            },
            error: (error) => {
              console.error('Error:', error);
              this.swalService.error('No se pudo eliminar el tipo');
            }
          });
        }
      });
  }
}