import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Oferta } from '../models/oferta.model';

@Injectable({ providedIn: 'root' })
export class OfertasService {

  private baseOfertas = '/ofertas/ofertas';
  private baseCategorias = '/ofertas/categorias';

  constructor(private http: HttpClient) {}

  // === OFERTAS ===
  getAll(): Observable<Oferta[]> {
    return this.http.get<Oferta[]>(this.baseOfertas);
  }

  getById(id: number): Observable<Oferta> {
    return this.http.get<Oferta>(`${this.baseOfertas}/${id}`);
  }

  create(payload: Partial<Oferta>): Observable<Oferta> {
    return this.http.post<Oferta>(this.baseOfertas, payload);
  }

  update(id: number, payload: Partial<Oferta>): Observable<Oferta> {
    return this.http.put<Oferta>(`${this.baseOfertas}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseOfertas}/${id}`);
  }

  // === CATEGORÍAS ===
  getCategorias(): Observable<any[]> {
    return this.http.get<any[]>(this.baseCategorias);
  }

  createCategoria(payload: any): Observable<any> {
    return this.http.post<any>(this.baseCategorias, payload);
  }
}
