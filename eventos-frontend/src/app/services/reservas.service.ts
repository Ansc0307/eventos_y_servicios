import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reserva } from '../models/reserva.model';

@Injectable({ providedIn: 'root' })
export class ReservasService {
  // use the frontend proxy: /api -> http://localhost:8080/ms-reservas
  private readonly base = '/ms-reservas/api/v1/reservas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.base);
  }

  getById(id: number) {
    return this.http.get<Reserva>(`${this.base}/${id}`);
  }
}
