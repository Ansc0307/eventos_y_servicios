import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Solicitud } from '../models/solicitud.model';

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  // frontend proxy: /api -> http://localhost:8080/ms-reservas
  private readonly base = '/api/solicitudes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(this.base);
  }

  getById(id: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.base}/${id}`);
  }

  create(solicitud: any): Observable<Solicitud> {
    return this.http.post<Solicitud>(this.base, solicitud);
  }

  update(id: number, solicitud: any): Observable<Solicitud> {
    return this.http.put<Solicitud>(`${this.base}/${id}`, solicitud);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getByEstado(estado: string): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.base}/estado/${estado}`);
  }

  getByOrganizador(idOrganizador: number): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.base}/organizador/${idOrganizador}`);
  }

  getByProveedor(idProveedor: number): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.base}/proveedor/${idProveedor}`);
  }
}
