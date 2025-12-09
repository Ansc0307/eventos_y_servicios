import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';

@Injectable({ providedIn: 'root' })
export class CategoriasService {
  private apiUrl = '/api/categorias';

  constructor(private http: HttpClient) {}

  obtenerCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  crearCategoria(payload: Partial<Categoria>): Observable<Categoria> {
    return this.http.post<Categoria>(this.apiUrl, payload);
  }
}
