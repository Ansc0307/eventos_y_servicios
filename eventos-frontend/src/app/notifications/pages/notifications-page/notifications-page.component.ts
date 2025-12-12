import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { ModalService } from '../../../services/modal.service';
import { Notificacion, NotificacionCreate } from '../../../models/notifications/notification.model';
import { Prioridad } from '../../../models/notifications/prioridad.model';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';
import { NotificationItemComponent } from '../../components/notification-item/notification-item.component';
import { NotificationFilterComponent } from '../../components/notification-filter/notification-filter.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { EmptyStateComponent } from '../../components/empty-state/empty-state.component';

@Component({
  selector: 'app-notifications-page',
  standalone: true,
  imports: [
    CommonModule, 
    NotificationItemComponent, 
    NotificationFilterComponent,
    LoaderComponent,
    EmptyStateComponent
  ],
  templateUrl: './notifications-page.component.html',
  styleUrls: ['./notifications-page.component.css']
})
export class NotificationsPageComponent implements OnInit {
  notificaciones: Notificacion[] = [];
  filteredNotificaciones: Notificacion[] = [];
  isLoading: boolean = true;
  error: string | null = null;
  
  // Datos para modales
  prioridades: Prioridad[] = [];
  tipos: TipoNotificacion[] = [];
  
  // Filtros
  filtros = {
    leido: null as boolean | null,
    prioridad: null as number | null,
    tipo: null as number | null
  };

  constructor(
    private notificacionesService: NotificacionesService,
    private modalService: ModalService
  ) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
    this.cargarDatosModales();
  }

  cargarNotificaciones(): void {
    this.isLoading = true;
    this.error = null;
    
    this.notificacionesService.getNotificacionesPorUsuario().subscribe({
      next: (data) => {
        this.notificaciones = data;
        this.aplicarFiltros();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar notificaciones:', err);
        this.error = 'Error al cargar las notificaciones';
        this.isLoading = false;
      }
    });
  }

  cargarDatosModales(): void {
    // Cargar prioridades
    this.notificacionesService.getPrioridades().subscribe({
      next: (data) => {
        this.prioridades = data;
      },
      error: (err) => {
        console.error('Error al cargar prioridades:', err);
      }
    });

    // Cargar tipos
    this.notificacionesService.getTiposNotificacion().subscribe({
      next: (data) => {
        this.tipos = data;
      },
      error: (err) => {
        console.error('Error al cargar tipos:', err);
      }
    });
  }

  aplicarFiltros(): void {
    let filtradas = [...this.notificaciones];
    
    // Filtro por estado de lectura
    if (this.filtros.leido !== null) {
      filtradas = filtradas.filter(n => n.leido === this.filtros.leido);
    }
    
    // Filtro por prioridad
    if (this.filtros.prioridad !== null) {
      filtradas = filtradas.filter(n => n.prioridad.id === this.filtros.prioridad);
    }
    
    // Filtro por tipo
    if (this.filtros.tipo !== null) {
      filtradas = filtradas.filter(n => n.tipoNotificacion.id === this.filtros.tipo);
    }
    
    this.filteredNotificaciones = filtradas;
  }

  onMarcarComoLeida(id: number): void {
    this.notificacionesService.marcarComoLeida(id).subscribe({
      next: (notificacionActualizada) => {
        // Actualizar la notificación en la lista
        const index = this.notificaciones.findIndex(n => n.id === id);
        if (index !== -1) {
          this.notificaciones[index] = notificacionActualizada;
          this.aplicarFiltros();
        }
      },
      error: (err) => {
        console.error('Error al marcar como leída:', err);
      }
    });
  }

  marcarTodasComoLeidas(): void {
    const noLeidas = this.notificaciones.filter(n => !n.leido);
    
    if (noLeidas.length === 0) {
      return;
    }

    // Marcar todas como leídas (en un caso real, debería haber un endpoint para esto)
    noLeidas.forEach(notificacion => {
      this.notificacionesService.marcarComoLeida(notificacion.id).subscribe({
        next: (notificacionActualizada) => {
          const index = this.notificaciones.findIndex(n => n.id === notificacion.id);
          if (index !== -1) {
            this.notificaciones[index] = notificacionActualizada;
          }
        },
        error: (err) => {
          console.error(`Error al marcar notificación ${notificacion.id} como leída:`, err);
        }
      });
    });

    // Actualizar la lista después de un breve delay
    setTimeout(() => {
      this.aplicarFiltros();
    }, 500);
  }

  onEliminarNotificacion(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta notificación?')) {
      this.notificacionesService.eliminarNotificacion(id).subscribe({
        next: () => {
          // Eliminar de la lista
          this.notificaciones = this.notificaciones.filter(n => n.id !== id);
          this.aplicarFiltros();
        },
        error: (err) => {
          console.error('Error al eliminar notificación:', err);
        }
      });
    }
  }

  onCrearNotificacion(): void {
    const campos = [
      {
        name: 'asunto',
        label: 'Asunto',
        type: 'text' as const,
        required: true
      },
      {
        name: 'mensaje',
        label: 'Mensaje',
        type: 'textarea' as const,
        required: true
      },
      {
        name: 'prioridadId',
        label: 'Prioridad',
        type: 'select' as const,
        required: true,
        options: this.prioridades.map(p => ({ id: p.id, nombre: p.nombre }))
      },
      {
        name: 'tipoId',
        label: 'Tipo',
        type: 'select' as const,
        required: true,
        options: this.tipos.map(t => ({ id: t.id, nombre: t.nombre }))
      }
    ];

    this.modalService.abrirModal('Crear Nueva Notificación', campos, 'Crear').then(resultado => {
      if (resultado) {
        const nuevaNotificacion: NotificacionCreate = {
          asunto: resultado.asunto,
          mensaje: resultado.mensaje,
          userId: 1, // Usuario actual (deberías obtener esto de tu servicio de autenticación)
          prioridad: { id: parseInt(resultado.prioridadId) },
          tipoNotificacion: { id: parseInt(resultado.tipoId) }
        };

        this.notificacionesService.crearNotificacion(nuevaNotificacion).subscribe({
          next: (notificacionCreada) => {
            this.notificaciones.unshift(notificacionCreada);
            this.aplicarFiltros();
          },
          error: (err) => {
            console.error('Error al crear notificación:', err);
          }
        });
      }
    });
  }

  onFiltrosCambiados(filtros: any): void {
    this.filtros = { ...filtros };
    this.aplicarFiltros();
  }

  contarNoLeidas(): number {
    return this.notificaciones.filter(n => !n.leido).length;
  }
}