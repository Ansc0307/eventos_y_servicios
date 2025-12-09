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
  private baseUrl = '/ms-notifications/v1';

  constructor(private http: HttpClient) {}

  // ===== NOTIFICACIONES =====
  getNotificaciones(): Observable<Notificacion[]> {
    return this.http.get<Notificacion[]>(`${this.baseUrl}/notificaciones`);
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
}