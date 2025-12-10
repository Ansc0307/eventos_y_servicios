import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timeout, catchError, throwError } from 'rxjs';
import { Reserva } from '../models/reserva.model';
import { Solicitud } from '../models/solicitud.model';

@Injectable({ providedIn: 'root' })
export class ReservasService {
  // use the frontend proxy: /ms-reservas -> http://localhost:8080/ms-reservas (gateway)
  private readonly base = '/ms-reservas/v1/reservas';

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

  getSolicitudByReservaId(idReserva: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.base}/${idReserva}/solicitud`).pipe(
      timeout(10000),
      catchError(err => {
        console.error('Error obteniendo solicitud por reserva:', err);
        return throwError(() => err);
      })
    );
  }
}
