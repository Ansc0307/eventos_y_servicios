import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Notificacion, NotificacionCreate } from '../models/notifications/notification.model';
import { Prioridad } from '../models/notifications/prioridad.model';
import { TipoNotificacion } from '../models/notifications/tipo-notificacion.model';

@Injectable({
  providedIn: 'root'
})
export class NotificacionesService {
  private baseUrl = '/ms-notifications/v1';
  // Eliminado: private userId = 1; // Ya no usamos usuario estático

  constructor(private http: HttpClient) {}

  // ===== NOTIFICACIONES =====
  getNotificaciones(): Observable<Notificacion[]> {
    return this.http.get<Notificacion[]>(`${this.baseUrl}/notificaciones`);
  }

  getNotificacionesPorUsuario(userId: number): Observable<Notificacion[]> {
    console.log('🔵 Llamando a getNotificacionesPorUsuario para userId:', userId);
    const url = `${this.baseUrl}/notificaciones/usuario/${userId}`;
    console.log('🔵 URL:', url);
    
    return this.http.get<Notificacion[]>(url).pipe(
      tap({
        next: (data) => console.log('Respuesta recibida:', data),
        error: (err) => console.error('Error en la petición:', err)
      })
    );
  }

  getNotificacionById(id: number): Observable<Notificacion> {
    return this.http.get<Notificacion>(`${this.baseUrl}/notificaciones/${id}`);
  }

  crearNotificacion(notificacion: NotificacionCreate): Observable<Notificacion> {
    return this.http.post<Notificacion>(`${this.baseUrl}/notificaciones`, notificacion);
  }

  editarNotificacion(id: number, notificacion: NotificacionCreate): Observable<Notificacion> {
    return this.http.put<Notificacion>(`${this.baseUrl}/notificaciones/${id}`, notificacion);
  }

  eliminarNotificacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/notificaciones/${id}`);
  }

  marcarComoLeida(id: number): Observable<Notificacion> {
    return this.http.patch<Notificacion>(`${this.baseUrl}/notificaciones/${id}/leida`, {});
  }

  // ===== PRIORIDADES =====
  getPrioridades(): Observable<Prioridad[]> {
    return this.http.get<Prioridad[]>(`${this.baseUrl}/prioridades`);
  }

  getPrioridadById(id: number): Observable<Prioridad> {
    return this.http.get<Prioridad>(`${this.baseUrl}/prioridades/${id}`);
  }

  crearPrioridad(prioridad: Partial<Prioridad>): Observable<Prioridad> {
    return this.http.post<Prioridad>(`${this.baseUrl}/prioridades`, prioridad);
  }

  editarPrioridad(id: number, prioridad: Partial<Prioridad>): Observable<Prioridad> {
    return this.http.put<Prioridad>(`${this.baseUrl}/prioridades/${id}`, prioridad);
  }

  eliminarPrioridad(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/prioridades/${id}`);
  }

  // ===== TIPOS =====
  getTiposNotificacion(): Observable<TipoNotificacion[]> {
    return this.http.get<TipoNotificacion[]>(`${this.baseUrl}/tipos-notificacion`);
  }

  getTipoNotificacionById(id: number): Observable<TipoNotificacion> {
    return this.http.get<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion/${id}`);
  }

  crearTipoNotificacion(tipo: Partial<TipoNotificacion>): Observable<TipoNotificacion> {
    return this.http.post<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion`, tipo);
  }

  editarTipoNotificacion(id: number, tipo: Partial<TipoNotificacion>): Observable<TipoNotificacion> {
    return this.http.put<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion/${id}`, tipo);
  }

  eliminarTipoNotificacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tipos-notificacion/${id}`);
  }

  /**
   * Envía una notificación a un usuario específico
   * @param asunto Asunto de la notificación
   * @param mensaje Contenido del mensaje
   * @param userId ID del usuario destinatario
   * @param prioridadId ID de la prioridad (ej: 1=BAJA, 2=MEDIA, 3=ALTA)
   * @param tipoId ID del tipo de notificación (ej: 1=RECORDATORIO, 2=ALERTA, 3=INFORMATIVA, 4=PROMOCIÓN, 5=SISTEMA)
   */
  enviarNotificacion(
    asunto: string, 
    mensaje: string, 
    userId: number, 
    prioridadId: number, 
    tipoId: number
  ): Observable<Notificacion> {
    
    const notificacion: NotificacionCreate = {
      asunto,
      mensaje,
      userId,
      prioridad: { id: prioridadId },
      tipoNotificacion: { id: tipoId }
    };
    
    console.log('Enviando notificación:', { asunto, userId, prioridadId, tipoId });
    
    return this.crearNotificacion(notificacion);
  }
}

