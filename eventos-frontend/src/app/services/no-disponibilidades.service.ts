import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timeout, catchError, throwError } from 'rxjs';
import { NoDisponibilidad } from '../models/NoDisponibilidad.model';

@Injectable({ providedIn: 'root' })
export class NoDisponibilidadesService {
  private readonly base = '/ms-reservas/no-disponibilidades';
  private readonly timeoutMs = 10000;

  constructor(private http: HttpClient) {}

  getAll(): Observable<NoDisponibilidad[]> {
    return this.http.get<NoDisponibilidad[]>(this.base).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error obteniendo no disponibilidades:', err);
        return throwError(() => err);
      })
    );
  }

  create(payload: Omit<NoDisponibilidad, 'idNoDisponibilidad'>): Observable<NoDisponibilidad> {
    return this.http.post<NoDisponibilidad>(this.base, payload).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error creando no disponibilidad:', err);
        return throwError(() => err);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error eliminando no disponibilidad:', err);
        return throwError(() => err);
      })
    );
  }
}
 
