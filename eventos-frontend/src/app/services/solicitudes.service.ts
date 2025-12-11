import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, timeout, catchError, throwError } from 'rxjs';
import { Solicitud } from '../models/solicitud.model';

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  // frontend proxy: /ms-reservas -> http://localhost:8080/ms-reservas
  private readonly base = '/ms-reservas/solicitudes';
  private readonly timeoutMs = 10000; // 10 segundos

  constructor(private http: HttpClient) {}

  getAll(): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(this.base).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en getAll:', err);
        return throwError(() => err);
      })
    );
  }

  getById(id: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.base}/${id}`).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en getById:', err);
        return throwError(() => err);
      })
    );
  }

  create(solicitud: any): Observable<Solicitud> {
    return this.http.post<Solicitud>(this.base, solicitud).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en create:', err);
        return throwError(() => err);
      })
    );
  }

  update(id: number, solicitud: any): Observable<Solicitud> {
    return this.http.put<Solicitud>(`${this.base}/${id}`, solicitud).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en update:', err);
        return throwError(() => err);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en delete:', err);
        return throwError(() => err);
      })
    );
  }

  getByEstado(estado: string): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.base}/estado/${estado}`).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en getByEstado:', err);
        return throwError(() => err);
      })
    );
  }

  getByOrganizador(idOrganizador: number): Observable<Solicitud[]> {
    const url = `${this.base}/organizador/${idOrganizador}`;
    console.log('Haciendo petición a:', url);
    return this.http.get<Solicitud[]>(url).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en getByOrganizador:', err);
        if (err.name === 'TimeoutError') {
          console.error('La petición excedió el tiempo de espera de', this.timeoutMs, 'ms');
        }
        return throwError(() => err);
      })
    );
  }

  getByProveedor(idProveedor: number): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.base}/proveedor/${idProveedor}`).pipe(
      timeout(this.timeoutMs),
      catchError(err => {
        console.error('Error en getByProveedor:', err);
        return throwError(() => err);
      })
    );
  }
  // --- Dentro de SolicitudesService ---
actualizarEstado(id: number, estado: string): Observable<Solicitud> {
  const url = `${this.base}/${id}/estado`; // /ms-reservas/solicitudes/{id}/estado
  return this.http.patch<Solicitud>(url, {}, { params: { estado } }).pipe(
    timeout(this.timeoutMs),
    catchError(err => {
      console.error(`Error actualizando estado de la solicitud ${id}:`, err);
      return throwError(() => err);
    })
  );
}


}
