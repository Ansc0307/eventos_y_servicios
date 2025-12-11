import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Oferta } from '../models/oferta.model';

@Injectable({ providedIn: 'root' })
export class OfertasService {
  private http = inject(HttpClient);
  // usar la ruta del proxy: /api -> ms-ofertas
  private apiUrl = '/api/ofertas';

  obtenerOfertas(): Observable<Oferta[]> {
    return this.http.get<Oferta[]>(this.apiUrl);
  }

  obtenerOferta(id: number): Observable<Oferta> {
    return this.http.get<Oferta>(`${this.apiUrl}/${id}`);
  }

  crearOferta(payload: Partial<Oferta>): Observable<Oferta> {
    return this.http.post<Oferta>(this.apiUrl, payload);
  }

  actualizarOferta(id: number, payload: Partial<Oferta>): Observable<Oferta> {
    return this.http.put<Oferta>(`${this.apiUrl}/${id}`, payload);
  }

  eliminarOferta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  agregarDescuento(ofertaId: number, payload: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${ofertaId}/descuentos`, payload);
  }

  eliminarDescuento(ofertaId: number, descuentoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${ofertaId}/descuentos/${descuentoId}`);
  }
}
