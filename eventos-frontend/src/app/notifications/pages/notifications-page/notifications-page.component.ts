import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { NotificacionesService } from '../../../services/notificaciones.service';
import { Notificacion, NotificacionCreate } from '../../../models/notifications/notification.model';
import { Prioridad } from '../../../models/notifications/prioridad.model';
import { TipoNotificacion } from '../../../models/notifications/tipo-notificacion.model';
import { NotificationItemComponent } from '../../components/notification-item/notification-item.component';
import { NotificationFilterComponent } from '../../components/notification-filter/notification-filter.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { EmptyStateComponent } from '../../components/empty-state/empty-state.component';
import { ModalNotificacionComponent } from '../../components/modal-notification/modal-notification.component';

@Component({
  selector: 'app-notifications-page',
  standalone: true,
  imports: [
    CommonModule, 
    NotificationItemComponent, 
    NotificationFilterComponent,
    LoaderComponent,
    EmptyStateComponent,
    ModalNotificacionComponent 
  ],
  templateUrl: './notifications-page.component.html',
  //styleUrls: ['./notifications-page.component.css']
})
export class NotificationsPageComponent implements OnInit, OnDestroy {
  notificaciones: Notificacion[] = [];
  filteredNotificaciones: Notificacion[] = [];
  isLoading: boolean = true;
  error: string | null = null;
  
  // Para el modal personalizado
  mostrarModalNuevaNotificacion: boolean = false;
  creandoNotificacion: boolean = false;
  
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
    this.cdr.detectChanges();
    
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
  }

  recargarNotificaciones(): void {
    this.cargarNotificaciones();
  }

  onMarcarComoLeida(id: number): void {
    this.notificacionesService.marcarComoLeida(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          const index = this.notificaciones.findIndex(n => n.id === id);
          if (index !== -1) {
            this.notificaciones[index].leido = true;
            this.aplicarFiltros();
          }
        },
        error: (err) => {
          console.error('Error al marcar como leída:', err);
          this.mostrarSweetAlert('error', 'Error', 'No se pudo marcar como leída');
        }
      });
  }

  marcarTodasComoLeidas(): void {
    const noLeidas = this.notificaciones.filter(n => !n.leido);
    
    if (noLeidas.length === 0) {
      this.mostrarSweetAlert('info', 'Información', 'No hay notificaciones por marcar como leídas');
      return;
    }

    // Mostrar confirmación
    this.mostrarSweetAlertConfirm(
      '¿Marcar todas como leídas?',
      `Se marcarán ${noLeidas.length} notificaciones como leídas`,
      'question'
    ).then((result) => {
      if (result.isConfirmed) {
        let procesadas = 0;
        const total = noLeidas.length;
        
        noLeidas.forEach(notificacion => {
          this.notificacionesService.marcarComoLeida(notificacion.id)
            .pipe(takeUntil(this.destroy$))
            .subscribe({
              next: () => {
                notificacion.leido = true;
                procesadas++;
                
                if (procesadas === total) {
                  this.aplicarFiltros();
                  this.mostrarSweetAlert('success', '¡Éxito!', `Se marcaron ${total} notificaciones como leídas`);
                }
              },
              error: (err) => {
                console.error(`Error al marcar ${notificacion.id} como leída:`, err);
                procesadas++;
                
                if (procesadas === total) {
                  this.aplicarFiltros();
                }
              }
            });
        });
      }
    });
  }

  onEliminarNotificacion(id: number): void {
    this.mostrarSweetAlertConfirm(
      '¿Eliminar notificación?',
      'Esta acción no se puede deshacer',
      'warning',
      'Sí, eliminar',
      'Cancelar'
    ).then((result) => {
      if (result.isConfirmed) {
        this.notificacionesService.eliminarNotificacion(id)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            next: () => {
              this.notificaciones = this.notificaciones.filter(n => n.id !== id);
              this.aplicarFiltros();
              this.mostrarSweetAlert('success', '¡Eliminada!', 'La notificación fue eliminada');
            },
            error: (err) => {
              console.error('Error al eliminar notificación:', err);
              this.mostrarSweetAlert('error', 'Error', 'No se pudo eliminar la notificación');
            }
          });
      }
    });
  }

  // ========== MÉTODOS PARA EL MODAL PERSONALIZADO ==========

  onCrearNotificacion(): void {
    if (this.prioridades.length === 0 || this.tipos.length === 0) {
      this.mostrarSweetAlert('warning', 'Espera', 'Cargando datos, por favor espera...');
      return;
    }
    
    this.mostrarModalNuevaNotificacion = true;
    this.cdr.detectChanges();
  }

  cerrarModalNuevaNotificacion(): void {
    this.mostrarModalNuevaNotificacion = false;
    this.creandoNotificacion = false;
    this.cdr.detectChanges();
  }

  crearNotificacion(formData: any): void {
    this.creandoNotificacion = true;
    this.cdr.detectChanges();

    const nuevaNotificacion: NotificacionCreate = {
      asunto: formData.asunto,
      mensaje: formData.mensaje,
      userId: 1, // TODO: Obtener del servicio de autenticación
      prioridad: { id: formData.prioridadId },
      tipoNotificacion: { id: formData.tipoId }
    };

    this.notificacionesService.crearNotificacion(nuevaNotificacion)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (notificacionCreada) => {
          this.notificaciones.unshift(notificacionCreada);
          this.aplicarFiltros();
          this.cerrarModalNuevaNotificacion();
          this.mostrarSweetAlert('success', '¡Creada!', 'La notificación fue creada exitosamente');
        },
        error: (err) => {
          console.error('Error al crear notificación:', err);
          this.creandoNotificacion = false;
          this.cdr.detectChanges();
          this.mostrarSweetAlert('error', 'Error', 'No se pudo crear la notificación');
        }
      });
  }

  // ========== MÉTODOS PARA SWEETALERT2 ==========

  private mostrarSweetAlert(icon: 'success' | 'error' | 'warning' | 'info', title: string, text: string): void {
    import('sweetalert2').then(Swal => {
      Swal.default.fire({
        icon,
        title,
        text,
        timer: icon === 'success' ? 2000 : 3000,
        showConfirmButton: icon !== 'success'
      });
    });
  }

  private mostrarSweetAlertConfirm(
    title: string, 
    text: string, 
    icon: 'warning' | 'question' | 'info' = 'warning',
    confirmButtonText: string = 'Confirmar',
    cancelButtonText: string = 'Cancelar'
  ): Promise<any> {
    return import('sweetalert2').then(Swal => {
      return Swal.default.fire({
        title,
        text,
        icon,
        showCancelButton: true,
        confirmButtonText,
        cancelButtonText,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33'
      });
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