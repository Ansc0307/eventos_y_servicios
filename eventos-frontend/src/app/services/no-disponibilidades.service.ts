import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';

@Injectable({ providedIn: 'root' })
export class NoDisponibilidadesService {
  // use the frontend proxy: /api -> http://localhost:8080/ms-reservas
  private readonly base = '/ms-reservas/no-disponibilidades';

  constructor(private http: HttpClient) {}

  getAll(): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(this.base);
  }

  getById(id: number): Observable<NoDisponibilidad> {
    return this.http.get<NoDisponibilidad>(`${this.base}/${id}`);
  }

  create(noDisponibilidad: any): Observable<NoDisponibilidad> {
    return this.http.post<NoDisponibilidad>(this.base, noDisponibilidad);
  }

  update(id: number, noDisponibilidad: any): Observable<NoDisponibilidad> {
    return this.http.put<NoDisponibilidad>(`${this.base}/${id}`, noDisponibilidad);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getByOferta(idOferta: number): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/oferta/${idOferta}`);
  }

  getActivasByOferta(idOferta: number): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/oferta/${idOferta}/activas`);
  }

  buscarPorMotivo(motivo: string): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/buscar/motivo`, {
      params: { motivo }
    });
  }

  buscarPorRango(inicio: string, fin: string): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/rango`, {
      params: { inicio, fin }
    });
  }

  buscarConflictos(inicio: string, fin: string): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/conflictos`, {
      params: { inicio, fin }
    });
  }

  conReserva(): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/con-reserva`);
  }

  sinReserva(): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(`${this.base}/sin-reserva`);
  }
}
