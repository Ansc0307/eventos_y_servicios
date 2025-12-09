import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, tap, finalize } from 'rxjs/operators';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { SwalService } from '../../../services/swal.service';
import { ModalService, ModalField } from '../../../services/modal.service';
import { Prioridad } from '../../../models/notifications/prioridad.model';

@Component({
  selector: 'app-prioridades-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './prioridades-list.component.html',
  styleUrls: ['./prioridades-list.component.css']
})
export class PrioridadesListComponent implements OnInit {
  prioridades$: Observable<Prioridad[]> = of([]);
  private prioridadesSubject = new BehaviorSubject<Prioridad[]>([]);

  loading = false;

  constructor(
    private notificacionesService: NotificacionesService,
    private swalService: SwalService,
    private modalService: ModalService
  ) {
    this.prioridades$ = this.prioridadesSubject.asObservable();
  }

  ngOnInit(): void {
    this.cargarPrioridades();
  }

  cargarPrioridades(): void {
    this.loading = true;

    this.notificacionesService.getPrioridades().pipe(
        tap(prioridades => {
        // actualizamos el BehaviorSubject para que el async pipe reciba datos
        this.prioridadesSubject.next(prioridades);
        }),
        catchError(error => {
        console.error('Error:', error);
        this.swalService.error('No se pudieron cargar las prioridades');
        // devolvemos array vacío para que el flujo continue
        return of([]);
        }),
        finalize(() => {
        // siempre se ejecuta, éxito o error
        this.loading = false;
        })
    ).subscribe();
    
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

    this.modalService.abrirModal('Crear Nueva Prioridad', fields, 'Crear')
      .then((result) => {
        if (result) {
          this.crearPrioridad(result);
        }
      });
  }

  crearPrioridad(prioridadData: any): void {
    this.notificacionesService.crearPrioridad(prioridadData).subscribe({
      next: (nuevaPrioridad) => {
        const currentPrioridades = this.prioridadesSubject.value;
        this.prioridadesSubject.next([...currentPrioridades, nuevaPrioridad]);
        this.swalService.success('Prioridad creada correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo crear la prioridad');
      }
    });
  }

  editarPrioridad(prioridad: Prioridad): void {
    const fields: ModalField[] = [
      {
        name: 'nombre',
        label: 'Nombre',
        type: 'text',
        required: true,
        value: prioridad.nombre
      },
      {
        name: 'descripcion',
        label: 'Descripción', 
        type: 'textarea',
        required: false,
        value: prioridad.descripcion || ''
      }
    ];

    this.modalService.abrirModal('Editar Prioridad', fields, 'Actualizar')
      .then((result) => {
        if (result) {
          this.actualizarPrioridad(prioridad.id, result);
        }
      });
  }

  actualizarPrioridad(id: number, prioridadData: any): void {
    this.notificacionesService.editarPrioridad(id, prioridadData).subscribe({
      next: (prioridadActualizada) => {
        const currentPrioridades = this.prioridadesSubject.value;
        const updatedPrioridades = currentPrioridades.map(p => 
          p.id === id ? prioridadActualizada : p
        );
        this.prioridadesSubject.next(updatedPrioridades);
        this.swalService.success('Prioridad actualizada correctamente');
      },
      error: (error) => {
        console.error('Error:', error);
        this.swalService.error('No se pudo actualizar la prioridad');
      }
    });
  }

  eliminarPrioridad(id: number): void {
    this.swalService.confirm('¿Estás seguro de eliminar esta prioridad?', 'Eliminar Prioridad')
      .then((confirmado) => {
        if (confirmado) {
          this.notificacionesService.eliminarPrioridad(id).subscribe({
            next: () => {
              const currentPrioridades = this.prioridadesSubject.value;
              const filteredPrioridades = currentPrioridades.filter(p => p.id !== id);
              this.prioridadesSubject.next(filteredPrioridades);
              this.swalService.success('Prioridad eliminada correctamente');
            },
            error: (error) => {
              console.error('Error:', error);
              this.swalService.error('No se pudo eliminar la prioridad');
            }
          });
        }
      });
  }

  getPrioridadClass(prioridadNombre: string): string {
    if (!prioridadNombre) return 'badge-secondary';
    
    switch (prioridadNombre.toLowerCase()) {
      case 'alta': return 'badge prioridad-alta';
      case 'media': return 'badge prioridad-media';
      case 'baja': return 'badge prioridad-baja';
      default: return 'badge prioridad-default';
    }
  }
}