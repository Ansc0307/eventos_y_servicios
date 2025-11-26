import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reserva } from '../models/reserva.model';

@Injectable({ providedIn: 'root' })
export class ReservasService {
  // use the frontend proxy: /api -> http://localhost:8080/ms-reservas
  private readonly base = '/api/v1/reservas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.base);
  }

  getById(id: number): Observable<Reserva> {
    return this.http.get<Reserva>(`${this.base}/${id}`);
  }

  create(reserva: any): Observable<Reserva> {
    return this.http.post<Reserva>(this.base, reserva);
  }

  update(id: number, reserva: any): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.base}/${id}`, reserva);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getByEstado(estado: string): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/estado/${estado}`);
  }

  getByIdSolicitud(idSolicitud: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.base}/solicitud/${idSolicitud}`);
  }
}
