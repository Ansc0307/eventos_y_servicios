import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notificacion, NotificacionCreate } from '../models/notifications/notification.model';
import { Prioridad } from '../models/notifications/prioridad.model';
import { TipoNotificacion } from '../models/notifications/tipo-notificacion.model';

@Injectable({
  providedIn: 'root'
})
export class NotificacionesService {
  private baseUrl = 'http://localhost:8080/ms-notifications/v1';

  constructor(private http: HttpClient) {}

  // ===== NOTIFICACIONES ===== (Acceso para todos)
  
  // Listar todas las notificaciones
  getNotificaciones(): Observable<Notificacion[]> {
    return this.http.get<Notificacion[]>(`${this.baseUrl}/notificaciones`);
  }

  // Obtener notificación por ID
  getNotificacionById(id: number): Observable<Notificacion> {
    return this.http.get<Notificacion>(`${this.baseUrl}/notificaciones/${id}`);
  }

  // Crear nueva notificación
  crearNotificacion(notificacion: NotificacionCreate): Observable<Notificacion> {
    return this.http.post<Notificacion>(`${this.baseUrl}/notificaciones`, notificacion);
  }

  // Editar notificación
  editarNotificacion(id: number, notificacion: NotificacionCreate): Observable<Notificacion> {
    return this.http.put<Notificacion>(`${this.baseUrl}/notificaciones/${id}`, notificacion);
  }

  // Eliminar notificación
  eliminarNotificacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/notificaciones/${id}`);
  }

  // ===== PRIORIDADES ===== (Solo admin)
  
  // Listar todas las prioridades
  getPrioridades(): Observable<Prioridad[]> {
    return this.http.get<Prioridad[]>(`${this.baseUrl}/prioridades`);
  }

  // Obtener prioridad por ID
  getPrioridadById(id: number): Observable<Prioridad> {
    return this.http.get<Prioridad>(`${this.baseUrl}/prioridades/${id}`);
  }

  // Crear nueva prioridad
  crearPrioridad(prioridad: Partial<Prioridad>): Observable<Prioridad> {
    return this.http.post<Prioridad>(`${this.baseUrl}/prioridades`, prioridad);
  }

  // Editar prioridad
  editarPrioridad(id: number, prioridad: Partial<Prioridad>): Observable<Prioridad> {
    return this.http.put<Prioridad>(`${this.baseUrl}/prioridades/${id}`, prioridad);
  }

  // Eliminar prioridad
  eliminarPrioridad(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/prioridades/${id}`);
  }

  // ===== TIPOS DE NOTIFICACIÓN ===== (Solo admin)
  
  // Listar todos los tipos
  getTiposNotificacion(): Observable<TipoNotificacion[]> {
    return this.http.get<TipoNotificacion[]>(`${this.baseUrl}/tipos-notificacion`);
  }

  // Obtener tipo por ID
  getTipoNotificacionById(id: number): Observable<TipoNotificacion> {
    return this.http.get<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion/${id}`);
  }

  // Crear nuevo tipo
  crearTipoNotificacion(tipo: Partial<TipoNotificacion>): Observable<TipoNotificacion> {
    return this.http.post<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion`, tipo);
  }

  // Editar tipo
  editarTipoNotificacion(id: number, tipo: Partial<TipoNotificacion>): Observable<TipoNotificacion> {
    return this.http.put<TipoNotificacion>(`${this.baseUrl}/tipos-notificacion/${id}`, tipo);
  }

  // Eliminar tipo
  eliminarTipoNotificacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tipos-notificacion/${id}`);
  }
}