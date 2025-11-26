import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';

@Component({
  selector: 'app-tipos-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tipos-list.component.html',
  styleUrls: ['./tipos-list.component.css']
})
export class TiposListComponent implements OnInit {
  tipos: TipoNotificacion[] = [];
  loading = true;
  guardando = false;
  mostrarFormulario = false;
  tipoEditando: TipoNotificacion | null = null;

  nuevoTipo: Partial<TipoNotificacion> = {
    id: 0,
    nombre: '',
    descripcion: ''
  };

  constructor(private notificacionesService: NotificacionesService) {}

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos(): void {
    this.loading = true;
    this.notificacionesService.getTiposNotificacion().subscribe({
      next: (data) => {
        this.tipos = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.loading = false;
        alert('Error al cargar tipos. Verifica que tengas permisos de administrador.');
      }
    });
  }

  mostrarFormularioCrear(): void {
    this.mostrarFormulario = true;
    this.tipoEditando = null;
    this.nuevoTipo = {
      id: 0,
      nombre: '',
      descripcion: ''
    };
  }

  editarTipo(tipo: TipoNotificacion): void {
    this.mostrarFormulario = true;
    this.tipoEditando = tipo;
    this.nuevoTipo = {
      id: tipo.id,
      nombre: tipo.nombre,
      descripcion: tipo.descripcion
    };
  }

  guardarTipo(): void {
    this.guardando = true;

    if (this.tipoEditando) {
      // Editar
      this.notificacionesService.editarTipoNotificacion(this.tipoEditando.id, this.nuevoTipo)
        .subscribe({
          next: (tipoActualizado) => {
            const index = this.tipos.findIndex(t => t.id === this.tipoEditando!.id);
            if (index !== -1) {
              this.tipos[index] = tipoActualizado;
            }
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al editar:', error);
            alert('Error al editar tipo. Verifica permisos de administrador.');
            this.guardando = false;
          }
        });
    } else {
      // Crear
      this.notificacionesService.crearTipoNotificacion(this.nuevoTipo)
        .subscribe({
          next: (nuevoTipo) => {
            this.tipos.push(nuevoTipo);
            this.cancelarEdicion();
            this.guardando = false;
          },
          error: (error) => {
            console.error('Error al crear:', error);
            alert('Error al crear tipo. Verifica permisos de administrador.');
            this.guardando = false;
          }
        });
    }
  }

  eliminarTipo(id: number): void {
    if (confirm('¿Estás seguro de eliminar este tipo de notificación?')) {
      this.notificacionesService.eliminarTipoNotificacion(id).subscribe({
        next: () => {
          this.tipos = this.tipos.filter(t => t.id !== id);
        },
        error: (error) => {
          console.error('Error al eliminar:', error);
          alert('Error al eliminar tipo. Verifica permisos de administrador.');
        }
      });
    }
  }

  cancelarEdicion(): void {
    this.mostrarFormulario = false;
    this.tipoEditando = null;
    this.nuevoTipo = {
      id: 0,
      nombre: '',
      descripcion: ''
    };
  }
}