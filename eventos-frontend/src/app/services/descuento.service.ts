import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Descuento } from '../models/descuento.model';

@Injectable({
  providedIn: 'root'
})
export class DescuentosService {

  private baseUrl = '/ofertas/ofertas';

  constructor(private http: HttpClient) {}

  agregarDescuento(ofertaId: number, descuento: Descuento): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/${ofertaId}/descuentos`,
      descuento
    );
  }

  eliminarDescuento(ofertaId: number, descuentoId: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/${ofertaId}/descuentos/${descuentoId}`
    );
  }
}
