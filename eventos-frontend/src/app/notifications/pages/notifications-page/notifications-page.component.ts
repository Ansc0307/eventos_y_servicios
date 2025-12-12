import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
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
  //styleUrls: ['./notifications-page.component.css']
})
export class NotificationsPageComponent implements OnInit, OnDestroy {
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

  private destroy$ = new Subject<void>();

  constructor(
    private notificacionesService: NotificacionesService,
    private modalService: ModalService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
    this.cargarDatosModales();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  cargarNotificaciones(): void {
    this.isLoading = true;
    this.error = null;
    this.cdr.detectChanges(); // Forzar detección de cambios inicial
    
    console.log('Cargando notificaciones...');
    
    this.notificacionesService.getNotificacionesPorUsuario()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          console.log('Notificaciones cargadas:', data);
          this.notificaciones = data || [];
          this.aplicarFiltros();
          this.isLoading = false;
          this.cdr.detectChanges();
          console.log('isLoading después de cargar:', this.isLoading);
        },
        error: (err) => {
          console.error('Error al cargar notificaciones:', err);
          this.error = this.obtenerMensajeError(err);
          this.isLoading = false;
          this.notificaciones = [];
          this.filteredNotificaciones = [];
          this.cdr.detectChanges();
        },
        complete: () => {
          console.log('Carga completada');
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
  }

  obtenerMensajeError(error: any): string {
    console.log('Error detallado:', error);
    
    if (error.status === 0) {
      return 'Error de conexión: No se pudo conectar con el servidor.';
    } else if (error.status === 404) {
      return 'No se encontraron notificaciones para el usuario.';
    } else if (error.status === 401 || error.status === 403) {
      return 'No tienes permisos para ver las notificaciones.';
    } else {
      return `Error ${error.status}: ${error.message || 'Error desconocido'}`;
    }
  }

  cargarDatosModales(): void {
    // Cargar prioridades
    this.notificacionesService.getPrioridades()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.prioridades = data || [];
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al cargar prioridades:', err);
        }
      });

    // Cargar tipos
    this.notificacionesService.getTiposNotificacion()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.tipos = data || [];
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al cargar tipos:', err);
        }
      });
  }

  aplicarFiltros(): void {
    let filtradas = [...this.notificaciones];
    
    if (this.filtros.leido !== null) {
      filtradas = filtradas.filter(n => n.leido === this.filtros.leido);
    }
    
    if (this.filtros.prioridad !== null) {
      filtradas = filtradas.filter(n => n.prioridad.id === this.filtros.prioridad);
    }
    
    if (this.filtros.tipo !== null) {
      filtradas = filtradas.filter(n => n.tipoNotificacion.id === this.filtros.tipo);
    }
    
    this.filteredNotificaciones = filtradas;
    this.cdr.detectChanges();
    console.log('Filtros aplicados. Total:', this.notificaciones.length, 'Filtradas:', filtradas.length);
  }

  recargarNotificaciones(): void {
    this.cargarNotificaciones();
  }

  onMarcarComoLeida(id: number): void {
    this.notificacionesService.marcarComoLeida(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          // Actualizar localmente
          const index = this.notificaciones.findIndex(n => n.id === id);
          if (index !== -1) {
            this.notificaciones[index].leido = true;
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

    // Contador para saber cuándo todas han sido procesadas
    let procesadas = 0;
    
    noLeidas.forEach(notificacion => {
      this.notificacionesService.marcarComoLeida(notificacion.id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            notificacion.leido = true;
            procesadas++;
            
            // Cuando todas han sido procesadas, actualizar la vista
            if (procesadas === noLeidas.length) {
              this.aplicarFiltros();
            }
          },
          error: (err) => {
            console.error(`Error al marcar ${notificacion.id} como leída:`, err);
            procesadas++;
            
            if (procesadas === noLeidas.length) {
              this.aplicarFiltros();
            }
          }
        });
    });
  }

  onEliminarNotificacion(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar esta notificación?')) {
      this.notificacionesService.eliminarNotificacion(id)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
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
    if (this.prioridades.length === 0 || this.tipos.length === 0) {
      alert('Cargando datos, por favor espera...');
      return;
    }

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
          userId: 1,
          prioridad: { id: parseInt(resultado.prioridadId) },
          tipoNotificacion: { id: parseInt(resultado.tipoId) }
        };

        this.notificacionesService.crearNotificacion(nuevaNotificacion)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
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