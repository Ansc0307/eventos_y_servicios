import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Solicitud } from '../models/solicitud.model';

@Injectable({ providedIn: 'root' })
export class SolicitudesService {
  // frontend proxy: /api -> http://localhost:8080/ms-reservas
  private readonly base = '/ms-reservas/api/solicitudes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(this.base);
  }

  getById(id: number) {
    return this.http.get<Solicitud>(`${this.base}/${id}`);
  }
}
