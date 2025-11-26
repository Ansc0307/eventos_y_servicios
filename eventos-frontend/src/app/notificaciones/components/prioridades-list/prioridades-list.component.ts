import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { Prioridad } from '../../../models/notifications/prioridad.model';

@Component({
  selector: 'app-prioridades-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './prioridades-list.component.html',
  styleUrls: ['./prioridades-list.component.css']
})
export class PrioridadesListComponent implements OnInit {
  prioridades: Prioridad[] = [];
  loading = true;
  guardando = false;
  mostrarFormulario = false;
  prioridadEditando: Prioridad | null = null;

  nuevaPrioridad: Partial<Prioridad> = {
    id: 0,
    nombre: '',
    descripcion: ''
  };

  constructor(private notificacionesService: NotificacionesService) {}

  ngOnInit(): void {
    this.cargarPrioridades();
  }

  cargarPrioridades(): void {
    this.loading = true;
    this.notificacionesService.getPrioridades().subscribe({
      next: (data) => {
        this.prioridades = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.loading = false;
        alert('Error al cargar prioridades. Verifica que tengas permisos de administrador.');
      }
    });
  }

  mostrarFormularioCrear(): void {
    this.mostrarFormulario = true;
    this.prioridadEditando = null;
    this.nuevaPrioridad = {
      id: 0,
      nombre: '',
      descripcion: ''
    };
  }

  editarPrioridad(prioridad: Prioridad): void {
    this.mostrarFormulario = true;
    this.prioridadEditando = prioridad;
    this.nuevaPrioridad = {
      id: prioridad.id,
      nombre: prioridad.nombre,
      descripcion: prioridad.descripcion
    };
  }

  guardarPrioridad(): void {
    this.guardando = true;

    if (this.prioridadEditando) {
      // Editar
      this.notificacionesService.editarPrioridad(this.prioridadEditando.id, this.nuevaPrioridad)
        .subscribe({
          next: (prioridadActualizada) => {
            const index = this.prioridades.findIndex(p => p.id === this.prioridadEditando!.id);
            if (index !== -1) {
              this.prioridades[index] = prioridadActualizada;
            }
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al editar:', error);
            alert('Error al editar prioridad. Verifica permisos de administrador.');
            this.guardando = false;
          }
        });
    } else {
      // Crear
      this.notificacionesService.crearPrioridad(this.nuevaPrioridad)
        .subscribe({
          next: (nuevaPrioridad) => {
            this.prioridades.push(nuevaPrioridad);
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al crear:', error);
            alert('Error al crear prioridad. Verifica permisos de administrador.');
            this.guardando = false;
          }
        });
    }
  }

  eliminarPrioridad(id: number): void {
    if (confirm('¿Estás seguro de eliminar esta prioridad?')) {
      this.notificacionesService.eliminarPrioridad(id).subscribe({
        next: () => {
          this.prioridades = this.prioridades.filter(p => p.id !== id);
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          alert('Error al eliminar prioridad. Verifica permisos de administrador.');
        }
      });
    }
  }

  cancelarEdicion(): void {
    this.mostrarFormulario = false;
    this.prioridadEditando = null;
    this.nuevaPrioridad = {
      id: 0,
      nombre: '',
      descripcion: ''
    };
  }

  getPrioridadClass(nombre: string): string {
    if (!nombre) return 'bg-secondary text-white';
    
    switch (nombre.toLowerCase()) {
      case 'alta': return 'bg-danger text-white';
      case 'media': return 'bg-warning text-dark';
      case 'baja': return 'bg-success text-white';
      case 'urgente': return 'bg-dark text-white';
      default: return 'bg-info text-white';
    }
  }
}